package com.tlsolution.tlsmodules.Extensions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.tlsolution.tlsmodules.Logger
import com.tlsolution.tlsmodules.Network.RequestEnvironmentMode
import com.tlsolution.tlsmodules.R
import java.util.*


/**
 * 액션 이벤트가 없는 한개의 버튼(예를 들어서 "확인")만 있는 경고 팝업 창
 */
fun Activity.showNoActionConfirmAlert(title: String,
                                      message: String,
                                      confirmBtnTitle: String? = getString(R.string.ok)) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(confirmBtnTitle) { _, _ -> }
    builder.create().show()
}

/**
 * 액션 이벤트가 있고 한개의 버튼만 있는 경고 팝업창
 */
fun Activity.showActionableConfirmAlert(title: String,
                                        message: String,
                                        confirmBtnTitle: String? = getString(R.string.ok),
                                        action: () -> Unit? = { }) {

    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(confirmBtnTitle) { _, _ ->  action() }
    builder.create().show()
}

/**
 * 긍정과 부정의 2개의 버튼을 가지고 있고, 각 이벤트를 가지고 있는 경고 팝업 창
 */
fun Activity.showTwoActionableAlert(title: String,
                                    message: String,
                                    positiveTitle: String,
                                    negativeTitle: String,
                                    positiveAction: () -> Unit? = { },
                                    negativeAction: () -> Unit? = { }) {

    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(positiveTitle) { _, _ -> positiveAction() }
    builder.setNegativeButton(negativeTitle) { _, _ -> negativeAction() }
    builder.create().show()
}

/**
 * 취소 + 2가지 기능을 가지고 있는 팝업창
 */
fun Activity.showThreeActionableAlert(title: String,
                                      message: String,
                                      cancelTitle: String,
                                      leftTitle: String,
                                      rightTitle: String,
                                      cancelAction:() -> Unit? = { },
                                      leftAction: () -> Unit? = { },
                                      rightAction: () -> Unit? = { }
) {

    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setNeutralButton(cancelTitle){ _, _ -> cancelAction() }
    builder.setNegativeButton(leftTitle) { _, _ -> leftAction() }
    builder.setPositiveButton(rightTitle) { _, _ -> rightAction() }

    builder.create().show()
}

fun Activity.checkContentString(content: String?,
                                emptyMessage: String?,
                                minimumLength: Int,
                                minimumMessage: String?,
                                maxLength:Int,
                                maxMessage:String?,
                                isForReplace:Boolean): String? {

    if (content!!.isBlank()) {
        runOnUiThread {
            Toast.makeText(this, emptyMessage, Toast.LENGTH_SHORT).show()
        }
        return null
    }

    if (content.length >= maxLength*2) {
        runOnUiThread {
            Toast.makeText(this, maxMessage, Toast.LENGTH_SHORT).show()
        }
        return null
    }

    if (content.length < minimumLength) {
        runOnUiThread {
            Toast.makeText(this, minimumMessage, Toast.LENGTH_SHORT).show()
        }
        return null
    }

    if (isForReplace) {
        var replacedContent = content.toString()
            .replace("\n", "\\n")
            .replace("\"", "\\\"")

        return replacedContent
    }

    return null
}




fun Activity.langCode(): Int {
    return if (Locale.getDefault().getLanguage() == "ko") 0 else 1
}

/**
 * 전화를 건다
 */
fun Activity.makePhoneCall(phoneNumber: String) {
    val permission = "android.permission.CALL_PHONE"
    val checkVal = baseContext.checkCallingOrSelfPermission(permission)

    if (checkVal == PackageManager.PERMISSION_GRANTED) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    } else {
        val message = if (langCode() == 1) "Please, enable phone-call permission in settings." else "세팅에서 전화걸기 권한을 켜주세요."
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Activity.popUpKeyboard(editText: EditText) {
    editText.requestFocus()
    val imm = this.baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.changeRequestEnvrionmentMode(completeButton: (RequestEnvironmentMode) -> Unit? = { }) {

    val builder = AlertDialog.Builder(this)
    builder.setTitle("서버 환경 설정")
    builder.setMessage("서버 환경을 선택해 주세요.")
    val input = EditText(this)
    input.inputType = InputType.TYPE_CLASS_NUMBER
    builder.setView(input)


    builder.setPositiveButton("O.K", { _, _ ->
        Logger.showDebuggingMessage("Request Envrionment has changed to Development", null)
        RequestEnvironmentMode.setCurrentMode(RequestEnvironmentMode.development, this)
        when (input.text.toString()) {
            "00000" -> completeButton(RequestEnvironmentMode.development)
            "11111" -> completeButton(RequestEnvironmentMode.test)
            "22222" -> completeButton(RequestEnvironmentMode.production)
        }
    })

    builder.setNegativeButton("취소", null)
    builder.create().show()
}