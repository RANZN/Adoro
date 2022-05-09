package com.hm.mmmhmm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
     val OK: OKLoginResponse
)

data class OKLoginResponse(
     val items: List<ItemLoginResponse>,
    private val length: Int,
    private val query: QueryLoginResponse,
    private val totalCount: Int
)

data class ItemLoginResponse(
    private  val _createdDate: String,
//    @SerializedName("_id")
//    @Expose
      val _id: String,
     val _updatedDate: String,
     val email: String,
     val name: String,
     val number: Long,
    val username: String,
    val profile: String,
    val referCode: String,
)

data class QueryLoginResponse(
     val collectionName: String,
val filterTree: FilterTree,
     val included: List<Any>,
     val invalidArguments: List<Any>,
     val limitNumber: Int,
val provider: Provider,
     val skipNumber: Int
)
