[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.codandotv/popcornguineapig-detekt-rule)](https://central.sonatype.com/artifact/io.github.codandotv/popcornguineapig-detekt-rule)

![Logo do CodandoTV](img/codandotv.png)

# Welcome! 👋

Welcome to the **popcornguineapig-detekt-rule**! A CodandoTV library : )

<img height="150px" width="100px" src="img/popcorngp-logo.webp" />

Custom [detekt](https://detekt.dev/) plugin from that adds code-level static analysis rules to enforce architectural boundaries in Kotlin projects.

## Minimum Requirements

| Dependency | Version |
|---|---|
| Kotlin | 2.2.0+ |
| JVM | 11+ |
| detekt | 1.23.8+ |
| Gradle | 9.0+ |

## 🚀 Setup

### 1. Add the dependency

```kotlin
// build.gradle.kts
detektPlugins("io.github.codandotv:popcornguineapig-detekt-rule:<version>")
```

### 2. Create the JSON configuration file

It is **important** to place the JSON configuration file at the same level as your `detekt.yml` definition (e.g., `config/detekt/popcorngp-config.json`):

```json
{
  "packagePrefix": "com.gabrielbmoro.moviedb",
  "debug": true,
  "rules": [
    {
      "filePattern": "^.*/domain/model/.+\\.kt$",
      "exclusiveDependencies": [
        "^.*/domain/model/.+\\.kt$"
      ]
    },
    {
      "filePattern": "^.*/.+Controller\\.kt$",
      "dependenciesAllowed": false
    }
  ]
}
```

### 3. Reference the configuration from `detekt.yml`

```yaml
popcorn_guineapig_rules:
    config: "config/detekt/popcorngp-config.json"
```

## Configuration Reference

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `packagePrefix` | `String` | Yes | The base package of your project (e.g., `com.example.app`). Only imports starting with this prefix are considered internal. |
| `debug` | `Boolean` | No | When `true`, enables debug logging. Defaults to `false`. |
| `rules` | `Array` | Yes | List of architecture rule definitions. |

### Rule Definition

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `filePattern` | `String` | Yes | Regex pattern matching the file paths this rule applies to. |
| `dependenciesAllowed` | `Boolean?` | No | When set to `false`, the matched file is **not allowed to have any internal imports**. |
| `exclusiveDependencies` | `String[]` | No | List of regex patterns. The file may **only** depend on packages matching at least one of these patterns. |
| `forbiddenDependencies` | `String[]` | No | List of regex patterns. The file **must not** depend on any package matching these patterns. |

### Evaluation Order

For a file matching `filePattern`, the checks are evaluated in the following order. Only the **first matching condition** is applied:

1. **`dependenciesAllowed`** — if `false`, any internal import is a violation.
2. **`forbiddenDependencies`** — if non-empty, any import matching a forbidden pattern is a violation.
3. **`exclusiveDependencies`** — if non-empty, any import that does **not** match an exclusive pattern is a violation.

## Example: Clean Architecture Enforcement

```json
{
  "packagePrefix": "com.example.app",
  "rules": [
    {
      "filePattern": "^.*/domain/.+\\.kt$",
      "exclusiveDependencies": [
        "^.*/domain/.+\\.kt$"
      ]
    },
    {
      "filePattern": "^.*/data/.+\\.kt$",
      "forbiddenDependencies": [
        "^.*/presentation/.+\\.kt$"
      ]
    },
    {
      "filePattern": "^.*/presentation/.+\\.kt$",
      "dependenciesAllowed": false
    }
  ]
}
```
