-- ShopNest database foundation (Supabase / PostgreSQL)
-- Apply in Supabase SQL Editor on a NEW project.
-- Starter schema only; not a complete production payment/shipping system.
-- Never expose the Supabase service_role key in a browser or Android app.
create extension if not exists pgcrypto;
create table if not exists public.profiles (
 id uuid primary key references auth.users(id) on delete cascade,
 full_name text, phone text, preferred_language text not null default 'hi',
 account_role text not null default 'buyer' check (account_role in ('buyer','retailer','seller','admin')),
 created_at timestamptz not null default now()
);
create table if not exists public.categories (
 id uuid primary key default gen_random_uuid(), name text not null, slug text not null unique,
 parent_id uuid references public.categories(id) on delete set null,
 is_active boolean not null default true, created_at timestamptz not null default now()
);
create table if not exists public.stores (
 id uuid primary key default gen_random_uuid(), owner_id uuid not null references public.profiles(id) on delete restrict,
 store_name text not null, description text,
 status text not null default 'pending' check (status in ('pending','approved','rejected','suspended')),
 created_at timestamptz not null default now()
);
create table if not exists public.products (
 id uuid primary key default gen_random_uuid(), store_id uuid not null references public.stores(id) on delete cascade,
 category_id uuid references public.categories(id) on delete set null, name text not null, description text, sku text,
 price_paise bigint not null check (price_paise >= 0), mrp_paise bigint check (mrp_paise is null or mrp_paise >= price_paise),
 stock_quantity integer not null default 0 check (stock_quantity >= 0), image_url text,
 status text not null default 'draft' check (status in ('draft','pending','active','rejected','archived')),
 created_at timestamptz not null default now(), updated_at timestamptz not null default now(), unique(store_id, sku)
);
create table if not exists public.addresses (
 id uuid primary key default gen_random_uuid(), user_id uuid not null references public.profiles(id) on delete cascade,
 recipient_name text not null, phone text not null, address_line1 text not null, address_line2 text,
 city text not null, state text not null, postal_code text not null, country text not null default 'India',
 is_default boolean not null default false, created_at timestamptz not null default now()
);
create table if not exists public.orders (
 id uuid primary key default gen_random_uuid(), order_number text not null unique,
 buyer_id uuid not null references public.profiles(id) on delete restrict, address_snapshot jsonb not null,
 subtotal_paise bigint not null check (subtotal_paise >= 0), shipping_paise bigint not null default 0 check (shipping_paise >= 0),
 total_paise bigint not null check (total_paise >= 0),
 payment_method text not null default 'cod' check (payment_method in ('cod','upi','card')),
 payment_status text not null default 'pending' check (payment_status in ('pending','paid','failed','refunded')),
 order_status text not null default 'placed' check (order_status in ('placed','confirmed','packed','shipped','delivered','cancelled','returned')),
 created_at timestamptz not null default now()
);
create table if not exists public.order_items (
 id uuid primary key default gen_random_uuid(), order_id uuid not null references public.orders(id) on delete cascade,
 product_id uuid references public.products(id) on delete set null, store_id uuid references public.stores(id) on delete set null,
 product_name_snapshot text not null, quantity integer not null check (quantity > 0),
 unit_price_paise bigint not null check (unit_price_paise >= 0), line_total_paise bigint not null check (line_total_paise >= 0)
);
create index if not exists products_category_status_idx on public.products(category_id,status);
create index if not exists products_store_status_idx on public.products(store_id,status);
create index if not exists orders_buyer_created_idx on public.orders(buyer_id,created_at desc);
create index if not exists order_items_order_idx on public.order_items(order_id);
alter table public.profiles enable row level security;
alter table public.categories enable row level security;
alter table public.stores enable row level security;
alter table public.products enable row level security;
alter table public.addresses enable row level security;
alter table public.orders enable row level security;
alter table public.order_items enable row level security;
create policy "profiles_select_own" on public.profiles for select to authenticated using (id=auth.uid());
-- SECURITY DEFINER helper avoids recursive profile-table RLS checks.
create or replace function public.current_profile_role()
returns text language sql stable security definer
set search_path = ''
as $
  select p.account_role from public.profiles p where p.id = (select auth.uid())
$;
revoke all on function public.current_profile_role() from public;
grant execute on function public.current_profile_role() to authenticated;

create policy "profiles_update_own" on public.profiles for update to authenticated
 using (id=auth.uid())
 with check (id=auth.uid() and account_role=public.current_profile_role());

-- Automatically create a basic buyer profile when a new Auth user signs up.
create or replace function public.handle_new_auth_user()
returns trigger language plpgsql security definer
set search_path = ''
as $
begin
  insert into public.profiles (id, full_name, phone)
  values (
    new.id,
    coalesce(new.raw_user_meta_data ->> 'full_name', ''),
    coalesce(new.phone, '')
  )
  on conflict (id) do nothing;
  return new;
end;
$;

drop trigger if exists on_auth_user_created_shopnest on auth.users;
create trigger on_auth_user_created_shopnest
  after insert on auth.users
  for each row execute procedure public.handle_new_auth_user();
create policy "categories_read_active" on public.categories for select to anon,authenticated using (is_active=true);
create policy "products_read_active" on public.products for select to anon,authenticated using (status='active');
create policy "stores_owner_read" on public.stores for select to authenticated using (owner_id=auth.uid());
create policy "stores_owner_create" on public.stores for insert to authenticated with check (owner_id=auth.uid() and status='pending');
create policy "stores_owner_update_pending" on public.stores for update to authenticated using (owner_id=auth.uid() and status='pending')
 with check (owner_id=auth.uid() and status='pending');
create policy "products_owner_read" on public.products for select to authenticated using
 (exists(select 1 from public.stores s where s.id=store_id and s.owner_id=auth.uid()));
create policy "products_owner_insert" on public.products for insert to authenticated with check
 (status in ('draft','pending') and exists(select 1 from public.stores s where s.id=store_id and s.owner_id=auth.uid() and s.status='approved'));
create policy "products_owner_update" on public.products for update to authenticated using
 (exists(select 1 from public.stores s where s.id=store_id and s.owner_id=auth.uid()))
 with check (exists(select 1 from public.stores s where s.id=store_id and s.owner_id=auth.uid()));
create policy "addresses_owner_all" on public.addresses for all to authenticated using (user_id=auth.uid()) with check (user_id=auth.uid());
create policy "orders_buyer_read" on public.orders for select to authenticated using (buyer_id=auth.uid());
create policy "order_items_buyer_read" on public.order_items for select to authenticated using
 (exists(select 1 from public.orders o where o.id=order_id and o.buyer_id=auth.uid()));
-- Follow-up backend work: server-side checkout with price/stock validation,
-- admin-only policies via trusted claims, payment webhook verification, shipping/refunds, audit logs and tests.
