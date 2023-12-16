package com.unamad.aulago.models.apiModels

data class StudentProfileApiModel(
    val name: String,
    val paternalSurname: String,
    val maternalSurname: String,
    val sex: String,
    val email: String?,
    val phoneNumber: String?,
    val userName: String,
    val careerName: String,
    val admissionTermName: String,
    val graduationTermName: String?,
    val lastEnrollment: String,
    val statusStudent: String
)

