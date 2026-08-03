# Existing Rules

Popcornguineapig-detekt-rule provides **a single configurable rule** that enforces custom
architectural dependency constraints in your Kotlin projects.

## ArchitectureRulesRule

| Property        | Value                                    |
|-----------------|------------------------------------------|
| **Rule ID**     | `ArchitectureRulesRule`                  |
| **Severity**    | `CodeSmell`                              |
| **Debt**        | 5 minutes                                |
| **Description** | Define dependency rules for your project |

This rule inspects every Kotlin file in your project and checks its internal imports (imports
matching the configured `packagePrefix`) against the rules you define in the JSON configuration
file.

### Evaluation Order

For a given file matching a rule's `filePattern`, the checks are evaluated in the following order.
Only the **first matching condition** is applied:

1. **`dependenciesAllowed`** — if set to `false`, any internal import is reported as a violation.
2. **`forbiddenDependencies`** — if non-empty, any internal import matching one of the forbidden
   regex patterns is reported as a violation.
3. **`exclusiveDependencies`** — if non-empty, any internal import that does **not** match any of
   the exclusive regex patterns is reported as a violation.
4. If none of the conditions above are triggered (e.g., all fields are `null` or empty), no
   violation is reported.

### Rule Configuration Fields

Each rule entry in the `rules` array supports the following fields:

| Field                   | Type       | Description                                                                                       |
|-------------------------|------------|---------------------------------------------------------------------------------------------------|
| `filePattern`           | `String`   | Regex pattern matching the file paths this rule applies to.                                       |
| `dependenciesAllowed`   | `Boolean?` | When set to `false`, the matched file is **not allowed to have any internal imports**.            |
| `forbiddenDependencies` | `String[]` | Regex patterns matched against **fully qualified import paths** (e.g., `com.example.domain.SomeClass`). The file **must not** depend on imports matching these patterns. |
| `exclusiveDependencies` | `String[]` | Regex patterns matched against **fully qualified import paths** (e.g., `com.example.domain.SomeClass`). The file may **only** depend on imports matching at least one of these patterns. |

### Examples

**No dependencies allowed** — files matching `*Controller.kt` must have zero internal imports:

```json
{
  "filePattern": "^.*/.+Controller\\.kt$",
  "dependenciesAllowed": false
}
```

**Exclusive dependencies** — files under `domain/model` may only depend on other files in
`domain/model`:

```json
{
  "filePattern": "^.*/domain/model/.+\\.kt$",
  "exclusiveDependencies": [
    ".*\\.domain\\.model\\..*"
  ]
}
```

**Forbidden dependencies** — files under `data` must not depend on `presentation`:

```json
{
  "filePattern": "^.*/data/.+\\.kt$",
  "forbiddenDependencies": [
    ".*\\.presentation\\..*"
  ]
}
```

### RuleSet

This rule is registered under the rule set ID **`popcorn_guineapig_rules`** and referenced from your
`detekt.yml` as:

```yaml
popcorn_guineapig_rules:
  config: "config/detekt/popcorngp-config.json"
```
