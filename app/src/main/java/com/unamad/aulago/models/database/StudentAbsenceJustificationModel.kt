package com.unamad.aulago.models.database

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unamad.aulago.MaterialColors
import com.unamad.aulago.MaterialColorsList

@Entity(tableName = "StudentAbsenceJustification")
data class StudentAbsenceJustificationModel(
    @PrimaryKey
    val id: String,
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

enum class StatusStudentAbsenceJustification(val value: Int, val description: String, val color: Color) {
    REQUESTED(1, "Solicitado", Color.Gray),
    TEACHER_APPROVED(2, "Aprobado por docente", MaterialColorsList.blue),
    OBSERVED(3, "Observado", MaterialColorsList.yellow),
    ACCEPTED(4, "Aceptado", MaterialColorsList.green),
    DENIED(5, "Denegado", MaterialColorsList.red)
}
fun Int.toStatusStudentAbsenceJustification(): StatusStudentAbsenceJustification {
    return when (this) {
        1 -> StatusStudentAbsenceJustification.REQUESTED
        2 -> StatusStudentAbsenceJustification.TEACHER_APPROVED
        3 -> StatusStudentAbsenceJustification.OBSERVED
        4 -> StatusStudentAbsenceJustification.ACCEPTED
        5 -> StatusStudentAbsenceJustification.DENIED
        else -> StatusStudentAbsenceJustification.REQUESTED
    }
}