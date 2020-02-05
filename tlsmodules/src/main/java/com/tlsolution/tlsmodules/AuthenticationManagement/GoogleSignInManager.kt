package com.tlsolution.tlsmodules.AuthenticationManagement


import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleSignInManager(val activity: Activity) {

    private var client: GoogleSignInClient? = null

    var googleSignInHandler: GoogleSignInHandler? = null

    companion object {
        val TAG = "GoogleSignInManager"
        val REQUEST_CODE = 32
    }

    fun config() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        this.client = GoogleSignIn.getClient(activity, gso)
    }

    fun signIn() {
        check(client != null) {
            Log.d(TAG, "Client is null")
        }

        val intent = client!!.signInIntent
        activity.startActivityForResult(intent,
            REQUEST_CODE
        )
    }

    fun signOut() {
        check(client != null) {
            Log.d(TAG, "Client is null")
        }
        client!!.signOut().addOnCompleteListener {
            googleSignInHandler?.didSignOut(this)
        }
    }

    fun evaluateResult(requestCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val email = account?.email ?: account?.id
                googleSignInHandler?.didSignIn(this, email)
            } catch (e: ApiException) {
                Log.d(TAG, e.message)
            }
        }
    }
}

interface GoogleSignInHandler {
    fun didSignIn(googleSignInManager: GoogleSignInManager, email: String?)
    fun didSignOut(googleSignInManager: GoogleSignInManager)
}