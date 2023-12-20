package com.unamad.aulago.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unamad.aulago.models.database.*
import com.unamad.aulago.models.query.*

@Dao
interface GeneralDao {

    //REGION USER QUERIES
    @Query("SELECT * FROM Users U WHERE U.id = :userId")
    fun getUser(userId: String): UserModel?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: List<UserModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateUser(user: List<UserModel>)
    //ENDREGION

    //REGION SYSTEM QUERIES
    @Query(
        """ 
        SELECT 
            (u.paternalSurname || ' ' || u.maternalSurname || ', ' || U.name ) fullName, 
            U.name,
            U.genero,
            s.token,
            (s.tokenVerify IS NOT NULL) tokenVerify, 
            c.role, c.userId
        FROM SystemData s 
        INNER JOIN Users u ON u.id = s.currentUserId 
        INNER JOIN userrole c ON c.userId = u.id
        LIMIT 1"""
    )
    fun userSystemDataStream(): LiveData<CurrentSessionQueryModel?>

    @Query(
        """ 
        SELECT (U.paternalSurname || ' ' || U.maternalSurname || ', ' || U.name) fullName, s.token, c.role, c.userId
        FROM SystemData s 
        INNER JOIN Users u ON u.id = s.currentUserId
        INNER JOIN userrole c ON c.userId = u.id
        LIMIT 1"""
    )
    suspend fun getUserSystemData(): SessionQueryModel?

    @Query("SELECT * FROM SystemData S LIMIT 1")
    fun getSystemData(): SystemDataModel?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSystemData(systemData: SystemDataModel)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateSystemData(systemData: SystemDataModel)

    @Query("DELETE FROM SystemData")
    fun closeSession()
    //ENDREGION

    //REGION CREDENTIALS
    @Query("SELECT * FROM  userrole WHERE userId = :userId")
    fun getCredentialUser(userId: String): UserRoleModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCredentials(credential: UserRoleModel)

    //ENDREGION


}
