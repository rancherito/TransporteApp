package com.unamad.aulago.models.apiModels

import com.google.gson.annotations.SerializedName

data class ScheduleApiModel(
    @SerializedName("classScheduleId") val classScheduleId: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("weekDay") val weekDay: Int,
    @SerializedName("courseId") val courseId: String,
    @SerializedName("classroomCode") val classroomCode: String,
    @SerializedName("buildingName") val buildingName: String,
    @SerializedName("classroomId") val classroomId: String,
    @SerializedName("buildingId") val buildingId: String,
    @SerializedName("sectionId") val sectionId: String,
    @SerializedName("classroomTypeName") val classroomTypeName: String,
    @SerializedName("floor") val floor: String?,
    @SerializedName("buildingFloors") val buildingFloors: String?
)
