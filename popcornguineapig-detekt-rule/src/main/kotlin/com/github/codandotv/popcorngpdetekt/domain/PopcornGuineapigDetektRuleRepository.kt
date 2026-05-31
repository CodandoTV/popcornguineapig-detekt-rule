package com.github.codandotv.popcorngpdetekt.domain

import com.github.codandotv.popcorngpdetekt.domain.models.DependencyRulesConfig

internal interface PopcornGuineapigDetektRuleRepository {
    fun load(filePath: String): DependencyRulesConfig
}
