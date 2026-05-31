package com.github.codandotv.popcorngpdetekt.presentation.rules

import com.github.codandotv.popcorngpdetekt.ServiceLocator
import com.github.codandotv.popcorngpdetekt.domain.Logger
import com.github.codandotv.popcorngpdetekt.domain.models.DependencyRulesConfig
import com.github.codandotv.popcorngpdetekt.presentation.fullFileName
import com.github.codandotv.popcorngpdetekt.presentation.imports
import com.github.codandotv.popcorngpdetekt.presentation.isInternalFile
import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtFile

public class ArchitectureRulesRule(
    private val dependencyRulesConfig: DependencyRulesConfig,
) : Rule() {
    override val issue: Issue = Issue(
        id = KEY,
        description = "Define dependency rules for you project",
        severity = Severity.CodeSmell,
        debt = Debt(5)
    )
    private val checker = ServiceLocator.checker

    private val logger: Logger by lazy {
        ServiceLocator.provideLogger(dependencyRulesConfig.debug)
    }

    override fun visit(root: KtFile) {
        super.visit(root)
        val entity = Entity.from(root)

        logger.logIfDebug(tag = KEY, "Visiting $root")

        dependencyRulesConfig.rules.forEach { targetRegex ->
            logger.logIfDebug(tag = KEY, "targetRegex-->$targetRegex")

            val isInternalFile = root.isInternalFile(
                packagePrefix = dependencyRulesConfig.packagePrefix,
            )
            val fullFileName = root.fullFileName()
            val isTargetFile = isInternalFile && fullFileName.matches(
                targetRegex.filePattern.toRegex()
            )
            logger.logIfDebug(
                tag = KEY,
                "isTargetFile-->$isTargetFile"
            )

            if (isTargetFile) {
                val imports = root.imports(packagePrefix = dependencyRulesConfig.packagePrefix)
                logger.logIfDebug(
                    tag = KEY,
                    "imports-->${imports.size}"
                )
                val violation = checker.check(
                    targetFileName = fullFileName,
                    dependencyRulesConfig = dependencyRulesConfig,
                    importsUsedByTheTarget = imports
                )
                if (violation != null) {
                    val errorMessage = """        
Current file:
${root.packageFqName}.${root.name}

${violation.message}        
""".trimIndent()

                    report(
                        CodeSmell(
                            issue = issue,
                            entity = entity,
                            message = errorMessage,
                        )
                    )
                }
            }
        }
    }

    internal companion object {
        internal const val KEY = "ArchitectureRulesRule"
    }
}
