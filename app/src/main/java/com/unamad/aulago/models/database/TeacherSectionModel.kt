package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "TeacherSections", foreignKeys = [
        ForeignKey(
            entity = SectionModel::class,
            parentColumns = ["id"],
            childColumns = ["sectionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["teacherUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TeacherSectionModel(
    @PrimaryKey
    override val id: String,
    @ColumnInfo(index = true)
    val sectionId: String,
    @ColumnInfo(index = true)
    val teacherUserId: String,
    val isPrincipal: Boolean,
): IModel