package com.unamad.aulago.models.apiModels

data class StudentSectionApiModel(
    val userId: String,
    val studentSection: String,
    val name: String,
    val paternalSurname: String,
    val userName: String,
    val maternalSurname: String,
    val sex: Int,
    val email: String?,
    val phoneNumber: String?
)
