# Module Dependency Graph

## Root Project (Shell)

```
popcornguineapig-detekt-rule (root)
  └── settings.gradle.kts — composite build including submodule
  └── README.md — user-facing docs
  └── AGENTS.md — AI context master initializer (DO NOT DUPLICATE)
  └── ai/ — centralized AI context
```

## Submodule: `popcornguineapig-detekt-rule/`

```
popcornguineapig-detekt-rule/
  ├── src/main/kotlin/com/github/codandotv/popcorngpdetekt/
  │   ├── data/
  │   │   └── PopcornGuineapigDetektRuleRepositoryImpl.kt
  │   │       └── depends on: domain/PopcornGuineapigDetektRuleRepository.kt
  │   ├── domain/
  │   │   ├── models/
  │   │   │   ├── DependencyRulesConfig.kt       (top-level config schema)
  │   │   │   ├── RuleConfig.kt                   (per-rule config schema)
  │   │   │   └── ArchitectureViolationError.kt   (violation model)
  │   │   ├── PopcornGuineapigDetektRuleRepository.kt  (interface)
  │   │   ├── AnalyseArchitectureUseCase.kt       (orchestrates checkers)
  │   │   ├── ExclusiveDependenciesRuleChecker.kt (exclusive dep logic)
  │   │   ├── ForbiddenDependenciesRuleChecker.kt (forbidden dep logic)
  │   │   └── Logger.kt                           (debug logging)
  │   ├── presentation/
  │   │   ├── PopcornRuleSetProvider.kt           (rule set registration)
  │   │   ├── rules/
  │   │   │   └── ArchitectureRulesRule.kt        (main rule implementation)
  │   │   └── KtFileExt.kt                        (extension functions)
  │   └── ServiceLocator.kt                       (DI composition root)
  └── src/test/kotlin/com/github/codandotv/popcorngpdetekt/
      ├── domain/
      │   ├── AnalyseArchitectureUseCaseTest.kt
      │   ├── ExclusiveDependenciesRuleCheckerTest.kt
      │   └── ForbiddenDependenciesRuleCheckerTest.kt
      └── presentation/
          └── KtFileExtensionsTest.kt
```

## Dependency Direction (Clean Architecture)

```
presentation/ ──→ domain/ ──→ data/
     │                 │
     │                 └── models/ (shared data structures)
     │
     └── ServiceLocator knows about all layers
```

- `presentation/` depends on `domain/` (injects use cases)
- `domain/` depends on `data/` (via repository interface)
- `data/` implements repository interface
- `models/` are pure data classes with no dependencies
- No layer depends on an outer layer
