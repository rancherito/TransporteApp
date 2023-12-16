package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "TeacherClasses", foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["teacherUserId"],
            onDelete = ForeignKey.CASCADE
        ), ForeignKey(
            entity = ClassScheduleModel::class,
            parentColumns = ["id"],
            childColumns = ["classScheduleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TeacherClassModel(
    @PrimaryKey
    override val id: String,
    @ColumnInfo(index = true)
    val classScheduleId: String,
    val startTime: String,
    val endTime: String,
    val isDictated: Boolean,
    @ColumnInfo(index = true)
    val teacherUserId: String
): IModel