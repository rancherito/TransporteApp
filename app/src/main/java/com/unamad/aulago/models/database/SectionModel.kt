package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Sections", foreignKeys = [
        ForeignKey(
            entity = CourseModel::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TermModel::class,
            parentColumns = ["id"],
            childColumns = ["termId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SectionModel(
    @PrimaryKey
    override val id: String,
    val code: String,
    @ColumnInfo(index = true)
    val courseId: String,
    @ColumnInfo(index = true)
    val termId: String,
    val modifyAt: String,
    val isDirectedCourse: Boolean,
    val vacancies: Int,
    val colorNumber: Long,
    val representativeStudentUserId: String? = null,
    val externalGroupLink: String? = null,
): IModel
