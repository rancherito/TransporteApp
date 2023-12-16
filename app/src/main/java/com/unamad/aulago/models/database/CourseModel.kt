package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Courses",
    foreignKeys = [
        ForeignKey(
            entity = CareerModel::class,
            parentColumns = ["id"],
            childColumns = ["careerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]

)
data class CourseModel(
    @PrimaryKey
    override val id: String,
    val name: String,
    val academicYear: Int,
    val isElective: Boolean,
    val modifyAt: String,
    val code: String,
    val colorNumber: Long,
    @ColumnInfo(index = true)
    val careerId: String? = null
): IModel
