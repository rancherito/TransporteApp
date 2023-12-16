package com.unamad.aulago.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unamad.aulago.models.database.TermModel

@Dao
interface TermDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(terms: List<TermModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(terms: List<TermModel>)

    @Query("SELECT * FROM Terms WHERE status = 1 ORDER BY name DESC LIMIT 1")
    fun getActive(): TermModel?

    @Query("SELECT * FROM Terms WHERE status = 1 ORDER BY name DESC LIMIT 1")
    fun activeTermStream(): LiveData<TermModel?>
}