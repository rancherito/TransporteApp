package com.unamad.aulago.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Careers")
data class CareerModel(
    @PrimaryKey
    override val id: String,
    val code: String,
    val name: String
): IModel
