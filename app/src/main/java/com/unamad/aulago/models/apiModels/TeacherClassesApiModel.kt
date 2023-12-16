package com.unamad.aulago.models.apiModels

data class TeacherClassesApiModel(
    val classId: String,
    val classScheduleId: String,
    val startTime: String,
    val endTime: String,
    val isDictated: Boolean
)