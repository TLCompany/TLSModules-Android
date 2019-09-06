package com.tlsolution.tlsmodules.UserManager

import android.content.Context

open class UserManager(val context: Context) {

    open val PREFS_KEY = "SharedPreferences"
    open val USER_KEY = "USER_KEY"
    open val sharedPref = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    companion object {
        val TAG = "UserManager"
    }

    open var user: User? = null

    /**
     * 사용자 데이터에 새로운 토큰을 넣어줍니다.
     */
    open fun setNewToken(newToken: String?) {
        val user = this.user
        user?.token = newToken
        this.user = user
    }
}
