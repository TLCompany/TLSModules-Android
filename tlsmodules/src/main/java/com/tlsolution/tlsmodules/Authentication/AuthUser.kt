package com.tlsolution.tlsmodules.Authentication

/**
 * 회원가입에서 필요한 기본적인 유저 정보를 담고 있다.
 */
open class AuthUser {
    val email: String?
    val mobile: String?
    val password: String

    constructor(email: String?, mobile: String?, password: String) {
        this.email = email
        this.mobile = mobile
        this.password = password
    }
}