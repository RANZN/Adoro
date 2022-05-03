package com.hm.mmmhmm.models

class RequestRegister internal constructor(
    var name: String,var number: Long?,var email: String,
   var username: String,var token: String,
    val myGroupInfo : Array<Int> = emptyArray(),
    val followingData : Array<Int> = emptyArray(),
    val followerData : Array<Int> = emptyArray(),
)