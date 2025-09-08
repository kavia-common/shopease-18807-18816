# Shopease Android (Kotlin, MVVM, No Compose)

This module contains a non-Compose Android app using MVVM, ViewBinding, Retrofit (mock), and AndroidX Navigation.

Features:
- Authentication (login/signup) using mock API
- Product browsing and search
- Cart management (+/-, total)
- Checkout flow (mock place order)
- Order history and status
- Profile with logout
- ServiceLocator for simple DI
- Retrofit interface with mock implementation and clear integration points to connect real backend

Build/Run:
- ./gradlew :app:assembleDebug
- Install and run the app. The launcher opens MainActivity. Use Profile to logout and AuthActivity to log back in.

Integration Notes:
- Replace NetworkModule.provideApiService(useMock = true) with real Retrofit service when backend is available.
- Move API base URL to BuildConfig field and/or an environment-driven config.
- Replace ServiceLocator with DI framework if desired (e.g., Hilt).
