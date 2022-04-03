package com.hm.mmmhmm.models

data class Query(
     val collectionName: String?,
     val filterTree: FilterTree,
     val included: List<Any>?,
     val invalidArguments: List<Any>?,
     val limitNumber: Int,
     val provider: Provider,
     val skipNumber: Int?
)

