package com.unamad.aulago.models.apiModels

data class TeacherInfo(
    val teacherId: String,
    val isPrincipal: Boolean,
    val teacherSectionId: String,
    val name: String,
    val paternalSurname: String,
    val maternalSurname: String,
    val email: String,
    val phoneNumber: String,
    val sex: Int,
)
