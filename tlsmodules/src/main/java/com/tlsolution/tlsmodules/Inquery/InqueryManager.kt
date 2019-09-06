package com.tlsolution.tlsmodules.Inquery

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Parcelable
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tlsolution.tlsmodules.VRTManager

class InqueryManager(rootActivity: Activity): VRTManager<Inquery>(rootActivity) {

    companion object {
        val INQUERY_DATA = "INQUERY_DATA"
        val INQUERY_WRITTEN = "INQUERY_WRITTEN"
        val INQUERY_PROCESSED = "INQUERY_PROCESSED"

        val TAG = "InqueryManager"
    }

    var newInqueryAction: (String) -> Unit = { }

    override fun launch(items: ArrayList<Inquery>) {
        val intent = Intent(rootActivity.baseContext, InqueryListActivity::class.java)
        intent.putParcelableArrayListExtra(INQUERY_DATA, items as java.util.ArrayList<out Parcelable>)
        rootActivity.startActivity(intent)
        LocalBroadcastManager.getInstance(rootActivity.baseContext).registerReceiver(mMessageReceiver, IntentFilter("Inquery"))
    }

    /**
     * 새로운 문의사항을 서버로 보내서 성공적인 응답을 얻은 경우에 호출하는 함수
     */
    public fun completedOnNewInquery(inqueries: ArrayList<Inquery>) {
        val intent = Intent("InqueriesBack")
        intent.putParcelableArrayListExtra(INQUERY_PROCESSED, inqueries as java.util.ArrayList<out Parcelable>)
        LocalBroadcastManager.getInstance(rootActivity.baseContext).sendBroadcast(intent)
    }

    /**
     *  새로운 문의사항을 서버로 보내는 통신이나 처리 과정에서 실패가 일어났을 때 호출하는 함수
     * */
    public fun failedOnNewInquery() {
        val intent = Intent("NewInqueryFailed")
        intent.putExtra(INQUERY_PROCESSED, false)
        LocalBroadcastManager.getInstance(rootActivity.baseContext).sendBroadcast(intent)
    }

    /**
     * InqueryManager의 사용이 끝나면 불러야 하는 함수(registeredReceiver를 다시 unregister해 줘야 한다)
     */
    public fun destroy() {
        LocalBroadcastManager.getInstance(rootActivity.baseContext).unregisterReceiver(mMessageReceiver)
    }

    private val mMessageReceiver = (object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val inquery = intent?.getStringExtra(INQUERY_WRITTEN)
            check(inquery != null) {
                Log.d(TAG, "No Inquery...")
                return
            }

            newInqueryAction(inquery)
        }
    })

}
