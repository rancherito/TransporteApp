package com.unamad.aulago.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unamad.aulago.models.database.SystemModel

@Dao
interface SystemDao {
    @Query("SELECT value FROM System WHERE `key` = :key")
    fun get(key: String): String?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(systemModel: SystemModel)
    @Update
    fun update(systemModel: SystemModel)

    @Query("SELECT value FROM System WHERE `key` = :key")
    fun getStream(key: String): LiveData<String?>
    @Query("DELETE FROM System")
    fun deleteAll()




}