package com.github.codandotv.popcorngpdetekt

import com.github.codandotv.popcorngpdetekt.data.PopcornGuineapigDetektRuleRepositoryImpl
import com.github.codandotv.popcorngpdetekt.domain.AnalyseArchitectureUseCase
import com.github.codandotv.popcorngpdetekt.domain.AnalyseArchitectureUseCaseImpl
import com.github.codandotv.popcorngpdetekt.domain.ExclusiveDependenciesRuleChecker
import com.github.codandotv.popcorngpdetekt.domain.ForbiddenDependenciesRuleChecker
import com.github.codandotv.popcorngpdetekt.domain.Logger
import com.github.codandotv.popcorngpdetekt.domain.LoggerImpl
import com.github.codandotv.popcorngpdetekt.domain.PopcornGuineapigDetektRuleRepository

internal object ServiceLocator {
    val forbiddenRuleChecker by lazy {
        ForbiddenDependenciesRuleChecker()
    }

    val exclusiveDependenciesRuleChecker by lazy {
        ExclusiveDependenciesRuleChecker()
    }

    val repository: PopcornGuineapigDetektRuleRepository by lazy {
        PopcornGuineapigDetektRuleRepositoryImpl()
    }

    val checker: AnalyseArchitectureUseCase by lazy {
        AnalyseArchitectureUseCaseImpl(
            forbiddenDependenciesRuleChecker = forbiddenRuleChecker,
            exclusiveDependenciesRuleChecker = exclusiveDependenciesRuleChecker,
        )
    }

    fun provideLogger(debugEnabled: Boolean): Logger {
        return LoggerImpl(
            isDebug = debugEnabled
        )
    }
}
