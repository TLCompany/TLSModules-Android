package com.tlsolution.tlsmodules.Extensions


import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tlsolution.tlsmodules.R
import java.util.*


/**
 * 액션 이벤트가 없는 한개의 버튼(예를 들어서 "확인")만 있는 경고 팝업 창
 */
fun Fragment.showNoActionConfirmAlert(title: String,
                                      message: String,
                                      confirmBtnTitle: String? = getString(R.string.ok)) {
    val builder = AlertDialog.Builder(this.context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(confirmBtnTitle) { _, _ ->

    }

    builder.create().show()
}

/**
 * 액션 이벤트가 있고 한개의 버튼만 있는 경고 팝업창
 */
fun Fragment.showActionableConfirmAlert(title: String,
                                        message: String,
                                        confirmBtnTitle: String? = getString(R.string.ok),
                                        action: () -> Unit? = { }) {

    val builder = AlertDialog.Builder(this.context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(confirmBtnTitle) { _, _ ->

        action()
    }

    builder.create().show()
}

/**
 * 긍정과 부정의 2개의 버튼을 가지고 있고, 각 이벤트를 가지고 있는 경고 팝업 창
 */
fun Fragment.showTwoActionableAlert(title: String,
                                    message: String,
                                    positiveTitle: String,
                                    negativeTitle: String,
                                    positiveAction: () -> Unit? = { },
                                    negativeAction: () -> Unit? = { }) {

    val builder = AlertDialog.Builder(this.context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(positiveTitle) { _, _ ->
        positiveAction()
    }

    builder.setNegativeButton(negativeTitle) { _, _ ->
        negativeAction()
    }

    builder.create().show()
}


/**
 * 취소 + 2가지 기능을 가지고 있는 팝업창
 */
fun Fragment.showThreeActionableAlert(title: String,
                                      message: String,
                                      cancelTitle: String,
                                      leftTitle: String,
                                      rightTitle: String,
                                      cancelAction:() -> Unit? = { },
                                      leftAction: () -> Unit? = { },
                                      rightAction: () -> Unit? = { }
) {

    val builder = AlertDialog.Builder(this.context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setNeutralButton(cancelTitle){ _, _ ->
        cancelAction()
    }
    builder.setNegativeButton(leftTitle) { _, _ ->
        leftAction()
    }

    builder.setPositiveButton(rightTitle) { _, _ ->
        rightAction()
    }




    builder.create().show()
}







fun Fragment.langCode(): Int {
    return if (Locale.getDefault().getLanguage() == "ko") 0 else 1
}

///**
// * 전화를 건다
// */
//fun Fragment.makePhoneCall(phoneNumber: String) {
//    val permission = "android.permission.CALL_PHONE"
//    val checkVal = Fragment.baseContext.checkCallingOrSelfPermission(permission)
//
//    if (checkVal == PackageManager.PERMISSION_GRANTED) {
//        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
//        startActivity(intent)
//    } else {
//        val message = if (langCode() == 1) "Please, enable phone-call permission in settings." else "세팅에서 전화걸기 권한을 켜주세요."
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}
//
//fun Fragment.popUpKeyboard(editText: EditText) {
//    editText.requestFocus()
//    val imm = this.baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
//}