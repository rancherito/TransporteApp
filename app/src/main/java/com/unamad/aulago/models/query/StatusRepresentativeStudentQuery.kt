package com.unamad.aulago.models.query

data class StatusRepresentativeStudentQuery (
    val courseName: String,
    val code: String,
    val colorNumber: Int,
    val externalGroupLink: String?,
    val sectionId: String

)