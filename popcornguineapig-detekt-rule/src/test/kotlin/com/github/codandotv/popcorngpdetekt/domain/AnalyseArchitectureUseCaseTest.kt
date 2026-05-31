package com.github.codandotv.popcorngpdetekt.domain

import com.github.codandotv.popcorngpdetekt.domain.models.DependencyRulesConfig
import com.github.codandotv.popcorngpdetekt.domain.models.RuleConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AnalyseArchitectureUseCaseTest {

    private val useCase = AnalyseArchitectureUseCaseImpl(
        forbiddenDependenciesRuleChecker = ForbiddenDependenciesRuleChecker(),
        exclusiveDependenciesRuleChecker = ExclusiveDependenciesRuleChecker(),
    )

    @Test
    fun `given no matching rule when checking then returns null`() {
        val config = DependencyRulesConfig(
            packagePrefix = "com.example",
            rules = listOf(RuleConfig(filePattern = ".*\\.domain\\..*"))
        )

        val result = useCase.check(
            targetFileName = "com.example.data.SomeFile",
            importsUsedByTheTarget = listOf("com.example.data.Repository"),
            dependencyRulesConfig = config
        )

        assertNull(result)
    }

    @Test
    fun `given matching rule with dependenciesAllowed false and non-empty imports then returns violation`() {
        val config = DependencyRulesConfig(
            packagePrefix = "com.example",
            rules = listOf(
                RuleConfig(
                    filePattern = ".*\\.data\\..*",
                    dependenciesAllowed = false
                )
            )
        )

        val result = useCase.check(
            targetFileName = "com.example.data.SomeFile",
            importsUsedByTheTarget = listOf("com.example.data.Repository"),
            dependencyRulesConfig = config
        )

        assertNotNull(result)
        assertEquals(
            "com.example.data.SomeFile should not have dependencies",
            result.message
        )
        assertEquals(
            listOf("com.example.data.Repository"),
            result.affectedRelationship
        )
    }

    @Test
    fun `given matching rule with dependenciesAllowed false and empty imports then returns null`() {
        val config = DependencyRulesConfig(
            packagePrefix = "com.example",
            rules = listOf(
                RuleConfig(
                    filePattern = ".*\\.domain\\..*",
                    dependenciesAllowed = false
                )
            )
        )

        val result = useCase.check(
            targetFileName = "com.example.domain.UseCase",
            importsUsedByTheTarget = emptyList(),
            dependencyRulesConfig = config
        )

        assertNull(result)
    }

    @Test
    fun `given matching rule with forbidden dependencies and a violation then returns error`() {
        val config = DependencyRulesConfig(
            packagePrefix = "com.example",
            rules = listOf(
                RuleConfig(
                    filePattern = ".*\\.domain\\..*",
                    forbiddenDependencies = listOf(".*\\.data\\..*")
                )
            )
        )

        val result = useCase.check(
            targetFileName = "com.example.domain.UseCase",
            importsUsedByTheTarget = listOf("com.example.data.Repository"),
            dependencyRulesConfig = config
        )

        assertNotNull(result)
        assertEquals(
            listOf("com.example.data.Repository"),
            result.affectedRelationship
        )
    }

    @Test
    fun `given matching rule with forbidden dependencies and no violation then returns null`() {
        val config = DependencyRulesConfig(
            packagePrefix = "com.example",
            rules = listOf(
                RuleConfig(
                    filePattern = ".*\\.domain\\..*",
                    forbiddenDependencies = listOf(".*\\.data\\..*")
                )
            )
        )

        val result = useCase.check(
            targetFileName = "com.example.domain.UseCase",
            importsUsedByTheTarget = listOf("com.example.domain.SomeClass"),
            dependencyRulesConfig = config
        )

        assertNull(result)
    }

    @Test
    fun `given matching rule with exclusive dependencies and a violation then returns error`() {
        val config = DependencyRulesConfig(
            packagePrefix = "com.example",
            rules = listOf(
                RuleConfig(
                    filePattern = ".*\\.domain\\..*",
                    exclusiveDependencies = listOf(".*\\.domain\\..*")
                )
            )
        )

        val result = useCase.check(
            targetFileName = "com.example.domain.UseCase",
            importsUsedByTheTarget = listOf("com.example.data.Repository"),
            dependencyRulesConfig = config
        )

        assertNotNull(result)
        assertEquals(
            listOf("com.example.data.Repository"),
            result.affectedRelationship
        )
    }

    @Test
    fun `given matching rule with exclusive dependencies and no violation then returns null`() {
        val config = DependencyRulesConfig(
            packagePrefix = "com.example",
            rules = listOf(
                RuleConfig(
                    filePattern = ".*\\.domain\\..*",
                    exclusiveDependencies = listOf(".*\\.domain\\..*")
                )
            )
        )

        val result = useCase.check(
            targetFileName = "com.example.domain.UseCase",
            importsUsedByTheTarget = listOf("com.example.domain.SomeClass"),
            dependencyRulesConfig = config
        )

        assertNull(result)
    }

    @Test
    fun `given matching rule with no conditions triggered then returns null`() {
        val config = DependencyRulesConfig(
            packagePrefix = "com.example",
            rules = listOf(
                RuleConfig(
                    filePattern = ".*\\.domain\\..*",
                )
            )
        )

        val result = useCase.check(
            targetFileName = "com.example.domain.UseCase",
            importsUsedByTheTarget = listOf("com.example.domain.SomeClass"),
            dependencyRulesConfig = config
        )

        assertNull(result)
    }
}
