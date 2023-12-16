package com.unamad.aulago.models.apiModels

data class StudentAbsenceJustificationApiModel(
    val studentAbsenceJustificationsId: String,
    val name: String,
    val code: String,
    val course: String,
    val status: Int,
    val file: String?,
    val description: String,
    val date: String,
    val classDate: String,
    val observation: String?,
    val sectionId: String,
    val termId: String
)