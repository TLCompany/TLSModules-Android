package com.tlsolution.tlsmodules.Network

import android.content.Context

enum class RequestEnvironmentMode {
    production, test, development;

    companion object {

        val Environment_MODE_KEY = "tlsmodules_environment_mode"
        val CURRENT_MODE_KEY = "tlsmodules_current_mode"

        fun getCurrentMode(context: Context): RequestEnvironmentMode? {
            val identifier = context.getSharedPreferences(Environment_MODE_KEY, Context.MODE_PRIVATE).getString(CURRENT_MODE_KEY, null)
            if (identifier == null) {
                return null
            }

            when (identifier) {
                "tls_module_request_environment_production" -> return production
                "tls_module_request_environment_test" -> return test
                "tls_module_request_environment_development" -> return development
                else -> return null
            }
        }

        fun setCurrentMode(mode: RequestEnvironmentMode, context: Context) {
            val editor = context.getSharedPreferences(Environment_MODE_KEY, Context.MODE_PRIVATE).edit()
            editor.putString(CURRENT_MODE_KEY, mode.identifier)
            editor.commit()
        }
    }

    var identifier: String? = null
        get() {
            when (this) {
                production -> return "tls_module_request_environment_production"
                test -> return "tls_module_request_environment_test"
                development -> return "tls_module_request_environment_development"
            }
        }
}

