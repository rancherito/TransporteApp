package com.unamad.aulago.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Terms")
data class TermModel(
    @PrimaryKey
    override val id: String,
    val name: String,
    val createdAt: String? = null,
    val isSummer: Boolean? = null,
    val number: String? = null,
    val year: Int,
    val status: Int
): IModel
