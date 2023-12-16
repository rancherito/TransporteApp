package com.unamad.aulago.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Buildings")
data class BuildingModel(
    @PrimaryKey
    override val id: String,
    val name: String,
    val floors: Int
): IModel
