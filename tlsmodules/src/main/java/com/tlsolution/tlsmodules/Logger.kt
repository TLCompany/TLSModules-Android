package com.tlsolution.tlsmodules

import android.util.Log

class Logger {

    companion object {
        fun showError(tag: String, type: ErrorType, target: String) {
            Log.d(tag, type.message(target))
        }

        fun showDebuggingMessage(tag: String, message: String) {
            Log.d(tag, message)
        }
    }
}

enum class ErrorType {
    unsafelyWrapped, network, error;

    fun message(target: String): String {
        when (this) {
            unsafelyWrapped -> return "Not safe to unwrap $target"
            network -> return "Network failure: $target"
            error -> return "Just Error: $target"
        }
    }
}