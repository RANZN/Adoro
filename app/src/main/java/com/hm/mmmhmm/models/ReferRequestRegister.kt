package com.hm.mmmhmm.models

data class ReferRequestRegister(
    var name: String,
    var number: Long?,
    var email: String,
    var username: String,
    var token: String,
    val myGroupInfo: Array<Int> = emptyArray(),
    val followingData: Array<Int> = emptyArray(),
    val followerData: Array<Int> = emptyArray(),
    val referedBy: String
)

data class IPAddressCheck(
    val ipAddress: String
)