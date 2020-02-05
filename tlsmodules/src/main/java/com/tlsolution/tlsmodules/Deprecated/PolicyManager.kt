package com.tlsolution.tlsmodules.Deprecated

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import com.tlsolution.tlsmodules.Models.Policy
import com.tlsolution.tlsmodules.VRTManager

@Deprecated(message = "View-related classes are no longer supoported.")
class PolicyManager(rootActivity: Activity): VRTManager<Policy>(rootActivity) {


    companion object {
        val POLICY_DATA = "POLICY_DATA"
    }

    override fun launch(items: ArrayList<Policy>) {
        val intent = Intent(rootActivity.baseContext, PolicyListActivity::class.java)
        intent.putParcelableArrayListExtra(POLICY_DATA, items as java.util.ArrayList<out Parcelable>)
        rootActivity.startActivity(intent)
    }
}