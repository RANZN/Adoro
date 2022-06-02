package com.hm.mmmhmm.models

data class CommentsData(
    var OK: OKComments?
)

data class OKComments(
    var items: List<ItemComment>?,
    var length: Int?,
    var query: Query?,
    var totalCount: Int?
)

data class ItemComment(
    var _createdDate: String?,
    var _id: String?,
    var _owner: String?,
    var _updatedDate: String?,
    var comment: List<Comment>?,
    var description: String?,
    var id: String?,
    var image: String?,
    var like: List<Like>?,
    var profile: String?,
    var serialNumber: Int?,
    var username: String?
)

data class Comment(
    var dateTime: String?,
    var id: String?,
    var profilePhoto: String?,
    var text: String?,
    var userName: String?
)

data class Like(
    var id: String?,
    var profile: String?,
    var userName: String?
)