package com.hm.mmmhmm.models

data class CampaignDetailModel(
    var OK: OKCampaignDetailModel?
)

data class OKCampaignDetailModel(
    var items: List<ItemCampaignDetailModel>?,
    var length: Int?,
    var query: QueryCampaignDetailModel?,
    var totalCount: Int?
)

data class ItemCampaignDetailModel(
    var _createdDate: String?,
    var _id: String?,
    var _owner: String?,
    var _updatedDate: String?,
    var appliedNumber: Int?,
    var brandLogo: String?,
    var brandName: String?,
    var campaignName: String?,
    var comment: List<Any>?,
    var description: String?,
    var downloadBalance: Int?,
    var duration: String?,
    var endDate: String?,
    var grandTotal: String?,
    var gst: Int?,
    var guidlines: String?,
    var like: List<Any>?,
    var logoOnPost: String?,
    var memeData: List<Any>?,
    var officialEmail: String?,
    var paymentStatus: String?,
    var quantity: Int?,
    var reportCounting: Int?,
    var serviceSubCategory: String?,
    var serviceType: String?,
    var shortDescription: String?,
    var targetIndustry: String?,
    var total: Int?,
    var useOfStockImage: String?
)

data class QueryCampaignDetailModel(
    var collectionName: String?,
    var filterTree: FilterTree?,
    var included: List<Any>?,
    var invalidArguments: List<Any>?,
    var limitNumber: Int?,
    var provider: Provider?,
    var skipNumber: Int?
)
