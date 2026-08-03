package com.github.codandotv.popcorngpdetekt.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ExclusiveDependenciesRuleCheckerTest {

    private val checker = ExclusiveDependenciesRuleChecker()

    @Test
    fun `given all deps match the exclusive pattern then passes`() {
        val result = checker.check(
            justWith = listOf(".*\\.domain\\..*"),
            deps = listOf("com.example.domain.UseCase")
        )

        assertNull(result)
    }

    @Test
    fun `given a dep does not match the exclusive pattern then fails`() {
        val result = checker.check(
            justWith = listOf(".*\\.domain\\..*"),
            deps = listOf("com.example.data.Repository")
        )

        assertNotNull(result)
        assertEquals(
            "This file should only depend on [.*\\.domain\\..*]",
            result.message
        )
        assertEquals(
            listOf("com.example.data.Repository"),
            result.affectedRelationship
        )
    }

    @Test
    fun `given multiple deps with some failing the exclusive pattern then fails with only non-matching deps`() {
        val result = checker.check(
            justWith = listOf(".*\\.domain\\..*"),
            deps = listOf(
                "com.example.domain.UseCase",
                "com.example.data.Repository",
                "com.example.network.Api"
            )
        )

        assertNotNull(result)
        assertEquals(
            "This file should only depend on [.*\\.domain\\..*]",
            result.message
        )
        assertEquals(2, result.affectedRelationship.size)
        assertEquals("com.example.data.Repository", result.affectedRelationship[0])
        assertEquals("com.example.network.Api", result.affectedRelationship[1])
    }

    @Test
    fun `given empty deps when exclusive rule is called then passes`() {
        val result = checker.check(
            justWith = listOf(".*\\.domain\\..*"),
            deps = emptyList()
        )

        assertNull(result)
    }
}
