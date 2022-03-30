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
    private val _updatedDate: String,
    private val email: String,
     val name: String,
    private val number: Long,
    private val username: String
)

data class QueryLoginResponse(
    private val collectionName: String,
//    private val filterTree: FilterTree,
    private val included: List<Any>,
    private val invalidArguments: List<Any>,
    private val limitNumber: Int,
//    private val provider: Provider,
    private val skipNumber: Int
)
//
//data class FilterTree(
//    val `$and`: List<And>
//)
//
//class Provider
//
//data class And(
//    val number: Long
//)