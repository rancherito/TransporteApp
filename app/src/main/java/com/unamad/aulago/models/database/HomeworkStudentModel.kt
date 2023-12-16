package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(
    tableName = "HomeworkStudents",
    foreignKeys = [
        ForeignKey(
            parentColumns = ["id"],
            childColumns = ["sectionId"],
            entity = SectionModel::class,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            parentColumns = ["id"],
            childColumns = ["studentUserId"],
            entity = UserModel::class,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HomeworkStudentModel(
    @PrimaryKey
    override val id: String,
    val contentId: String,
    @ColumnInfo(index = true)
    val sectionId: String,
    val homeworkTitle: String? = "",
    val homeworkDescription: String? = "",
    val show: Boolean,
    val dateBegin: String,
    val dateEnd: String,
    val unitName: String,
    val attempts: Int,
    val isGroup: Boolean,
    val usedAttempts: Int,
    @ColumnInfo(index = true)
    val studentUserId: String
): IModel