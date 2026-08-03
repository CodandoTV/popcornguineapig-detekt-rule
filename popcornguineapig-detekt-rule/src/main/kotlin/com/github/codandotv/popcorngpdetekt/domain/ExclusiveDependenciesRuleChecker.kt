package com.github.codandotv.popcorngpdetekt.domain

import com.github.codandotv.popcorngpdetekt.domain.models.ArchitectureViolationError

internal class ExclusiveDependenciesRuleChecker {
    internal fun check(justWith: List<String>, deps: List<String>): ArchitectureViolationError? {
        val affectedRelationship = mutableListOf<String>()
        justWith.forEach { justWithDep ->
            deps.forEach { dep ->
                if (justWithDep.toRegex().matches(dep).not()) {
                    affectedRelationship.add(dep)
                }
            }
        }
        if (affectedRelationship.isNotEmpty()) {
            return ArchitectureViolationError(
                message = "This file should only depend on $justWith",
                affectedRelationship = affectedRelationship,
            )
        } else {
            return null
        }
    }
}
