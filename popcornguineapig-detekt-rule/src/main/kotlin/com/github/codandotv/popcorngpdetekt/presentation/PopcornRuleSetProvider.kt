package com.github.codandotv.popcorngpdetekt.presentation

import com.github.codandotv.popcorngpdetekt.ServiceLocator
import com.github.codandotv.popcorngpdetekt.presentation.rules.ArchitectureRulesRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

public class PopcornRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "popcorn_guineapig_rules"
    private val repository = ServiceLocator.repository


    private val configurationKey = "config"
    override fun instance(config: Config): RuleSet {
        val configurationFilePath = config.valueOrNull<String>(configurationKey)

        if (configurationFilePath.isNullOrBlank()) {
            val errorMessage = """
Missing required configuration file.

Example:

$ruleSetId:
    $configurationKey: "config/detekt/popcorngp-config.json"
""".trimIndent()
            error(errorMessage)
        } else {
            val dependencyRulesConfig = repository.load(configurationFilePath)
            return RuleSet(
                id = ruleSetId,
                rules = listOf(
                    ArchitectureRulesRule(
                        dependencyRulesConfig
                    ),
                )
            )
        }
    }
}
