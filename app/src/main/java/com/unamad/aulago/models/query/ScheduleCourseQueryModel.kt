package com.unamad.aulago.models.query

data class ScheduleCourseQueryModel(
    val courseName: String,
    val courseCode: String,
    val sectionCode: String,
    val colorNumber: Long,
    val sectionId: String,
    val startTime: Int? = null,
    val endTime: Int? = null,
    val weekDay: Int? = null,
    val buildingName: String? = null,
    val classroomName: String? = null,
    val typeName: String? = null,
    val floor: Int? = null,
    val careerCode: String? = null,
    val careerName: String? = null,
    val role: String
)