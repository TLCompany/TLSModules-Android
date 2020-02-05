package com.tlsolution.tlsmodules.Deprecated


import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Patterns
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tlsolution.tlsmodules.ErrorType
import com.tlsolution.tlsmodules.Logger
import com.tlsolution.tlsmodules.Models.Policy
import com.tlsolution.tlsmodules.R

@Deprecated(message = "View-related classes are no longer supoported.")
open class AuthManager<T>(val rootActivity: Activity) {

    internal var isAllSet: Boolean = false
    internal var broadcaster = LocalBroadcastManager.getInstance(rootActivity)

    internal var verificationType: AuthVerificationType? = null
    internal var target: String? = null

    var completAction: (T) -> Unit = { }

    companion object {
        val TAG = "AuthManager"
        val POLICY_DATA = "POLICY_DATA"
        val POLICY_COMPLETED = "POLICY_COMPLETED"
        val VERIFICATION_TYPE = "VERIFICATION_TYPE"
        val VCODE_TO_SEND = "VCODE_TO_SEND"
        val VCODE_SENT = "VCODE_SENT"
        val VCODE_SENT_DATA = "VCODE_SENT_DATA"
        val CHECK_VCODE = "CHECK_VCODE"
        val CHECK_VCODE_DATA = "CHECK_VCODE_DATA"
        val TARGET_DATA = "TARGET_DATA"
        val PASSWORD_GOAL_TYPE_DATA = "PASSWORD_GOAL_TYPE_DATA"
        val PASSWORD_COMPLETE = "PASSWORD_COMPLETE"
        val PASSWORD_DATA = "PASSWORD_DATA"
        val PASSWORD_GOAL_COMPLETE = "PASSWORD_GOAL_COMPLETE"
        val PASSWORD_GOAL_COMPLETE_DATA = "PASSWORD_GOAL_COMPLETE_DATA"
        fun validateEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    open fun setUpBroadcastLiensteners() { }
    open fun startRegistration(policies: ArrayList<Policy>) {
        check(!isAllSet) {
            Logger.showError(TAG, ErrorType.error, "Broadcast listeners are not set")
            return
        }
    }
    open fun findMyPassword() {
        check(!isAllSet) {
            Logger.showError(TAG, ErrorType.error, "Broadcast listeners are not set")
            return
        }
    }

    /**
     * 정책사항의 동의가 완료된 후 다음을 눌렀을 때의 이벤트를 담당하는 BroadcastListener를 등록(register)한다
     */
    internal fun setAgreementReceiver() {
        broadcaster.registerReceiver(agreementReceiver, IntentFilter(POLICY_COMPLETED))
    }

    /**
     * 정책사항의 동의가 완료 된 후의 이벤트를 실행하는 함수
     */
    open fun handleAgreementCompletion() {

    }

    private val agreementReceiver = (object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            this@AuthManager.handleAgreementCompletion()
        }
    })

    /**
     * 인증번호 보내기의 이벤트 신호를 화면(AuthVerificationActivity)에서부터 받아오기 위한 receiver를 등록한다.
     */
    internal fun setVCodeToSendReceiver() {
        broadcaster.registerReceiver(vcodeToSendReceiver, IntentFilter(VCODE_TO_SEND))
    }

    /**
     * 인증번호 보내기를 누른 다음의 이벤트를 앱 프로젝트에서 호출하는 함수
     */
    open fun handleVCodeToSendCompletion(verificationType: AuthVerificationType?, target: String) {
        Logger.showDebuggingMessage(TAG, verificationType.toString())
    }

    private val vcodeToSendReceiver = (object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            check(intent != null) {
                Logger.showError(TAG, ErrorType.error, "vcodeToSendReceiver")
                return
            }
            val typeInt = intent?.getIntExtra(AuthManager.VERIFICATION_TYPE, 0)
            val target = intent.getStringExtra(TARGET_DATA)
            this@AuthManager.target = target
            val verificationType = AuthVerificationType.create(typeInt)
            this@AuthManager.verificationType = verificationType
            handleVCodeToSendCompletion(verificationType, target)
        }
    })

    /**
     * 인증번호를 서버에서 보낸 이벤트가 성공인지 아닌지를 다시 AuthVerificationActivity에 보내주는 함수
     */
    internal fun sendResultOfSendingVCode(isSuccssful: Boolean) {
        val intent = Intent(VCODE_SENT)
        intent.putExtra(VCODE_SENT_DATA, isSuccssful)
        broadcaster.sendBroadcast(intent)
    }

    internal fun setCheckVCodeReceiver() {
        broadcaster.registerReceiver(checkVCodeReceiver, IntentFilter(CHECK_VCODE))
    }

    private val checkVCodeReceiver = (object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            check(intent != null) {
                Logger.showError(TAG, ErrorType.error, "vcodeToSendReceiver")
                return
            }
            val vcode = intent.getStringExtra(CHECK_VCODE_DATA)
            Logger.showDebuggingMessage(TAG, vcode)
            handleCheckVCode(vcode)
        }
    })

    open fun handleCheckVCode(vcode: String) {
        Logger.showDebuggingMessage(TAG, vcode)
    }

    internal fun handleCompleteVerification(isVCodeCorrect: Boolean, completeAction: () -> Unit = {}) {
        check(isVCodeCorrect) {
            Toast.makeText(rootActivity.baseContext, rootActivity.getString(R.string.auth_vcode_incorrect), Toast.LENGTH_SHORT).show()
            return
        }
        completeAction()
    }

    fun setPasswordCompleteReceiver() {
        broadcaster.registerReceiver(passwordCompleteReceiver, IntentFilter(PASSWORD_COMPLETE))
    }

    private val passwordCompleteReceiver = (object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            check(intent != null) {
                Logger.showError(TAG, ErrorType.error, "passwordCompleteReceiver")
                return
            }
            val password = intent.getStringExtra(PASSWORD_DATA)
            handlePasswordComplete(password)
        }
    })

    open fun handlePasswordComplete(password: String) {

    }

    internal fun sendResultOfPasswordGoalCompletion(isSuccessful: Boolean) {
        val intent = Intent(PASSWORD_GOAL_COMPLETE)
        intent.putExtra(PASSWORD_GOAL_COMPLETE_DATA, isSuccessful)
        broadcaster.sendBroadcast(intent)
    }


    /**
     * 모든 Broadcaster에 register되어 있던 receiver들을 제거한다.
     */
    open fun removeAllReceivers() {
        broadcaster.unregisterReceiver(agreementReceiver)
        broadcaster.unregisterReceiver(vcodeToSendReceiver)
    }

}