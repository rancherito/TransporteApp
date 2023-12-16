package com.unamad.aulago.models.query

data class AcademicChargeQueryModel(
    val isMe: Boolean,
    val colorNumber: Long,
    val courseName: String,
    val courseSectionCode: String,
    val fullName: String,
    val isPrincipal: Boolean,
    val isDirectedCourse: Boolean,
    val sex: Int,
    val phoneNumber: String?,
    val sectionId: String,
    val enrolledStudents: Int,
    val careerName: String? = null,
    val careerCode: String? = null
)