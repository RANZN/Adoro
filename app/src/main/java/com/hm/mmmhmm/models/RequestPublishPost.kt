package com.hm.mmmhmm.models

data class RequestPublishPost(
    val comment: List<Any>,
    val description: String,
    val image: String,
    val like: List<Any>,
    val profile: String,
    val username: String
)