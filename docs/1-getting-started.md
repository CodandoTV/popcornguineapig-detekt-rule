# Getting started 🚀

## 1. Define rules

Create your configuration file at the same level of your `detekt.yml` definition (e.g.,
`config/detekt/popcorngp-config.json`).

```json
{
  "packagePrefix": "com.gabrielbmoro.moviedb",
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

In this first rule, I am defining that any file located under the `domain/model` package may only
depend on other classes within the same `domain/model` package. This ensures that domain models
remain isolated and do not introduce dependencies on other layers or modules of the application.

In the second rule, I am telling Popcorngp that any class whose name ends with `*Controller.kt` must
not
have dependencies on other internal project classes. This helps ensure that controllers remain
isolated and do not become tightly coupled to other parts of the codebase.

## 2. Installing Popcorngp in your project

This custom Detekt plugin can be used in two different ways, depending on how you run Detekt in your
project.

### Option 1: Gradle integration

If your project uses the Detekt Gradle plugin, you can add this plugin as a `detektPlugins`
dependency. Once configured, the custom rules will automatically be loaded and executed whenever
Detekt runs as part of your Gradle build.

```kotlin
plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    buildUponDefaultConfig = true

    config.setFrom(
        "$rootDir/config/detekt/detekt.yml"
    )
}

dependencies {
    detektPlugins("io.github.codandotv:popcornguineapig-detekt-rule:<version>)
}
```

This approach is recommended for most Kotlin projects because it integrates seamlessly with your
existing build process and CI/CD pipelines.

### Option 2: Detekt CLI

If you use Detekt through the command-line interface, you can load the plugin by providing the
generated JAR file using the --plugins option.

This approach is useful when running Detekt outside of Gradle or when integrating Detekt into custom
tooling and scripts.

Example:

```shell
detekt \
  --input src \
  --config detekt.yml \
  --plugins popcornguineapig-detekt-rule.jar
```

In both cases, the plugin will discover and execute your custom architectural rules, reporting any
violations according to the configuration defined in your Detekt settings.

--

Any problems you are facing, any suggestions you want to add, please feel free
to [reach us out](mailto:gabrielbronzattimoro.es@gmail.com).