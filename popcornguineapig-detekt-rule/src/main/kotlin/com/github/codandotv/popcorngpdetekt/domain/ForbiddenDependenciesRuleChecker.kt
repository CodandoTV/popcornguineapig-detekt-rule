package com.github.codandotv.popcorngpdetekt.domain

import com.github.codandotv.popcorngpdetekt.domain.models.ArchitectureViolationError

internal class ForbiddenDependenciesRuleChecker {

    fun check(notWith: List<String>, deps: List<String>): ArchitectureViolationError? {
        val affectedRelationship = mutableListOf<String>()
        notWith.forEach { notWithDep ->
            deps.forEach { dep ->
                if (notWithDep.toRegex().matches(dep)) {
                    affectedRelationship.add(dep)
                }
            }
        }
        if (affectedRelationship.isNotEmpty()) {
            return ArchitectureViolationError(
                message = "This file should not depends on $notWith",
                affectedRelationship = affectedRelationship,
            )
        } else {
            return null
        }
    }
}

