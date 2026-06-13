# Popcorn Guineapig Detekt Rule — AGENTS.md

## Project Overview

Custom [detekt](https://detekt.dev/) plugin that adds code-level static analysis rules to enforce architectural boundaries in Kotlin projects. Published by CodandoTV.

## Language & Runtime

- **Language:** Kotlin 2.2.0
- **Runtime:** JVM
- **Framework:** detekt API 1.23.8
- **Library serialization:** kotlinx-serialization-json 1.9.0

## Build System

- **Build tool:** Gradle (version resolved via Gradle wrapper)
- **Version catalog:** `gradle/libs.versions.toml`
- **Plugins:** Kotlin JVM, Kotlin Serialization, Vanniktech Maven Publish, Kover

## Modules

Single submodule at `popcornguineapig-detekt-rule/`, included as a composite build from the root `settings.gradle.kts`. The root project is a shell; all source code lives in the submodule.

Source structure follows Clean Architecture internally:
- `src/main/kotlin/.../data/` — Repository implementation
- `src/main/kotlin/.../domain/` — Use cases, checkers, models
- `src/main/kotlin/.../presentation/` — RuleSet provider, rule, extensions

## Version

Defined in `popcornguineapig-detekt-rule/version.properties`:

```
VERSION=1.0.0
```

## Documentation

- **MkDocs config:** `mkdocs.yml`
- **Documentation content:** `docs/` (Markdown files)
- **Site theme:** Material for MkDocs

## Git Tags

Current tags: `v1.0.0`

## Maven Central Coordinate

```
io.github.codandotv:popcornguineapig-detekt-rule:1.0.0
```

## Group & Artifact IDs

- **Group ID:** `io.github.codandotv`
- **Artifact ID:** `popcornguineapig-detekt-rule`
- **License:** MIT

## Key Source Files

| File | Purpose |
|---|---|
| `src/main/kotlin/.../presentation/rules/ArchitectureRulesRule.kt` | Main rule implementation |
| `src/main/kotlin/.../domain/models/DependencyRulesConfig.kt` | Config schema (top-level) |
| `src/main/kotlin/.../domain/models/RuleConfig.kt` | Config schema (per-rule) |
| `src/main/kotlin/.../presentation/PopcornRuleSetProvider.kt` | RuleSet (id: `popcorn_guineapig_rules`) |
| `src/main/kotlin/.../domain/AnalyseArchitectureUseCase.kt` | Core analysis logic |
| `src/main/kotlin/.../domain/ExclusiveDependenciesRuleChecker.kt` | Exclusive dep checker |
| `src/main/kotlin/.../domain/ForbiddenDependenciesRuleChecker.kt` | Forbidden dep checker |
