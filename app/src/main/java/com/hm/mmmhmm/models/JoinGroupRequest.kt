package com.hm.mmmhmm.models

class JoinGroupRequest internal constructor(
    var groupDetail: GroupDetail,var memberDetail: MemberDetail?,
)

class  GroupDetail internal constructor(
    var _id: String?,
    var category: String?,
    var description: String?,
    var groupName: String?,
    var groupProfile: String?,
    var privacy: String?
)

class MemberDetail internal constructor(
    var name: String?,
    var profile: String?,
    var userID: String?,
    var username: String?
)

