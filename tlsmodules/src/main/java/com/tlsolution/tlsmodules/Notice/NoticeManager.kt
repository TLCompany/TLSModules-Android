package com.tlsolution.tlsmodules.Notice

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import com.tlsolution.tlsmodules.VRTManager


class NoticeManager(rootActivity: Activity): VRTManager<Notice>(rootActivity) {

    companion object {
        val NOTICES_DATA = "ANNOUNCEMENT_DATA"
    }

    override fun launch(items: ArrayList<Notice>) {

        val intent = Intent(rootActivity.baseContext, NoticeListActivity::class.java)
        intent.putParcelableArrayListExtra(NOTICES_DATA, items as java.util.ArrayList<out Parcelable>)
        rootActivity.startActivity(intent)
    }

}