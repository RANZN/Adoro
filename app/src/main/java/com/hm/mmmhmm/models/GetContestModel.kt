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
    var campaignName: String?,
    var comment: List<Any>?,
    var contest: String?,
    var description: String?,
    var designType: String?,
    var downloadBalance: Int?,
    var endDate: String?,
    var featured: String?,
    var featuredImage: String?,
    var grandTotal: String?,
    var gst: Int?,
    var guidlines: String?,
    var like: List<ContestLike>?,
    var logoOnPost: String?,
    var memeData: List<Any>?,
    var officialEmail: String?,
    var paymentStatus: String?,
    var quantity: Int?,
    var serviceSubCategory: String?,
    var serviceType: String?,
    var shortDescription: String?,
    var targetIndustry: String?,
    var timeLeft: String?,
    var total: Int?,
    var useOfStockImage: String?
)
data class ContestLike(
    var id: String?,
    var profileLink: String?,
    var profilePhoto: String?,
    var userName: String?
)

