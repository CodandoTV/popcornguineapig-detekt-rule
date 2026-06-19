---
name: minimum-requirements
description: Checks all project dependencies and updates README.md with the minimum requirements for using the library
---

# minimum-requirements

When invoked, this skill will:

1. Read the submodule `build.gradle.kts` at `popcornguineapig-detekt-rule/build.gradle.kts` to identify all declared dependencies:
   - `compileOnly(libs.detekt.api)` → maps to detekt API version from `gradle/libs.versions.toml`
   - `implementation(libs.kotlinx.serialization.json)` → maps to kotlinx-serialization-json version
   - `testImplementation` deps (not required for users, can be noted as test-only)

2. Read `gradle/libs.versions.toml` to resolve actual version numbers for all dependency references.

3. Derive the minimum requirements a project needs to use this library:
   - **detekt** ≥ `1.23.8` (compileOnly dep, users must have detekt)
   - **Kotlin** ≥ `2.2.0` (the library is compiled against this version)
   - **Gradle** (any version supported by the Gradle wrapper; list the wrapper version from `gradle/wrapper/gradle-wrapper.properties` if available)
   - **JVM** ≥ the target JVM version from the submodule's build config

4. Find the current version from `popcornguineapig-detekt-rule/version.properties`.

5. Check the current `README.md` — does it already have a "Minimum Requirements" section? If yes, update it with any new findings; if no, append the section before the existing "Setup" section (or at the end if no Setup section exists).

6. The Minimum Requirements section should look like:

   ```markdown
   ## Minimum Requirements

   | Dependency | Version |
   |---|---|
   | Kotlin | 2.2.0+ |
   | JVM | 11+ |
   | detekt | 1.23.8+ |
   | Gradle | 8.x+ |
   ```

   Adjust values based on the actual findings from the version catalog and build files.
