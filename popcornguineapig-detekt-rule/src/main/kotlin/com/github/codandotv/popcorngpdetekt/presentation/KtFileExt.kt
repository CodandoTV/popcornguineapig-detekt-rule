package com.github.codandotv.popcorngpdetekt.presentation

import org.jetbrains.kotlin.psi.KtFile
internal fun KtFile.fullFileName(): String {
    return StringBuilder().apply {
        val packageFqName = packageFqName.asString()
        if(packageFqName.isNotEmpty()) {
            append(packageFqName)
            append(".")
        }
        append(name)
    }.toString()
}

internal fun KtFile.isInternalFile(packagePrefix: String): Boolean {
    val fullFileName = fullFileName()
    return fullFileName.contains(packagePrefix)
}

internal fun KtFile.imports(packagePrefix: String): List<String> {
    return importDirectives.mapNotNull {
        val pathStr = it.importPath?.pathStr
        if (pathStr?.contains(packagePrefix) == true) {
            pathStr
        } else {
            null
        }
    }
}
