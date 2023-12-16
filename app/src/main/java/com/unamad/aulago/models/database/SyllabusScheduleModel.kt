package com.unamad.aulago.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SyllabusSchedule")
class SyllabusScheduleModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val endDate: String,
    val startDate: String,
    val typeDescription: String
)