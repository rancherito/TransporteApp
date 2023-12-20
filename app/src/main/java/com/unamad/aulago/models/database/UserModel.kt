package com.unamad.aulago.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserModel(
    @PrimaryKey
    override val id: String,
    val name: String,
    val paternalSurname: String,
    val maternalSurname: String,
    val genero: String,
    val modifyAt: String,
    @ColumnInfo(defaultValue = "")
    val userName: String = "",
    val phoneNumber: String? = null,
    val email: String? = null,
    val password: String,
    val fechaNacimiento : String,
):IModel