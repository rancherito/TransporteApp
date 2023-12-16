package com.unamad.aulago.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedulePublishGrades")
data class SchedulePublishGradesModel(
    @PrimaryKey
    val id: String,
    val careerCode: String,
    val courseCode: String,
    val courseName: String,
    val sectionCode: String,
    val sectionId: String,
    val endDate: String,
    val numberOfUnit: Int,
    val startDate: String,
)
