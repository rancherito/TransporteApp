package com.unamad.aulago.models.query

data class CourseStudentTeachersQueryModel(
    val curseSectionCode: String,
    val courseName: String,
    val colorNumber: Long,
    val sectionId: String,
    val teacherUserId: String?,
    val isPrincipal: Boolean?,
    val teacherFullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val sex: Int?,
)