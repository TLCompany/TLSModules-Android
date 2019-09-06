package com.tlsolution.tlsmodules.Authentication

import android.app.Activity
import android.util.Log
import com.kakao.auth.*
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import java.util.ArrayList


class KakaoSignInManager(val activity: Activity): KakaoSignInCallbackHandler {

    override fun didFetchUserInfo(email: String, nickname: String) {
        kakaoSignInHandler?.didSignIn(this, email, nickname)
    }

    private var kakaoCallback: KakaoCallback? = null
    var kakaoSignInHandler: KakaoSignInHandler? = null

    companion object {
        val TAG = "KakaoSignInManager"
    }

    /**
     * 카카오톡 로그인의 session & reqeust
     */
    fun config() {

        kakaoCallback = KakaoCallback(this)
        Session.getCurrentSession().addCallback(kakaoCallback)
        Log.d(TAG, "😃 kakaotalk signIn ready to go!")
    }

    fun signOut() {
        UserManagement.getInstance().requestLogout(object: LogoutResponseCallback() {
            override fun onCompleteLogout() {
                kakaoSignInHandler?.didSignOut(this@KakaoSignInManager)
            }
        })
    }

    fun signIn(){
        Session.getCurrentSession().checkAndImplicitOpen()
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, activity)
    }


    /**
     * 카카오톡 로그인 callback 하는 클라스
     */
    private class KakaoCallback(val callbackHander: KakaoSignInCallbackHandler): ISessionCallback {

        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.d("KakaoCallback", "Kakao Log-In Exception has occurred: ${exception}")
        }

        override fun onSessionOpened() {
            requestUsersProfileFromKakaotalk()
        }


        private fun requestUsersProfileFromKakaotalk() {
            //카카오톡 로그인 request
            var keys = ArrayList<String>()
            keys.add("properties.nickname")
            keys.add("kakao_account.email")

            UserManagement.getInstance().me(keys, object : MeV2ResponseCallback() {

                override fun onSessionClosed(errorResult: ErrorResult?) {

                }

                override fun onSuccess(result: MeV2Response?) {
                    val nickname = result?.nickname.toString()
                    val email = result?.kakaoAccount?.email ?: result?.id.toString()

                    callbackHander.didFetchUserInfo(email, nickname)
                }
            })
        }
    }

}

/**
 * SNS 로그인시  외부 또는 내부 클래스에서 작업 후 받은 정보를 SocialMediaSignInManager로 보내준다.
 */
interface KakaoSignInCallbackHandler {
    fun didFetchUserInfo(email: String, nickname: String)
}


/**
 * SNS 로그인시  외부 또는 내부 클래스에서 작업 후 받은 정보를 SocialMediaSignInManager로 보내준다.
 */
interface KakaoSignInHandler {
    fun didSignIn(kakaoSignInManager: KakaoSignInManager, email: String, nickname: String)
    fun didSignOut(kakaoSignInManager: KakaoSignInManager)
}
