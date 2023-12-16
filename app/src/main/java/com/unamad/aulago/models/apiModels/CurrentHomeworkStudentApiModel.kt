package com.unamad.aulago.models.apiModels
data class CurrentHomeworkStudentApiModel(
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
    val id: String
)