---
name: android-dependency-guard
description: Strict dependency version management rules, KSP/Kotlin matching policies, and libs.versions.toml guardrails.
---

# Android Dependency Guard Policy

## Core Rules

1. **Zero-Guessing Policy:**
   - NEVER invent, bump, or guess library version numbers for Kotlin, KSP, AGP, Compose, or Jetpack libraries.
   - ALWAYS inspect `gradle/libs.versions.toml` to reuse existing version aliases and catalog references.

2. **Strict Compiler Coupling:**
   - **Kotlin & KSP:** KSP version MUST strictly match the base Kotlin version (`<kotlin_version>-<ksp_patch>`).
   - **Compose Compiler:** Ensure Compose Compiler / Kotlin Compiler plugin versions align with target Kotlin release.
   - **Java / JVM:** Ensure `sourceCompatibility` / `targetCompatibility` match `jvmTarget` across all modules.

3. **Incompatibility Stop Protocol:**
   - If a requested feature requires a library version not present or incompatible with current Gradle setup:
     1. **STOP execution immediately.**
     2. Do NOT generate broken or conflicting `build.gradle.kts` files.
     3. Highlight the missing dependency or mismatch and prompt for user approval.