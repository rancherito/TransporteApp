package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Payments", foreignKeys = [
    ForeignKey(
        entity = UserModel::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class PaymentModel(
    @PrimaryKey
    override val id: String,
    val termName: String ? = null,
    val description: String,
    val issueDate: String,
    val paymentDate: String?,
    val total: Double,
    val isBankPayment: Boolean,
    val status: Int,
    val createdBy: String?,
    @ColumnInfo(index = true)
    val userId: String,
    val conceptCode: String?
): IModel
