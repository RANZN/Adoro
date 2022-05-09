package com.hm.mmmhmm.models

data class GetContestModel(
    var OK: OKGetContest?
)

data class OKGetContest(
    var _createdDate: String?,
    var _id: String?,
    var _owner: String?,
    var _updatedDate: String?,
    var appliedNumber: Int?,
    var brandLogo: String?,
    var brandName: String?,
    var comment: List<Any>?,
    var contest: String?,
    var description: String?,
    var duration: String?,
    var endDate: String?,
    var featured: String?,
    var featuredImage: String?,
    var grandTotal: String?,
    var gst: Int?,
    var like: List<Any>?,
    var memeData: List<Any>?,
    var quantity: Int?,
    var reportCounting: Int?,
    var serviceSubCategory: String?,
    var serviceType: String?,
    var shortDescription: String?,
    var total: Int?
)