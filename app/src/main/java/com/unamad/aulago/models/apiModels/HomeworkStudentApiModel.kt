package com.unamad.aulago.models.apiModels

data class HomeworkStudentApiModel(
    val contentId: String,
    val homeworkId: String,
    val sectionId: String,
    val homeworkTitle: String? = null,
    val homeworkDescription: String? = null,
    val show: Boolean,
    val dateBegin: String,
    val dateEnd: String,
    val unitName: String,
    val attempts: Int,
    val isGroup: Boolean,
    val usedAttempts: Int
)
