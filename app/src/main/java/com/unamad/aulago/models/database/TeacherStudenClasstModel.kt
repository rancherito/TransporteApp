package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "TeacherClassStudent", foreignKeys = [
    ForeignKey(
        entity = UserModel::class,
        parentColumns = ["id"],
        childColumns = ["studentUserId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = TeacherClassModel::class,
        parentColumns = ["id"],
        childColumns = ["teacherClassId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class TeacherClassStudentModel(
    @PrimaryKey
    override val id: String,
    @ColumnInfo(index = true)
    val teacherClassId: String,
    @ColumnInfo(index = true)
    val studentUserId: String,
    val isAbsent: Boolean,
): IModel