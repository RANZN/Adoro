package com.hm.mmmhmm.models

data class ServerAggreementModel(
    val `data`: ServerAgreementData?,
    val message: String?,
    val status: Int?
)

data class ServerAgreementData(
    var consenter_email: String?,
    val agmt_act_title: String?,
    val agmt_consente_sign_date: Any?,
    val agmt_consente_sign_img: Any?,
    val agmt_declaration: String?,
    val agmt_fixed_footer: String?,
    val agmt_fixed_header: String?,
    val agmt_fixed_subfooter: String?,
    val agmt_fondling_check: Int?,
    val agmt_fondling_title: String?,
    val agmt_oral_check: Int?,
    val agmt_oral_title: String?,
    var agmt_other_conduct_custom: Any? = "",
    val agmt_other_conduct_title: String?,
    val agmt_prosper_sign_date: Any?,
    val agmt_prosper_sign_img: Any?,
    val agmt_self_filler: String?,
    val agmt_sex_check: Int?,
    val agmt_sex_title: String?,
    val agmt_title: String?,
    val created_at: String?,
    val status: String?,
    val updated_at: String?
)