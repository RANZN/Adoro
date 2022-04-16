package com.hm.mmmhmm.models

data class PostCommentRequest(
    var _id: String?,
    var commentData: CommentData?
)

data class CommentData(
    var dateTime: String?,
    var id: String?,
    var profilePhoto: String?,
    var text: String?,
    var userName: String?
)