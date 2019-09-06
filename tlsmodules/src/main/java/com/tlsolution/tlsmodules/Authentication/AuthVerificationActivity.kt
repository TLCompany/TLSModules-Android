package com.tlsolution.tlsmodules.Authentication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tlsolution.tlsmodules.ErrorType
import com.tlsolution.tlsmodules.Extensions.popUpKeyboard
import com.tlsolution.tlsmodules.Extensions.showActionableConfirmAlert
import com.tlsolution.tlsmodules.Logger
import com.tlsolution.tlsmodules.R
import kotlinx.android.synthetic.main.activity_auth_verification.*

/**
 * 회원가입 또는 비밀번호 찾기등에서 필요한 인증을 하는 Activity.
 */
class AuthVerificationActivity : AuthenticationActivity() {

    companion object {
        val TAG = "AuthVerificationActivity"
    }

    private lateinit var verificationType: AuthVerificationType
    private lateinit var broadcaster: LocalBroadcastManager
    private var isVCodeSent = false
    private var isVCodeCorrect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_verification)

        setUpActionBar(authVerificationActionBar, "회원가입")
        broadcaster = LocalBroadcastManager.getInstance(this)
        displayData()
        setUpClickListeners()
        receiveResults()
    }

    override fun onResume() {
        super.onResume()

        targetTextEdit.setText(null)
        vcodeTextEdit.setText(null)
        isVCodeCorrect = false
        isVCodeSent = false
        popUpKeyboard(targetTextEdit)
    }

    private fun displayData() {
        verificationType = AuthVerificationType.create(intent.getIntExtra(AuthManager.VERIFICATION_TYPE, 0))
        setUpDescription(authVerificationDescription, verificationType?.title, verificationType?.subtitle)
        targetTextEdit.setHint(verificationType.hint)
        vcodeTextEdit.setHint(getString(R.string.auth_vcode_hint))
        targetTextEdit.inputType = verificationType.keyboardType
        vcodeTextEdit.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun setUpClickListeners() {
        sendButton.setOnClickListener {
            val target = targetTextEdit.text.toString()

            check(!target.isEmpty()) {
                Toast.makeText(this, verificationType.noTargetMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            check(verificationType == AuthVerificationType.email && AuthManager.validateEmail(target)) {
                Toast.makeText(this, getString(R.string.auth_verification_invalid_email), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //이메일 또는 휴대폰 번호를 유효한 값으로 입력했을 때, broadcast receiver를 통해서 알려준다.
            val intent = Intent(AuthManager.VCODE_TO_SEND)
            intent.putExtra(AuthManager.VERIFICATION_TYPE, verificationType.id)
            intent.putExtra(AuthManager.TARGET_DATA, target)
            broadcaster.sendBroadcast(intent)
        }

        verifyButton.setOnClickListener {
            val vcode = vcodeTextEdit.text.toString()

            check(isVCodeSent) {
                Toast.makeText(this, getString(R.string.auth_verification_no_vcode), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            check(!vcode.isEmpty()) {
                Toast.makeText(this, getString(R.string.auth_verification_vcode_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //사용자가 인증번호를 입력했을 때, broadcast receiver를 통해서 알려준다.
            val intent = Intent(AuthManager.CHECK_VCODE)
            intent.putExtra(AuthManager.CHECK_VCODE_DATA, vcode)
            broadcaster.sendBroadcast(intent)
        }

        targetTextEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isVCodeSent = false
                isVCodeCorrect = false
                vcodeTextEdit.setText("")
            }
        })
    }

    private fun receiveResults() {
        broadcaster.registerReceiver(vcodeResultReceiver, IntentFilter(AuthManager.VCODE_SENT))
    }

    private val vcodeResultReceiver = (object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            check(intent != null) {
                Logger.showError(TAG, ErrorType.error, "vcodeResultReceiver")
                return
            }
            val isSuccessful = intent.getBooleanExtra(AuthManager.VCODE_SENT_DATA, false)
            isVCodeSent = isSuccessful
            if (isSuccessful) {
                showActionableConfirmAlert("", getString(R.string.auth_verification_vcode_sent)) {
                    popUpKeyboard(vcodeTextEdit)
                }
            } else {
                Toast.makeText(this@AuthVerificationActivity, "Something went wrong while sending a verification code to the client", Toast.LENGTH_SHORT).show()
            }
        }
    })
}
