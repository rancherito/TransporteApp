package com.unamad.aulago.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "studentsHomeworks"
)
data class StudentHomeworkModel(
    @PrimaryKey
    val id: String,
    val contentId: String,
    val homeworkId: String,
    val sectionId: String,
    val homeworkTitle: String,
    val homeworkDescription: String,
    val show: Boolean,
    val dateBegin: String,
    val dateEnd: String,
    val unitName: String,
    val attempts: Int,
    val isGroup: Boolean,
    val usedAttempts: Int,
    val courseName: String,
    val sectionCode: String,
    val studentUserId: String
)