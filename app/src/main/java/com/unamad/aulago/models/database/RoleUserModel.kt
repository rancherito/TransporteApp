package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "Credentials",
    foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RoleUserModel(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(index = true)
    val userId: String,
    val role: String
)
