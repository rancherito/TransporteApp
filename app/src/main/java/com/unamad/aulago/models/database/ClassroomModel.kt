package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Classrooms",
    foreignKeys = [
        ForeignKey(
            entity = BuildingModel::class,
            parentColumns = ["id"],
            childColumns = ["buildingId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ClassroomModel(
    @PrimaryKey
    override val id: String,
    val name: String,
    @ColumnInfo(index = true)
    val buildingId: String,
    val floor: Int,
    val typeName: String
): IModel
