# ShopNest Android — Supabase Email Authentication Setup

This guide is the next integration step after enabling the Email provider in Supabase.

## 1. Values needed for the Android app
In Supabase Dashboard, open Project Settings → API (or API Keys). Copy:
- Project URL (looks like https://YOUR_PROJECT_REF.supabase.co)
- Publishable key (preferred) or legacy anon/public key

These are client-side configuration values. Never use the service_role/secret key in Android, a website, screenshots, or chat. Do not commit private keys.

## 2. Email provider settings
In Authentication → Sign In / Providers → Email:
- Confirm Email may remain enabled; the app must show a “check your inbox” message after sign-up.
- Set a secure minimum password policy supported by Supabase.
- Configure Site URL and allowed redirect URLs for the actual app/web callback before testing email confirmation or password reset.
- Keep public sign-up enabled only if ShopNest is accepting open buyer registration.

## 3. Account roles and safety
The database profile trigger creates a basic buyer profile after Auth signup. A user must never be allowed to choose or update their own account_role to seller, retailer, or admin.
- Seller onboarding should create a pending store request; approval is performed by a trusted admin backend.
- Retailer access must be granted by trusted server-side workflow if/when enabled.
- Admin access must use trusted server-side authorization (for example, verified custom claims), not a client-editable profile field.
- Do not expose the Supabase service_role key in the Android app.

## 4. Required app flows
Implement and test these screens against the real Supabase project:
1. Sign up with name, email and password.
2. Sign in with email and password.
3. Sign out.
4. Restore the current session on app restart.
5. Email-confirmation pending state.
6. Password reset request and deep-link return.
7. Friendly Hindi error messages for invalid email, weak password, duplicate account, wrong credentials and network failure.

## 5. Integration inputs
Before wiring the app, record the Project URL and publishable/anon key in local, untracked build configuration (for example local.properties or an ignored secrets file) and inject them as BuildConfig fields. Ensure the file is git-ignored. Do not paste service_role keys or payment secrets into source code.

## 6. Acceptance checks
- A new user can register and receives the configured confirmation email.
- An unconfirmed user sees a clear pending-confirmation message.
- A confirmed user can sign in and remain signed in after restarting the app.
- Sign out clears the local session.
- A normal buyer cannot grant themselves seller, retailer or admin permissions.
- No service_role key appears in source, build artifacts, logs or client configuration.
- Test with a dedicated test account; do not use a real buyer's personal account for development.

## Current status
The Email provider is reported enabled by the project owner. This guide does not claim the Android client is already connected or that a live registration/sign-in test has passed. App integration still requires the project's URL and publishable/anon key, followed by a real-device test.
