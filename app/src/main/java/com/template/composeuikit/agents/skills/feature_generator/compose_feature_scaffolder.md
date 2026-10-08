---
name: compose-feature-scaffolder
description: Dynamic macros and commands (/feature, /ui, /scaffold-domain) to generate enterprise Compose feature scaffolding and wire navigation.
---

# Compose Feature Scaffolder & Commands

## Dynamic Commands & Macros

### 1. Feature Scaffolding Command: `/feature`
When executing `/feature [FeatureName] [Domain]`:
1. **Directory Verification:** Check for `core/`, `navigation/`, `ui/components/`, `ui/screens/`, `ui/theme/`, and `utils/`. Create missing directories.
2. **Domain Resolution:** Target path strictly `ui/screens/<domain>/<feature_folder>/`.
3. **Class Triad Generation:** Create `[FeatureName]Models.kt`, `[FeatureName]ViewModel.kt`, and `[FeatureName]Screen.kt` using templates.
4. **Unit Test Generation:** Generate `[FeatureName]ViewModelTest.kt` in the matching `test` package.
5. **Localization:** Append required string resource keys to `res/values/strings.xml`.
6. **Navigation Wiring:** Append route to `NavGraph.kt` in `navigation/`, instantiate ViewModel via `hiltViewModel()`, and wire callbacks.

### 2. Quick Screen Macro: `/ui`
When executing `/ui [ClassName] [Optional Components]`:
- If components specified: Generate screen incorporating those components via `/feature` rules.
- If empty: Generate clean scaffold with template placeholders.

### 3. Mass Domain Scaffolding Command: `/scaffold-domain`
When executing `/scaffold-domain [DomainName]`:
Generate full domain suites under `ui/screens/[DomainName]/`:
- `auth`: `Login`, `SignUp`, `ForgotPassword`, `Otp`, `Onboarding`
- `ecommerce`: `HomeFeed`, `ProductDetails`, `ShoppingCart`, `Checkout`, `OrderHistory`
- `finance`: `WalletDashboard`, `CardManagement`, `TransactionHistory`, `TransferMoney`
- `social`: `UserProfile`, `Settings`, `NotificationCenter`, `Messaging`
- `legal`: `GdprConsent`, `PrivacyTerms`, `Feedback`, `AboutApp`

## Pre-Flight Self-Correction Checklist (MANDATORY)
Before outputting code, run this internal audit:
- [ ] **Audit 1:** Is `modifier: Modifier = Modifier` the FIRST parameter in EVERY Composable?
- [ ] **Audit 2:** Are ALL strings localized using `stringResource()`?
- [ ] **Audit 3:** Is `hiltViewModel()` used in the Stateful Wrapper?
- [ ] **Audit 4:** Are single-shot events sent through a `UiEffect` Channel?