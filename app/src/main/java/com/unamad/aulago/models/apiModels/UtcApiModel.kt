package com.unamad.aulago.models.apiModels

import com.google.gson.annotations.SerializedName

data class UtcApiModel(
    @SerializedName("\$id") val id: String? = null,
    val currentDateTime: String? = null,
    val utcOffset: String? = null,
    val isDayLightSavingsTime: Boolean? = null,
    val dayOfTheWeek: String? = null,
    val timeZoneName: String? = null,
    val currentFileTime: Long? = null,
    val ordinalDate: String? = null,
    val serviceResponse: String? = null
)
