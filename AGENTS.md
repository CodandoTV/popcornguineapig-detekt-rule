# Popcorn Guineapig Detekt Rule — AGENTS.md

**Master initializer for AI coding assistants.**  
Every AI assistant reads this file first. Do not duplicate its content elsewhere.

---

## What This Project Is

A custom [detekt](https://detekt.dev/) plugin that enforces architectural boundaries in
Kotlin projects via static analysis. Users define rules in JSON config; the plugin
checks that internal imports conform to the declared architecture (exclusive dependencies,
forbidden dependencies, no-dependency files). Published by CodandoTV.

**Maven Central:** `io.github.codandotv:popcornguineapig-detekt-rule:1.0.0`  
**License:** MIT

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Kotlin (JVM) | 2.2.0 |
| Framework | detekt API | 1.23.8 |
| Serialization | kotlinx-serialization-json | 1.9.0 |
| Build | Gradle (wrapper) | 9.0+ |
| Coverage | Kover | 0.8.0 |
| Publish | Vanniktech Maven Publish | 0.36.0 |

---

## Folder / Module Structure

```
/                                       # Root shell — composite build
├── popcornguineapig-detekt-rule/       # Single submodule (all source code)
│   ├── src/main/kotlin/.../
│   │   ├── data/       — Repository implementation
│   │   ├── domain/     — Use cases, checkers, models
│   │   │   └── models/ — DependencyRulesConfig, RuleConfig, ArchitectureViolationError
│   │   └── presentation/ — RuleSet provider, rule, extensions
│   ├── src/test/kotlin/.../
│   └── version.properties              # VERSION=1.0.0
├── docs/                               # Zensical documentation
├── ai/                                 # Centralized AI context
│   ├── module-graph.md                 # Module dependency graph
│   ├── instructions/                   # Platform-specific patterns
│   └── skills/                         # Task-specific guidance
├── gradle/libs.versions.toml           # Version catalog
├── .github/workflows/                  # CI pipelines
├── AGENTS.md                           # THIS FILE
└── README.md                           # User-facing docs
```

---

## Platform Context — Which File to Load

| Assistant | Init File | Instructions |
|---|---|---|
| All | `AGENTS.md` (this file) | Master context — always read first |
| OpenCode | `openspec.json` → `ai/skills/` | `ai/instructions/opencode.md` |
| Claude Code | `CLAUDE.md` (pointer) | `ai/instructions/claude-code.md` |
| Cursor | `.cursorrules` (pointer) | `ai/instructions/cursor.md` |
| GitHub Copilot | `.github/copilot-instructions.md` (pointer) | `ai/instructions/copilot.md` |
| Gemini Code | `.gemini/context.md` (pointer) | `ai/instructions/gemini-code.md` |

All native init files are lightweight pointers to this file. Do not add rules to them.

---

## Available Skills

Before starting any task:
1. List files in `ai/skills/` to find a matching skill directory
2. Read the `SKILL.md` file inside the matching directory
3. Follow its instructions before proceeding

Current skills:

| Skill | Task |
|---|---|
| `documentation-review` | Scan docs/ for broken links, stale config examples, inconsistencies |
| `minimum-requirements` | Verify and update README minimum dependency versions |
| `release-notes` | Bump version, generate changelog, prompt to commit/tag |

---

## How to Implement Tasks

### Build & Test
```bash
# Run all tests with coverage
./gradlew :popcornguineapig-detekt-rule:koverHtmlReport

# Run tests only
./gradlew :popcornguineapig-detekt-rule:test
```

### Validation before marking done
1. Run `./gradlew :popcornguineapig-detekt-rule:koverHtmlReport` — must pass
2. Verify tests exist for new logic (tests are in `src/test/kotlin/.../`)
3. If adding new configuration fields: update `DependencyRulesConfig.kt` and `RuleConfig.kt`
4. If adding a new checker: wire it into `AnalyseArchitectureUseCase.kt`

### When a task is complete
- Update the todo list, marking the task `completed`
- If you created/modified files, note the file paths in your response

---

## Critical Architectural Rules

### Configuration Schema
```json
{
  "packagePrefix": "com.example.app",       // Required — base package
  "debug": false,                           // Optional — debug logging
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
For a file matching `filePattern`, checks run in this order (first match wins):
1. `dependenciesAllowed = false` — any internal import is a violation
2. `forbiddenDependencies` — any import matching a forbidden pattern is a violation
3. `exclusiveDependencies` — any import not matching an exclusive pattern is a violation

### Clean Architecture Enforcement
- `presentation/` → `domain/` → `data/` (inward dependency only)
- `data/` implements `domain/` repository interface
- Models (`domain/models/`) are pure data classes with no dependencies
- `ServiceLocator.kt` is the DI composition root — knows all layers
- Never add a dependency that points outward

### File Organization
- One class per file (Kotlin convention, `explicitApi()` enabled)
- Tests mirror the source structure under `src/test/kotlin/`
- Use `internal` visibility for implementation details
- Public API is what detekt sees: `PopcornRuleSetProvider` and `ArchitectureRulesRule`

---

## CI / Automation Overview

| Workflow | Trigger | What It Does |
|---|---|---|
| `pr.yml` | PR to `main` | Runs `./gradlew :popcornguineapig-detekt-rule:koverHtmlReport` |
| `publish.yml` | Manual dispatch | Publishes to Maven Central, runs Fastlane to update tag |
| `documentation.yml` | PR to `main` (docs changes) | Deploys Zensical site via `zensical build` |

---

## PR Review Checklist

- [ ] Does the code follow Clean Architecture layering?
- [ ] Are new configuration fields added to both `DependencyRulesConfig` and `RuleConfig`?
- [ ] Are new checkers wired into `AnalyseArchitectureUseCase`?
- [ ] Do unit tests cover the new logic?
- [ ] Does `./gradlew :popcornguineapig-detekt-rule:koverHtmlReport` pass?
- [ ] Are `explicitApi()` visibility rules respected?
- [ ] Is the evaluation order preserved in the rule chain?
- [ ] Is the version bumped in `version.properties` if the public API changed?
