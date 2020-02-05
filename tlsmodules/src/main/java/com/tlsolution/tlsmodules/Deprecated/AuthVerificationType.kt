package com.tlsolution.tlsmodules.Deprecated

import android.text.InputType

/**
 * 인증방법의 타입(이메일 또는 휴대폰 번호)
 */
@Deprecated(message = "this class is no longer supoported.")
enum class AuthVerificationType {
    email, mobile;

    companion object {
        fun create(id: Int): AuthVerificationType {
            when (id) {
                0 -> return email
                else -> return mobile
            }
        }
    }

    var keyboardType: Int = InputType.TYPE_CLASS_TEXT
        get() {
            when (this) {
                email -> return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                mobile -> return InputType.TYPE_CLASS_PHONE
            }
        }


    var id: Int = 0
        get() {
            when (this) {
                email -> return 0
                mobile -> return 1
            }
        }


    var title: String = ""
        get() {
            when (this) {
                email -> return "이메일"
                mobile -> return "휴대폰 번호"
            }
        }


    var subtitle: String = ""
        get() {
            when (this) {
                email ->  return "인증 된 이메일은 고객님의 아이디로 사용됩니다."
                mobile -> return "회원가입을 위해서 휴대폰 번호를 인증해 주세요."
            }
        }

    var hint: String = ""
        get() {
            when (this) {
                email -> return "ex) myemail@gmail.com"
                mobile -> return  "01012345678"
            }
        }

    var noTargetMessage: String = ""
        get() {
            when (this) {
                email -> return "이메일을 입력해 주세요."
                mobile -> return "휴대폰 번호를 입력해 주세요."
            }
        }

    var preposition: String = ""
        get() {
            when (this) {
                email -> return "을"
                mobile -> return "를"
            }
        }
}