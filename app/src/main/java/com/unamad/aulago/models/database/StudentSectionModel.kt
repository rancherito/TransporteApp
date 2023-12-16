package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "StudentSections", foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["studentUserId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SectionModel::class,
            parentColumns = ["id"],
            childColumns = ["sectionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]

)
data class StudentSectionModel(
    @PrimaryKey
    override val id: String,
    @ColumnInfo(index = true)
    val studentUserId: String,
    @ColumnInfo(index = true)
    val sectionId: String
): IModel