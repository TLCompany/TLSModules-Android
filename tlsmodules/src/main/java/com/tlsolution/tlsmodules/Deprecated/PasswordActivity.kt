package com.tlsolution.tlsmodules.Deprecated

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tlsolution.tlsmodules.ErrorType
import com.tlsolution.tlsmodules.Extensions.showActionableConfirmAlert
import com.tlsolution.tlsmodules.Logger
import com.tlsolution.tlsmodules.R
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_password.*

/**
 * 비밀번호를 설정 또는 재설정하는 Activity
 */
@Deprecated(message = "View-related classes are no longer supoported.")
class PasswordActivity : AuthenticationActivity() {

    companion object {
        val TAG = "PasswordActivity"
    }
    private var goalType: PasswordGoalType = PasswordGoalType.register
    private var password: String? = null
    private lateinit var broadcaster: LocalBroadcastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        broadcaster = LocalBroadcastManager.getInstance(this)
        broadcaster.registerReceiver(passwordCompleteReceiver, IntentFilter(AuthManager.PASSWORD_GOAL_COMPLETE))
        val typeID = intent.getIntExtra(AuthManager.PASSWORD_GOAL_TYPE_DATA, 0)
        goalType = PasswordGoalType.create(typeID)
        setUpActionBar(passwordActionBar, goalType.title)
        setUpDescription(passwordDescription, goalType.title, getString(R.string.password_subtitle))
        warningTextView.visibility = View.INVISIBLE
        setUpEventListeners()
    }

    private val passwordCompleteReceiver = (object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            check(intent != null) {
                Logger.showError(TAG, ErrorType.error, "passwordCompleteReceiver")
                return
            }
            val isSuccessful = intent.getBooleanExtra(AuthManager.PASSWORD_GOAL_COMPLETE_DATA, false)
            val title = "${goalType.title} ${if (isSuccessful) "성공" else "실패"}"
            val message = "${if (isSuccessful) "${goalType.title}이 성공하였습니다" else "다시 시도해 주세요."}"
            showActionableConfirmAlert(title, message) {
                if (isSuccessful) {
                    finishAffinity()
                }
            }
        }
    })

    private fun setUpEventListeners() {
        pw1TextEdit.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                evaluateTextInputForPassword()
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })

        pw2TextEdit.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                evaluateTextInputForPassword()
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })

        completeButton.setOnClickListener {
            check(password != null) {
                Toast.makeText(this, getString(R.string.password_complete_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //유효한 비밀번호를 입력해서 broadcast receiver를 통해서 알려준다.
            val intent = Intent(AuthManager.PASSWORD_COMPLETE)
            intent.putExtra(AuthManager.PASSWORD_DATA, password!!)
            broadcaster.sendBroadcast(intent)
        }

    }

    private fun evaluateTextInputForPassword() {
        val pw1 = pw1TextEdit.text.toString()
        val pw2 = pw2TextEdit.text.toString()

        check(!pw1.isEmpty() && !pw2.isEmpty()) {
            password = null
            return
        }

        check(pw1 == pw2) {
            password = null
            warningTextView.setText(getString(R.string.password_dont_match))
            warningTextView.visibility = View.VISIBLE
            return
        }

        pw1.validator()
            .nonEmpty()
            .atleastOneNumber()
            .atleastOneLowerCase()
            .minLength(8)
            .addErrorCallback {
                warningTextView.setText(getString(R.string.password_invalid))
                warningTextView.visibility = View.VISIBLE
                password = null
            }
            .addSuccessCallback {
                warningTextView.visibility = View.INVISIBLE
                password = pw1
            }
            .check()
    }
}
