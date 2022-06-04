package com.hm.mmmhmm.models

class PostLikeRequest internal constructor(
    var _id: String?,
    var likeData: PostLikeData?
)
class PostLikeData internal constructor(
    var id: String?,
    var profile: String?,
    var profilePhoto: String?,
    var userName: String?
)