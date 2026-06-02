import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
}

repositories {
    mavenCentral()
}

val versionPropertiesFile = file("../popcornguineapig-detekt-rule/version.properties")
val versionProperties = Properties().apply {
    load(versionPropertiesFile.inputStream())
}

val versionPublish = versionProperties.getProperty("VERSION")

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        project.property("GROUP_ID") as String,
        project.property("ARTIFACT_ID") as String,
        versionPublish
    )

    pom {
        name.set(project.property("ARTIFACT_ID") as String)
        description.set(project.property("ARTIFACT_ID") as String)
        inceptionYear.set("2024")
        url.set(project.property("POM_URL") as String)

        licenses {
            license {
                name.set(project.property("POM_LICENSE_NAME") as String)
                url.set(project.property("POM_LICENSE_URL") as String)
            }
        }
        scm {
            connection.set("scm:git@github.com:CodandoTV/popcorn-guineapig.git")
            url.set("https://github.com/CodandoTV/popcorn-guineapig.git")
        }
        developers {
            developer {
                id.set(project.property("POM_DEVELOPER_ID") as String)
                name.set(project.property("POM_DEVELOPER_NAME") as String)
                email.set(project.property("POM_DEVELOPER_EMAIL") as String)
            }
        }
    }
}

kotlin {
    explicitApi()
}

dependencies {
    compileOnly(libs.detekt.api)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.detekt.test)
    testImplementation(libs.kotlin.test)
}

tasks.koverHtmlReport {
    dependsOn("test") // Ensure tests are run before generating the report
}