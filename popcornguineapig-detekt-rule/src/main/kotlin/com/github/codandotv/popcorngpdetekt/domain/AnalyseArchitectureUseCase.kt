package com.github.codandotv.popcorngpdetekt.domain

import com.github.codandotv.popcorngpdetekt.domain.models.ArchitectureViolationError
import com.github.codandotv.popcorngpdetekt.domain.models.DependencyRulesConfig

internal interface AnalyseArchitectureUseCase {
    fun check(
        targetFileName: String,
        importsUsedByTheTarget: List<String>,
        dependencyRulesConfig: DependencyRulesConfig
    ): ArchitectureViolationError?
}

internal class AnalyseArchitectureUseCaseImpl(
    private val forbiddenDependenciesRuleChecker: ForbiddenDependenciesRuleChecker,
    private val exclusiveDependenciesRuleChecker: ExclusiveDependenciesRuleChecker,
) : AnalyseArchitectureUseCase {
    override fun check(
        targetFileName: String,
        importsUsedByTheTarget: List<String>,
        dependencyRulesConfig: DependencyRulesConfig
    ): ArchitectureViolationError? {
        dependencyRulesConfig.rules.firstOrNull {
            targetFileName.matches(it.filePattern.toRegex())
        }?.let { rule ->
            return when {
                rule.dependenciesAllowed == false -> {
                    if (importsUsedByTheTarget.isNotEmpty()) {
                        ArchitectureViolationError(
                            message = "$targetFileName should not have dependencies",
                            affectedRelationship = importsUsedByTheTarget
                        )
                    } else {
                        null
                    }
                }

                rule.forbiddenDependencies.isNotEmpty() -> {
                    forbiddenDependenciesRuleChecker.check(
                        notWith = rule.forbiddenDependencies,
                        deps = importsUsedByTheTarget,
                    )
                }

                rule.exclusiveDependencies.isNotEmpty() -> {
                    exclusiveDependenciesRuleChecker.check(
                        justWith = rule.exclusiveDependencies,
                        deps = importsUsedByTheTarget,
                    )
                }

                else -> null
            }
        }

        return null
    }

}