package com.tlsolution.tlsmodules.Network


import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.tlsolution.tlsmodules.UserManager.User
import okhttp3.*
import java.io.File
import java.io.IOException


enum class RequestResult {
    failure, pSuccess, nSuccess, tryAgain, error;

    var statusCode: Int? = null
    var errorMessage: String? = null
    var newToken: String? = null
}

class RequestManager(val rootActivity: Activity) {

    companion object {
        val TAG = "Request Manager"
    }

    fun request(r: Request, c: User? = null, cb: ((RequestResult?, LinkedTreeMap<String, Any>?) -> Unit)) {
        r.environmentMode = RequestEnvironmentMode.getCurrentMode(this.rootActivity) ?: RequestEnvironmentMode.production

        val url = r.url
        var body: RequestBody? = null
        if (r.body != null) {
            body = RequestBody(r.body!!)
        }
        val client = OkHttpClient()

        val requestBuilder = okhttp3.Request.Builder().method(r.method, body).url(url)

        val token = c?.token
        if (token != null) {  requestBuilder.addHeader("authorization", token).addHeader("platform", "Android") }

        val request = requestBuilder.build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                val result = RequestResult.failure
                result.errorMessage = e.message
                cb(result, null)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "${response}")
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

                val linkedTreeMap = GsonBuilder().create().fromJson(jsonString,  LinkedTreeMap<String, Any>().javaClass)
                val data = linkedTreeMap.get("Data") as? LinkedTreeMap<String, Any>
                cb(evaluateResponse(response), data)
            }
        })
    }

    fun uploadRequest(r: Request,
                      c: User? = null,
                      files: ArrayList<File>?,
                      isImage: Boolean, cb: ((RequestResult?, LinkedTreeMap<String, Any>?) -> Unit)) {
        r.environmentMode = RequestEnvironmentMode.getCurrentMode(this.rootActivity) ?: RequestEnvironmentMode.production

        val url = r.url
        var body: MultipartBody? = null
        val MEDIA_TYPE: MediaType? = if(isImage){MediaType.parse("image/jpg")} else{
            MediaType.parse("video/mp4") }
        var builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

        files?.forEach {
            builder.addFormDataPart("Files", "imageData", RequestBody.create(MEDIA_TYPE, it!!))

        }

        body = builder.build()
        val requestBuilder = okhttp3.Request.Builder()
            .post(body)
            .url(url)
            .header("Content-Type", "multipart/form-data")
        val token = c?.token
        if (token != null) {
            requestBuilder
                .addHeader("authorization", token)
        }
        val client = OkHttpClient()
        val request = requestBuilder.build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val result = RequestResult.failure
                result.errorMessage = e.message
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "${response}")
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


                val data = GsonBuilder().create()
                    .fromJson(jsonString, LinkedTreeMap<String, Any>().javaClass)
                Log.d(TAG, "${data}")
                cb(evaluateResponse(response), data)
            }
        })
    }

    private fun renewToken(r: Request, c: User?, cb: ((RequestResult?) -> Unit)) {

        val secretKey = c?.clientSecret
        val body = RequestBody("{\"Data\" : {\"clientSecretKey\":\"$secretKey\", \"type\":\"${c?.type}\"}}")
        val requestBuilder = okhttp3.Request.Builder().method("POST", body).url(r.tokenURL)
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
                val linkedTreeMap = GsonBuilder().create().fromJson(jsonString,  LinkedTreeMap<String, Any>().javaClass)
                val newTokenJson = linkedTreeMap.get("Data") as? LinkedTreeMap<String, Any>
                val newToken = newTokenJson?.get("token") as? String

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
            400, 401, 403, 404, 406, 409 -> {
                result = RequestResult.nSuccess
                return result
            }
            500 -> {
                result = RequestResult.error
                return  result
            }
        }

        return null
    }

    private fun RequestBody(jsonString: String): RequestBody {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString)
    }
}