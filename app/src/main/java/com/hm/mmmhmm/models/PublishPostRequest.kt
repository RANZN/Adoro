package com.hm.mmmhmm.models



class PublishPostRequest internal constructor(
    var username: String,
    var image: String,
    var description: String,
    var like: List<Any>?,
    var comment: List<Any>?,
    var serialNumber: Int,
    var id: String,
    var profile: String,
)