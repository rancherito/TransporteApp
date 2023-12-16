package com.unamad.aulago.models.query

data class HomeworkStudentQueryModel(
    val homeworkTitle: String,
    val homeworkDescription: String,
    val sectionId: String,
    val show: Boolean,
    val dateBegin: String,
    val dateEnd: String,
    val unitName: String,
    val attempts: Int,
    val isGroup: Boolean,
    val usedAttempts: Int,
    val colorNumber: Long,
    val courseSectionCode: String,
    val courseName: String
)