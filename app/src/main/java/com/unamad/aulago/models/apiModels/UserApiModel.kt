package com.unamad.aulago.models.apiModels

import com.google.gson.annotations.SerializedName

data class UserApiModel(
    @SerializedName("token") var token: String,
    @SerializedName("name") var name: String,
    @SerializedName("paternalSurname") var paternalSurname: String,
    @SerializedName("maternalSurname") var maternalSurname: String,
    @SerializedName("sex") var sex: Int,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phoneNumber") var phoneNumber: String? = null,
    @SerializedName("role") var role: String,
    @SerializedName("id") var id: String

)