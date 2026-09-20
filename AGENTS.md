# Popcorn Guineapig Detekt Rule — AGENTS.md

OpenCode project context. This is the single source of truth for AI sessions — do not
duplicate its content elsewhere.

---

## What This Project Is

A custom [detekt](https://detekt.dev/) plugin that enforces architectural boundaries in
Kotlin projects via static analysis. Users define rules in JSON config; the plugin
checks that internal imports conform to the declared architecture (exclusive dependencies,
forbidden dependencies, no-dependency files). Published by CodandoTV.

**Maven Central:** `io.github.codandotv:popcornguineapig-detekt-rule:1.0.1`  
**License:** MIT

---

## OpenCode Setup

- Config: `opencode.json` at repo root — `instructions: ["AGENTS.md"]`. Project
  skills are auto-discovered from `.opencode/skills/`.
- This `AGENTS.md` is auto-loaded into every session.
- Skills are loaded on demand via the `skill` tool; the system prompt matches task
  descriptions against each `SKILL.md` `description`.
- Task workflow: read this file → find a matching skill in `.opencode/skills/` → read its
  `SKILL.md` → implement → run validation.

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Kotlin (JVM) | 2.2.0 |
| Framework | detekt API | 1.23.8 |
| Serialization | kotlinx-serialization-json | 1.9.0 |
| Build | Gradle (wrapper) | 9.0.0 |
| Coverage | Kover | 0.8.0 |
| Publish | Vanniktech Maven Publish | 0.36.0 |

---

## Folder / Module Structure

```
/                                       # Root shell — composite build
├── popcornguineapig-detekt-rule/       # Single submodule (all source code)
│   ├── src/main/kotlin/com/github/codandotv/popcorngpdetekt/
│   │   ├── ServiceLocator.kt           # DI composition root — knows all layers
│   │   ├── data/
│   │   │   └── PopcornGuineapigDetektRuleRepositoryImpl.kt  # implements domain repository
│   │   ├── domain/
│   │   │   ├── models/
│   │   │   │   ├── DependencyRulesConfig.kt       # top-level config schema
│   │   │   │   ├── RuleConfig.kt                  # per-rule config schema
│   │   │   │   └── ArchitectureViolationError.kt  # violation model
│   │   │   ├── PopcornGuineapigDetektRuleRepository.kt  # repository interface
│   │   │   ├── AnalyseArchitectureUseCase.kt      # orchestrates checkers
│   │   │   ├── ExclusiveDependenciesRuleChecker.kt  # exclusive dependency logic
│   │   │   ├── ForbiddenDependenciesRuleChecker.kt  # forbidden dependency logic
│   │   │   └── Logger.kt                          # debug logging
│   │   └── presentation/
│   │       ├── PopcornRuleSetProvider.kt          # rule set registration
│   │       ├── rules/
│   │       │   └── ArchitectureRulesRule.kt       # main rule implementation
│   │       └── KtFileExt.kt                       # extension functions
│   ├── src/test/kotlin/com/github/codandotv/popcorngpdetekt/
│   │   ├── domain/
│   │   │   ├── AnalyseArchitectureUseCaseTest.kt
│   │   │   ├── ExclusiveDependenciesRuleCheckerTest.kt
│   │   │   └── ForbiddenDependenciesRuleCheckerTest.kt
│   │   └── presentation/
│   │       └── KtFileExtensionsTest.kt
│   └── version.properties              # VERSION=1.0.1 — source of the published version
├── docs/                               # Zensical documentation
├── .opencode/
│   └── skills/                         # Task-specific guidance (auto-discovered)
├── gradle/libs.versions.toml           # Version catalog
├── .github/workflows/                  # CI pipelines
├── AGENTS.md                           # THIS FILE
└── README.md                           # User-facing docs
```

The Gradle module path is `:popcornguineapig-detekt-rule` (same name as the repo root).

---

## Available Skills

Before starting any task:
1. List files in `.opencode/skills/` to find a matching skill directory
2. Read the `SKILL.md` file inside the matching directory
3. Follow its instructions before proceeding

| Skill | Task |
|---|---|
| `documentation-review` | Review docs/ for broken links, stale JSON config examples, inconsistencies with README.md and source |
| `minimum-requirements` | Verify and update minimum dependency versions (Kotlin, JVM, detekt, Gradle) in README.md |
| `trigger-release` | Cut a release — bump version.properties, update CHANGELOG.md, push v* tag to trigger publish |

### Maintenance
- Adding a skill: create `.opencode/skills/<skill-name>/SKILL.md` — no other file needs
  updating; opencode auto-discovers it at startup.
- Do not add per-assistant context files (Claude, Cursor, Copilot, Gemini). OpenCode is
  the sole supported assistant; anything that would go in such a file belongs in this
  `AGENTS.md`.

---

## Commands

```bash
# Tests only
./gradlew :popcornguineapig-detekt-rule:test

# Tests + coverage report (this is the CI gate)
./gradlew :popcornguineapig-detekt-rule:koverHtmlReport

# Run a single test class
./gradlew :popcornguineapig-detekt-rule:test \
  --tests "com.github.codandotv.popcorngpdetekt.domain.ExclusiveDependenciesRuleCheckerTest"
```

### Validation before marking done
1. Run `./gradlew :popcornguineapig-detekt-rule:koverHtmlReport` — must pass
2. Verify tests exist for new logic (tests mirror `src/main` under `src/test/kotlin/.../`)
3. If adding new configuration fields: update both `DependencyRulesConfig.kt` and `RuleConfig.kt`
4. If adding a new checker: wire it into `AnalyseArchitectureUseCase.kt`

---

## Critical Architectural Rules

### Configuration Schema
```json
{
  "packagePrefix": "com.example.app",       // Required — base package
  "debug": false,                           // Optional — default false
  "rules": [
    {
      "filePattern": "^.*/domain/.+\\.kt$", // Required — regex matching files
      "dependenciesAllowed": false,         // Optional — deny all internal deps
      "exclusiveDependencies": ["..."],     // Optional — only these deps allowed
      "forbiddenDependencies": ["..."]      // Optional — these deps forbidden
    }
  ]
}
```

### Evaluation Order
For the **first** rule whose `filePattern` matches, checks run in this order (first match wins):
1. `dependenciesAllowed = false` — any internal import is a violation
2. `forbiddenDependencies` — any import matching a forbidden pattern is a violation
3. `exclusiveDependencies` — any import not matching an exclusive pattern is a violation

### Clean Architecture Enforcement
```
presentation/ ──→ domain/ ──→ data/
     │                 │
     │                 └── models/ (shared data structures)
     │
     └── ServiceLocator knows about all layers
```
- `presentation/` depends on `domain/` (injects use cases)
- `domain/` depends on `data/` (via the repository interface)
- `data/` implements the repository interface
- Models (`domain/models/`) are pure data classes with no dependencies
- `ServiceLocator.kt` is the DI composition root — it knows all layers
- Never add a dependency that points outward

### File Organization
- One class per file
- `explicitApi()` is enabled on the submodule — new public declarations need explicit
  visibility (`public`/`internal`) and explicit return types
- Use `internal` for implementation details
- Public API detekt sees: `PopcornRuleSetProvider` and `ArchitectureRulesRule`

### Dependency Quirks
- `detekt-api` is `compileOnly` — the plugin is not runnable standalone
- Tests use `detekt-test`; unit-test checkers/use cases directly
- Version lives only in `popcornguineapig-detekt-rule/version.properties` (not root)

---

## CI / Automation Overview

| Workflow | Trigger | What It Does |
|---|---|---|
| `pr.yml` | PR to `main` | Runs `./gradlew :popcornguineapig-detekt-rule:koverHtmlReport` |
| `publish.yml` | Tag push (`v*`) | Publishes to Maven Central |
| `documentation.yml` | PR to `main` (docs changes) | Deploys Zensical site via `zensical build` |

CI runs on JDK 21.

---

## PR Review Checklist

- [ ] Does the code follow Clean Architecture layering?
- [ ] Are new configuration fields added to both `DependencyRulesConfig` and `RuleConfig`?
- [ ] Are new checkers wired into `AnalyseArchitectureUseCase`?
- [ ] Do unit tests cover the new logic?
- [ ] Does `./gradlew :popcornguineapig-detekt-rule:koverHtmlReport` pass?
- [ ] Are `explicitApi()` visibility rules respected?
- [ ] Is the evaluation order preserved in the rule chain?
- [ ] Is the version bumped in `popcornguineapig-detekt-rule/version.properties` if the public API changed?
