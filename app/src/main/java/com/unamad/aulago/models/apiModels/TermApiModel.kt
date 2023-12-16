package com.unamad.aulago.models.apiModels

import com.google.gson.annotations.SerializedName
import java.util.*


data class TermApiModel(

    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("isSummer") var isSummer: Boolean? = null,
    @SerializedName("number") var number: String? = null,
    @SerializedName("year") var year: Int,
    @SerializedName("status") var status: Int

)