package com.unamad.aulago.models.query

data class TeacherSectionInfoQueryModel(
    val colorNumber: Long,
    val courseName: String,
    val sectionCode: String,
    val fullName: String,
    val isPrincipal: Boolean,
    val isDirectedCourse: Boolean,
    val sex: Int,
    val sectionId: String,
    val totalEnrolled: Int,
    val totalClasses: Int,
    val careerName: String,
    val careerCode: String,
    val isMe: Boolean,
    val phoneNumber: String,
    val externalGroupLink: String? = null,
    val courseCode: String
)