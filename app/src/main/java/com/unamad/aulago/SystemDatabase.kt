package com.unamad.aulago

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unamad.aulago.dao.GeneralDao
import com.unamad.aulago.dao.SystemDao
import com.unamad.aulago.dao.ConductorDao.ConductorDao
import com.unamad.aulago.dao.AdminDao.AdminDao
import com.unamad.aulago.models.database.RoleUserModel
import com.unamad.aulago.models.database.SystemDataModel
import com.unamad.aulago.models.database.SystemModel
import com.unamad.aulago.models.database.TaxiModel
import com.unamad.aulago.models.database.UserModel

@Database(
    entities = [
        UserModel::class,
        SystemDataModel::class,
        RoleUserModel::class,
        SystemModel::class,
        TaxiModel::class
    ],
    version = 1,
    exportSchema = false,
    autoMigrations = []
)
abstract class SystemDatabase : RoomDatabase() {
    abstract fun generalDao(): GeneralDao
    abstract fun studentDao(): ConductorDao
    abstract fun teacherDao(): AdminDao
    abstract  fun systemDao(): SystemDao

}
