package com.unamad.aulago.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StudentAssistance")
data class StudentAssistanceModel(
    @PrimaryKey
    val id: String,
    val sectionId: String,
    val course: String,
    val total: Int,
    val maxAbsences: Int,
    val dictated: Int,
    val assisted: Int,
    val absences: Int,
    val termId: String,
    val code: String,
    val userId: String,
)