package com.github.codandotv.popcorngpdetekt.domain.models

internal data class ArchitectureViolationError(
    val message: String,
    val affectedRelationship: List<String>,
)