package com.unamad.aulago.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UnitsSchedule")
data class UnitsScheduleModel(
    @PrimaryKey
    override val id: String,
    val componentId: String,
    val component: Int,
    val numberUnit: Int,
    val startDate: String? = null,
    val endDate: String? = null
) : IModel