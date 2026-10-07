---
name: compose-architecture-rules
description: Core architecture guidelines, anti-patterns, UDF flow, state management standards, and Jetpack Compose best practices for Nova UI Kit.
---

# Compose Architecture & Design Rules

## Core Architectural Principles
1. **Unidirectional Data Flow (UDF):**
   - State flows DOWN (from ViewModel to Screen).
   - Events flow UP (from Screen to ViewModel).
   - Side-effects are handled out-of-band via explicit `UiEffect` channels.

2. **Class Triad Pattern:**
   Every feature screen MUST consist of three dedicated files:
   - `[Feature]Models.kt` (UI State, UI Events, UI Side-Effects)
   - `[Feature]ViewModel.kt` (Business logic, StateFlow emissions, Effect channel)
   - `[Feature]Screen.kt` (Stateful Wrapper + Stateless Composable + Previews)

3. **State Structure Guidelines:**
   - **Sealed Interface UI State:** Use for screen-level async states (`Loading`, `Success`, `Error`).
   - **Data Class State:** Use for interactive screens with multiple distinct form fields/inputs to avoid full-screen hierarchy destruction on state updates.

## Anti-Pattern Enforcement

| AI Default Pattern | Why It Is Rejected | Production-Grade Requirement |
|---|---|---|
| `viewModel()` factory | Breaks modular DI testing | ALWAYS use `hiltViewModel()` |
| Hardcoded UI text | Breaks multi-language localization | ALL strings wrapped in `stringResource(R.string.*)` |
| `modifier` at the end | Violates Compose positional API | `modifier: Modifier = Modifier` MUST be the FIRST parameter |
| `Scaffold` in Wrapper | Prevents stateless UI previews | `Scaffold` belongs strictly inside the Stateless UI Composable |
| Single-shot events in `UiState` | Causes double-execution on recomposition | Use `Channel<UiEffect>` for navigation and toasts |
| Raw `dp` values for layout | Causes visual inconsistency | Use design system tokens or theme spacing (`MaterialTheme.spacing.*`) |

## Design System & Styling Rules
- **Positional Parameter Order:** Every Composable MUST accept `modifier: Modifier = Modifier` as its **first optional parameter**.
- **Multi-Preview Standard:** Provide previews for both Light Mode and Dark Mode for all stateless composables.