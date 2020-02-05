package com.tlsolution.tlsmodules.Deprecated

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tlsolution.tlsmodules.Extensions.showActionableConfirmAlert
import com.tlsolution.tlsmodules.Extensions.showNoActionConfirmAlert
import com.tlsolution.tlsmodules.Extensions.showTwoActionableAlert
import com.tlsolution.tlsmodules.Models.Inquery
import com.tlsolution.tlsmodules.R
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.activity_new_inquery.*

@Deprecated(message = "View-related classes are no longer supoported.")
class NewInqueryActivity : TLSActivity() {

    companion object {
        val COMPLETED = "COMPLETED_ON_NEW_INQUERY"
    }

    var successBroadcastManager = LocalBroadcastManager.getInstance(this)
    var failureBroadcastManager = LocalBroadcastManager.getInstance(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_inquery)

        setUpActionBar(findViewById(R.id.newInqueryActionBar), "문의하기", "보내기")
        successBroadcastManager.registerReceiver(successReceiver, IntentFilter("InqueriesBack"))
        failureBroadcastManager.registerReceiver(failureReceiver, IntentFilter("NewInqueryFailed"))
    }


    private val successReceiver = (object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val inqueries = intent?.getParcelableArrayListExtra<Inquery>(InqueryManager.INQUERY_PROCESSED)
            check(inqueries != null) {
                Log.d(InqueryManager.TAG, "No Inqueries...")
                return
            }

            val data = Intent()
            data.putParcelableArrayListExtra(COMPLETED, inqueries as java.util.ArrayList<out Parcelable>)
            setResult(Activity.RESULT_OK, data)
            runOnUiThread {
                finish()
            }
        }
    })

    private val failureReceiver = (object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            runOnUiThread {
                showActionableConfirmAlert("", getString(R.string.unknown_error_meesage)) {
                    finish()
                }
            }
        }
    })

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(successReceiver)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(failureReceiver)
    }

    override fun rightAction() {

        val inquery = inqueryTextEdit.text.toString()
        check(!inquery.isEmpty()) {
            showNoActionConfirmAlert("", getString(R.string.inquery_no_content))
            return
        }

        showTwoActionableAlert("", getString(R.string.inquery_complete_question), getString(R.string.ok), getString(R.string.cancel), {
            val intent = Intent("Inquery")
            intent.putExtra(InqueryManager.INQUERY_WRITTEN, inquery)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            (Unit)
        })
    }
}
