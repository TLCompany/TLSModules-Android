package com.tlsolution.tlsmodules

import android.app.Activity
import android.os.Parcelable

interface TLSManager{}

open class VRTManager<T: Parcelable>: TLSManager {

    lateinit var rootActivity: Activity

    constructor(rootActivity: Activity) {
        this.rootActivity = rootActivity
    }

    open fun launch(items: ArrayList<T>) {

    }
}