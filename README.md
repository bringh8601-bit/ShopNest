# ShopNest — Luxury Prime Edition

ShopNest is an Android-first multi-vendor shopping marketplace starter for India.

## Design
- Premium navy blue and gold visual identity
- Hindi-first shopping experience with English-friendly labels
- Search, category browsing, product cards and demo cart
- Planned roles: customer, seller and admin

## Planned categories
Mobiles, electronics, fashion, footwear, grocery, beauty, home & kitchen, toys, books, sports and more.

## Android setup
Open this repository in Android Studio or another Android IDE that supports Gradle projects.
- Package: `in.shopnest.app`
- Language: Kotlin
- UI: Jetpack Compose
- Minimum Android: API 24

## Important status
This is an early UI starter, not a production marketplace. Login, seller onboarding, database, inventory, order management, shipping, refunds, admin security and real payment gateway integration still need to be implemented and tested. UPI, cards and COD are planned options only; no real payments are active.

Never put payment secrets or private API keys in the app source.

## Database foundation (new)
A starter Supabase/PostgreSQL schema is available at [docs/database-schema.sql](docs/database-schema.sql).
It defines profiles, categories, stores, products, addresses, orders and order items, with initial row-level security policies.

### Setup checklist
1. Create a Supabase project.
2. Open **SQL Editor** and run the contents of `docs/database-schema.sql`.
3. Configure authentication and a trusted profile-creation trigger before testing sign-in.
4. Build checkout as a server-side function that rechecks product prices and stock.
5. Add verified payment webhooks and admin-only controls before accepting real orders or money.

The schema is a development foundation only. It does not activate real payments, shipping, KYC, or production admin access.
