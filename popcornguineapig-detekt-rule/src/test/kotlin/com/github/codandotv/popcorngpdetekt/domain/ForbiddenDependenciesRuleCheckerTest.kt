package com.github.codandotv.popcorngpdetekt.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ForbiddenDependenciesRuleCheckerTest {
    @Test
    fun `given a file with a forbidden dep when the forbidden dependency rule is called then fails`() {
        val checker = ForbiddenDependenciesRuleChecker()

        val result = checker.check(
            notWith = listOf(".*\\.data\\..*"),
            deps = listOf("com.example.data.Repository")
        )

        assertNotNull(result)
    }

    @Test
    fun `given a file with multiple forbidden deps when the forbidden dependency rule is called then fails with all affected`() {
        val checker = ForbiddenDependenciesRuleChecker()

        val result = checker.check(
            notWith = listOf(".*\\.data\\..*", ".*\\.network\\..*"),
            deps = listOf(
                "com.example.data.Repo",
                "com.example.network.Api",
                "com.example.domain.UseCase"
            )
        )

        assertNotNull(result)
        assertEquals(2, result.affectedRelationship.size)
        assertEquals("com.example.data.Repo", result.affectedRelationship[0])
        assertEquals("com.example.network.Api", result.affectedRelationship[1])
    }

    @Test
    fun `given a file with no forbidden deps when the forbidden dependency rule is called then passes`() {
        val checker = ForbiddenDependenciesRuleChecker()

        val result = checker.check(
            notWith = listOf(".*\\.data\\..*"),
            deps = listOf("com.example.domain.UseCase")
        )

        assertNull(result)
    }

    @Test
    fun `given a file with empty deps when the forbidden dependency rule is called then passes`() {
        val checker = ForbiddenDependenciesRuleChecker()

        val result = checker.check(
            notWith = listOf(".*\\.data\\..*"),
            deps = emptyList()
        )

        assertNull(result)
    }
}
