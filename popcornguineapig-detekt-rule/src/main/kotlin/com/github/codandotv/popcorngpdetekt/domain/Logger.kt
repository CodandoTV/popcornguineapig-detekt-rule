package com.github.codandotv.popcorngpdetekt.domain

internal interface Logger {
    fun logIfDebug(tag: String, message: String)
}

internal class LoggerImpl(
    private val isDebug: Boolean,
) : Logger {
    override fun logIfDebug(tag: String, message: String) {
        if (isDebug) {
            println("[POPCORNGP_DETEKT_RULE] [$tag]: $message")
        }
    }
}
