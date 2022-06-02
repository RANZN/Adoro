package com.hm.mmmhmm.models

import com.google.gson.annotations.SerializedName

data class IpStatusModel (

    @SerializedName("OK" ) var OK : OKj? = OKj()

)
data class Data (

    @SerializedName("_id"          ) var Id          : String? = null,
    @SerializedName("_createdDate" ) var CreatedDate : String? = null,
    @SerializedName("_updatedDate" ) var UpdatedDate : String? = null,
    @SerializedName("ipAddress"    ) var ipAddress   : String? = null,
    @SerializedName("status"       ) var status      : String? = null,
    @SerializedName("referedBy"    ) var referedBy   : String? = null

)
data class OKj (

    @SerializedName("status" ) var status : String? = null,
    @SerializedName("data"   ) var data   : Data?   = Data()

)