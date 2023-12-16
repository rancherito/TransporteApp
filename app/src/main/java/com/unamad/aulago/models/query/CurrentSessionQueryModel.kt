package com.unamad.aulago.models.query
data class CurrentSessionQueryModel(
    val fullName: String,
    val token: String,
    val userId: String,
    val role: String,
    val termName: String? = null,
    val tokenVerify: Boolean,
    val name: String,
    val sex: Int
)
