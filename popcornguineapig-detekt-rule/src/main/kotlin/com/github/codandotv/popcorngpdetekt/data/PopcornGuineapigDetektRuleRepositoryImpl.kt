package com.github.codandotv.popcorngpdetekt.data

import com.github.codandotv.popcorngpdetekt.domain.PopcornGuineapigDetektRuleRepository
import com.github.codandotv.popcorngpdetekt.domain.models.DependencyRulesConfig
import kotlinx.serialization.json.Json
import java.io.File

internal class PopcornGuineapigDetektRuleRepositoryImpl : PopcornGuineapigDetektRuleRepository {
    private val json = Json { prettyPrint = true }

    override fun load(filePath: String): DependencyRulesConfig {
        val configFile = File(filePath)
        if (configFile.exists()) {
            val dependencyRulesConfig = json.decodeFromString<DependencyRulesConfig>(
                configFile.readText()
            )
            return dependencyRulesConfig
        } else {
            error("Something went wrong. Popcornguineapig configuration file was not found")
        }
    }
}
