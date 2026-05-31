package com.github.codandotv.popcorngpdetekt.domain.models

import kotlinx.serialization.Serializable

@Serializable
public data class DependencyRulesConfig(
    val packagePrefix: String,
    val rules: List<RuleConfig>,
    val debug: Boolean = false,
)
