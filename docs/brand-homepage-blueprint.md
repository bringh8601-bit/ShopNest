# ShopNest — Brand & Home Screen Blueprint

## Product direction
ShopNest is an Android-first, India-focused multi-vendor marketplace. The first release should be a polished, Hindi-first shopping experience with English-friendly labels and a language selector. Keep the interface simple enough for first-time smartphone shoppers.

## Brand system
- Name: ShopNest
- Positioning: Luxury Prime — trusted everyday shopping across India
- Primary: deep navy blue (#101B35)
- Accent: warm gold (#D6AE55)
- Background: warm ivory (#FAF8F2)
- Text: ink navy (#172033)
- Success: green; destructive: clear red
- Logo concept: a minimal gold shopping-bag outline with a subtle house/roof shape and a centered “S”; wordmark SHOPNEST in uppercase, “LUXURY PRIME” as a small descriptor.
- Use gold sparingly for premium emphasis; maintain strong contrast and readable type.

## Buyer home screen (first implementation target)
1. Top app bar: logo/wordmark, delivery location, cart icon with item count.
2. Search field: “क्या ढूँढ रहे हैं? / Search products”.
3. Address/delivery strip: “Deliver to…” with a location-change action.
4. Promotional banner carousel with accessible text labels and no fake countdowns.
5. Category grid: Mobiles, Electronics, Fashion, Footwear, Grocery, Beauty, Home & Kitchen, Toys, Books, Sports, plus “All categories”.
6. Featured sections: Best sellers, New arrivals, Deals for you.
7. Product card: image, product name, price in rupees, MRP/discount only when verified, rating only when real, wishlist and add-to-cart actions.
8. Bottom navigation: Home, Categories, Wishlist, Orders, Account.

## Role journeys
- Buyer: browse, search, wishlist, cart, address, checkout, order tracking, returns/support.
- Seller: apply for a store, wait for approval, add products, manage stock and orders, view payouts only after verified payment integration.
- Retailer: buyer-style bulk enquiry / repeat-order flow; do not grant seller permissions automatically.
- Admin: protected approval, catalog moderation, user support and audit screens. Admin authority must come from trusted server-side claims, never a user-editable profile field.

## India-first localization
- Keep UI strings in Android string resources; do not hard-code visible text in composables.
- Start with Hindi and English, then add other Indian languages through Android resource folders and reviewed translations.
- Format prices with India locale (₹); use phone and postal-code input validation appropriate for India.
- Support accessibility: scalable text, content descriptions, large touch targets and clear error messages.

## Design and implementation acceptance checklist
- Responsive layouts for common Android phone sizes.
- Loading, empty, error and offline states for every data-backed section.
- No fake products, ratings, discounts, stock claims or payment success.
- Cart totals and stock must be revalidated by a trusted backend before order creation.
- Never place Supabase service-role keys or payment secrets in the Android app.
- Add UI tests for navigation and role-gated screens before release.

## Current implementation boundary
This document is the design blueprint, not a claim that these screens or production services are already implemented. The existing Supabase schema is a starter foundation. Authentication provider configuration, secure role management, server-side checkout, payment verification, shipping, refunds and production testing remain separate tasks.
