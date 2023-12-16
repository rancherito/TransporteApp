package com.unamad.aulago.models.apiModels
data class ScheduleGradesPublicationApiModel(
    val sectionId: String,
    val sectionCode: String,
    val courseName: String,
    val courseCode: String,
    val careerCode: String,
    val attempts: List<AttemptDateApiModel>
)
