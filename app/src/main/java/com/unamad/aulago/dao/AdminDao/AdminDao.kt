package com.unamad.aulago.dao.AdminDao

import androidx.room.*
import com.unamad.aulago.models.database.UserModel
import com.unamad.aulago.models.database.UserRoleModel

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertarComductor(conductor: UserModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRol(rol: UserRoleModel)

}