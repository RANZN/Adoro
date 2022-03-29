package com.hm.mmmhmm.models

data class InviteListModel(
    val `data`: List<InviteList>,
    val message: String,
    val status: Int
)

data class InviteList(
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
    val agmt_other_conduct_custom: String?,
    val agmt_other_conduct_title: String?,
    val agmt_prosper_sign_date: Any?,
    val agmt_prosper_sign_img: Any?,
    val agmt_self_filler: String?,
    val agmt_sex_check: Int?,
    val agmt_sex_title: String?,
    val agmt_title: String?,
    val consenter: Proposer?,
    val consenter_email: String?,
    val consenter_id: Int?,
    val created_at: String?,
    val id: Int?,
    val parent_invite: Int?,
    val proposer: Proposer?,
    val proposer_id: Int?,
    val sent_by: Int?,
    val sent_to: Int?,
    val status: String?,
    val updated_at: String?
)


data class Proposer(
    val address: Any?,
    val created_at: String?,
    val device_id: String?,
    val device_type: String?,
    val dob: Any?,
    val email: String?,
    val email_verified_at: Any?,
    val gender: String?,
    val id: Int?,
    val identity_number: String?,
    val name: String?,
    val phone: Any?,
    val profile_picture: String?,
    val referal_code: String?,
    val role: String?,
    val sexual_orientation: String?,
    val updated_at: String?
)