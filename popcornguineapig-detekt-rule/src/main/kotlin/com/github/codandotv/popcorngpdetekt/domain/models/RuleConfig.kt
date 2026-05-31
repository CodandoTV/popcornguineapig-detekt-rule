package com.github.codandotv.popcorngpdetekt.domain.models

import kotlinx.serialization.Serializable

@Serializable
public data class RuleConfig(
    val filePattern: String,
    val dependenciesAllowed: Boolean? = null,
    val forbiddenDependencies: List<String> = emptyList(),
    val exclusiveDependencies: List<String> = emptyList(),
)