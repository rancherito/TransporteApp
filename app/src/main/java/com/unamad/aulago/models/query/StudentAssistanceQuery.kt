package com.unamad.aulago.models.query
//course, total, maxAbsences, dictated, assisted, absences, code, C.colorNumber
data class StudentAssistanceQuery (
    val course: String,
    val total: Int,
    val maxAbsences: Int,
    val dictated: Int,
    val assisted: Int,
    val absences: Int,
    val code: String,
    val colorNumber: Long
)