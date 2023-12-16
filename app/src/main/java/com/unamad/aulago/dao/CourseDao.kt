package com.unamad.aulago.dao

import androidx.room.*
import com.unamad.aulago.models.database.CourseModel

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(courseModel: List<CourseModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(courseModel: List<CourseModel>)

    @Delete
    fun delete(courseModel: List<CourseModel>)

    @Query("""SELECT * FROM Courses""")
    fun list(): List<CourseModel>
}
