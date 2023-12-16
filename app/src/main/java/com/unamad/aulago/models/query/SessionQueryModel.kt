package com.unamad.aulago.models.query

data class SessionQueryModel(
    val fullName: String,
    val token: String,
    val userId: String,
    val role: String
)
