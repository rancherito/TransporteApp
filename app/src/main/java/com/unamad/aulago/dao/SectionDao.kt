package com.unamad.aulago.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unamad.aulago.classes.UserContact
import com.unamad.aulago.models.database.SectionModel
import com.unamad.aulago.models.query.StatusRepresentativeStudentQuery

@Dao
interface SectionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(courseModel: List<SectionModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(courseModel: List<SectionModel>)

    @Delete
    fun delete(courseModel: List<SectionModel>)

    @Query("""SELECT * FROM Sections""")
    fun list(): List<SectionModel>

    @Query("""
        SELECT U.paternalSurname || ' ' || U.maternalSurname || ', ' || U.name fullName, U.phoneNumber, U.email, U.userName FROM Sections S
        JOIN Users U ON U.id = S.representativeStudentUserId
        WHERE S.id = :sectionId LIMIT 1
        """)
    fun getRepresentativeStudentStream(sectionId: String?): LiveData<UserContact?>
@Query("""
        SELECT '[' || C.code || '] ' || C.name courseName, S.code, S.colorNumber, S.externalGroupLink, S.id sectionId
        FROM Sections S
        JOIN Users U ON U.id = S.representativeStudentUserId
        JOIN SystemData SD ON SD.currentUserId = S.representativeStudentUserId
        JOIN Courses C ON C.id = S.courseId
        """)
    fun listStatusRepresentativeStream(): LiveData<List<StatusRepresentativeStudentQuery>>

}
