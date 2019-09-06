package com.tlsolution.tlsmodules.Authentication

import android.os.Bundle
import android.view.View
import com.tlsolution.tlsmodules.TLSActivity
import kotlinx.android.synthetic.main.view_auth_description.view.*

/**
 * 모든 Authentication에 관련된 Acitivity의 super class
 */
open class AuthenticationActivity: TLSActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    internal fun setUpDescription(descriptionView: View, title: String, subtitle: String) {
        descriptionView.titleTextView.setText(title)
        descriptionView.subtitleTextView.setText(subtitle)
    }
}