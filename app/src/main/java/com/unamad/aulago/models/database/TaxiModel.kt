package com.unamad.aulago.models.database

import androidx.room.PrimaryKey
@androidx.room.Entity(
    tableName = "Taxis",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = UserModel::class,
            parentColumns = ["id"],
            childColumns = ["duenioUserId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)
data class TaxiModel(
    @PrimaryKey
    val id: String,
    val duenioUserId : String,
    val placa: String,
    val marca: String,
    val modelo: String,
    val tipoTaxi: Int,
    val numoeroAsientos: Int,
)
