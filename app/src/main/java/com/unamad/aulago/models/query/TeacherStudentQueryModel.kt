package com.unamad.aulago.models.query

data class TeacherStudentQueryModel(
    val fullName: String,
    val sex: Int,
    val phoneNumber: String?,
    val email: String?,
    val userName: String,
    val userId: String
)