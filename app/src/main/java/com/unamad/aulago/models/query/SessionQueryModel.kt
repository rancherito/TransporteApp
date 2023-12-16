package com.unamad.aulago.models.query

data class SessionQueryModel(
    val fullName: String,
    val token: String,
    val termId: String,
    val userId: String,
    val role: String
)
