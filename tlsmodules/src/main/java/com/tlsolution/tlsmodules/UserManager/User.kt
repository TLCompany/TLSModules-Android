package com.tlsolution.tlsmodules.UserManager

/**
 * TLS에서 만드는 모든 앱에서 사용하는 User들이 기본적으로 가져야 할 perperties를 가지고 있는 Interface
 */
interface User {
    var id: Int
    var clientSecret: String?
    var token: String?
    var json: String?
    var type: Int?
}
