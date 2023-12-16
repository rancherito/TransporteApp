package com.unamad.aulago.models.query

data class StudentHomeworkQueryModel  (
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
    val studentUserId: String,
    val colorNumber: Int
)