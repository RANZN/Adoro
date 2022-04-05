package com.hm.mmmhmm.models

class CreateGroupRequest internal constructor( var category: String?,
                                               var description: String?,
                                               var groupName: String?,
                                               var groupProfile: String?,
                                               //var memberData: List<Any>?,
                                               val memberData : Array<Int> = emptyArray(),
                                               var privacy: String?)