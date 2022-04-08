package com.hm.mmmhmm.models

class GroupDiscussionPostUpdateLikeRequest internal constructor(
    var groupDiscussionPostID: String?,
    var likeData: LikeData?
)
class LikeData internal constructor(
    var id: String?,
    var profileLink: String?,
    var profilePhoto: String?,
    var userName: String?
)