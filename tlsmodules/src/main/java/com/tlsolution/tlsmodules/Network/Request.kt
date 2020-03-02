package com.tlsolution.tlsmodules.Network

interface Request {
    var baseURL: String
    var url: String?
    var body: String?
    var method: String?
    var endpoint: String
    var tokenURL: String
    var environmentMode: RequestEnvironmentMode

    fun getBaseURLString(): String
}
