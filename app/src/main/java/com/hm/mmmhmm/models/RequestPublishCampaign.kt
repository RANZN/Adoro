package com.hm.mmmhmm.models

data class RequestPublishCampaign(
    var _id: String?,
    var memeDetail: MemeDetail?
)

data class MemeDetail(
    var _id: String?,
    var image: String?,
    var profile: String?,
    var username: String?
)