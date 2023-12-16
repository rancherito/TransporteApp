package com.unamad.aulago.models.query

data class StudentSectionQueryModel(
    val courseSectionCode: String,
    val courseName: String,
    val sectionId: String,
    val studentFullName: String,
    val sex: Int,
    val colorNumber: Long,
    val phoneNumber: String?,
    val email: String?,
    val externalGroupLink: String?,
    val representativeStudentUserId: String?
)