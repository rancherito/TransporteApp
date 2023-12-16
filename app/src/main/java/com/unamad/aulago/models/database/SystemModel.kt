package com.unamad.aulago.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "System")
data class SystemModel(
    @PrimaryKey
    val key: String,
    val value: String?
)