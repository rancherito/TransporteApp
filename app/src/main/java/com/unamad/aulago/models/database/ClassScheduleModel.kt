package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ClassSchedule",
    foreignKeys = [
        ForeignKey(
            entity = SectionModel::class,
            parentColumns = ["id"],
            childColumns = ["sectionId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ClassroomModel::class,
            parentColumns = ["id"],
            childColumns = ["classroomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ClassScheduleModel(
    @PrimaryKey
    override val id: String,
    val startTime: Int,
    val endTime: Int,
    val weekDay: Int,
    @ColumnInfo(index = true)
    val sectionId: String,
    @ColumnInfo(index = true)
    val classroomId: String
): IModel
