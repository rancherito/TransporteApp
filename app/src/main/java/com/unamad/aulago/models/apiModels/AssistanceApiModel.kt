package com.unamad.aulago.models.apiModels

data class AssistanceApiModel(
    val course: String,
    val total: Int,
    val maxAbsences: Int,
    val dictated: Int,
    val assisted: Int,
    val absences: Int,
    val termId: String,
    val sectionId: String,
    val code: String,
    val userId: String
)

