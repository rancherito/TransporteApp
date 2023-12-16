package com.unamad.aulago.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.unamad.aulago.models.database.CareerModel

@Dao
interface CareerDao {

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(careers: List<CareerModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(careers: List<CareerModel>)
}