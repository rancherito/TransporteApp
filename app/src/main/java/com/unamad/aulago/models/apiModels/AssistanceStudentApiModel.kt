package com.unamad.aulago.models.apiModels

data class AssistanceStudentApiModel(
    val classId: String,
    val studentUserId: String,
    val fullName: String = "",
    var isAbsent: Boolean?,
    val id: String?
)