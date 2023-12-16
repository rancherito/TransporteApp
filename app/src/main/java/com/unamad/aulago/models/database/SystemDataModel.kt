package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "SystemData", foreignKeys = [
        ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["currentUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SystemDataModel(
    @PrimaryKey
    override var id: String = UUID.randomUUID().toString(),
    var token: String? = null,
    @ColumnInfo(index = true)
    var currentUserId: String? = null,
    var termLastDataCached: String? = null,
    var teachersLastDataCached: String? = null,
    var companionsLastDataCached: String? = null,
    var studentsLastDataCached: String? = null,
    var scheduleLastDataCached: String? = null,
    var assistanceSectionId: String? = null,
    var tokenVerify: Boolean? = null
): IModel