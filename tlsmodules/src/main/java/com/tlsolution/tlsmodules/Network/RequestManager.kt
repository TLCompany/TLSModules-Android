package com.tlsolution.tlsmodules.Network


import android.util.Log
import com.google.gson.GsonBuilder
import com.tlsolution.tlsmodules.UserManagement.User
import okhttp3.*
import java.io.IOException


enum class RequestResult {
    failure, pSuccess, nSuccess, tryAgain, error;

    var statusCode: Int? = null
    var errorMessage: String? = null
    var newToken: String? = null
}

class RequestManager {

    companion object {
        val TAG = "Request Manager"
    }

    fun request(r: Request, c: User? = null, cb: ((RequestResult?, HashMap<String, Any>?) -> Unit)) {

        val url = r.url
        var body: RequestBody? = null
        if (r.body != null) {
            body = RequestBody(r.body!!)
        }
        val client = OkHttpClient()
        val requestBuilder = okhttp3.Request.Builder().method(r.method, body).url(url)

        val token = c?.token
        if (token != null) {  requestBuilder.addHeader("authorization", token) }

        val request = requestBuilder.build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                val result = RequestResult.failure
                result.errorMessage = e.message
                cb(result, null)
            }

            override fun onResponse(call: Call, response: Response) {

                val jsonString = response.body()?.string()
                check(jsonString != null) {
                    Log.d(TAG, "😱 json string is null...")
                    return
                }

                if (response.code() == 419) {
                    renewToken(r, c) {
                        if (it == RequestResult.tryAgain) {
                            it.statusCode = 419
                            cb(it, null)
                        } else {
                            cb(RequestResult.error, null)
                        }
                    }
                    return
                }

                val hashMap = GsonBuilder().create().fromJson(jsonString,  HashMap<String, Any>().javaClass)
                cb(evaluateResponse(response), hashMap)
            }
        })
    }

    private fun renewToken(r: Request, c: User?, cb: ((RequestResult?) -> Unit)) {
        val secretKey = c?.clientSecret
        val body = RequestBody("{\"clientSecretKey\":\"$secretKey\"}")
        val requestBuilder = okhttp3.Request.Builder().method(r.method, body).url(r.tokenURL)
        val request = requestBuilder.build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                val result = RequestResult.failure
                result.errorMessage = e.message
                cb(result)
            }

            override fun onResponse(call: Call, response: Response) {
                val statusCode = response.code()
                val jsonString = response.body()?.string()
                val hashMap = GsonBuilder().create().fromJson(jsonString,  HashMap<String, Any>().javaClass)
                val newToken = hashMap.get("token") as? String

                check(newToken != null) {
                    cb(RequestResult.nSuccess)
                    return
                }

                var result = RequestResult.tryAgain
                result.newToken = newToken
                result.statusCode = response.code()
                result.errorMessage = response.message()

                when (statusCode) {
                    401 -> {
                        result = RequestResult.nSuccess
                        cb(result)
                    }
                    200-> {
                        result = RequestResult.pSuccess
                        cb(result)
                    }
                    500 -> {
                        result = RequestResult.error
                        cb(result)
                    }
                    else -> cb(null)
                }
            }
        })
    }

    private fun evaluateResponse(response: Response): RequestResult? {
        val statusCode = response.code()
        var result = RequestResult.pSuccess

        result.errorMessage = response.message()
        result.statusCode = response.code()

        when (statusCode) {
            200, 201, 202 -> {
                result = RequestResult.pSuccess
                return result
            }
            400, 401, 404, 406 -> {
                result = RequestResult.nSuccess
                return result
            }
        }

        return null
    }

    private fun RequestBody(jsonString: String): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString)
    }
}