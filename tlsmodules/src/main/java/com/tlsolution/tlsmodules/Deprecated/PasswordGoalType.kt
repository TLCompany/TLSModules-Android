package com.tlsolution.tlsmodules.Deprecated

/**
 * 비밀번호를 설정하는 목표(회원가입 또는 재설정)
 */
@Deprecated(message = "View-related classes are no longer supoported.")
enum class PasswordGoalType {
    register, reset;

    companion object {
        fun create(id: Int): PasswordGoalType {
            when (id) {
                0 -> return register
                else -> return reset
            }
        }
    }

    var id: Int = 0
        get() {
            when (this) {
                register -> return 0
                reset -> return 1
            }
        }

    var title: String = ""
        get() {
            when (this) {
                register -> return "회원가입"
                reset -> return "비밀번호 재설정"
            }
        }
}