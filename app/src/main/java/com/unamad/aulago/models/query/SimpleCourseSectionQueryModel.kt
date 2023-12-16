package com.unamad.aulago.models.query

data class SimpleCourseSectionQueryModel(
    val courseName: String,
    val sectionId: String,
    val colorNumber: Long,
    val sectionCode: String,
    val nextDatetime: String? = null,
    val careerCode: String? = null,
    val careerName: String? = null,
    val courseCode: String,
)
