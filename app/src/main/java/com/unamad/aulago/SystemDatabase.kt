package com.unamad.aulago

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unamad.aulago.dao.GeneralDao
import com.unamad.aulago.dao.SystemDao
import com.unamad.aulago.dao.ConductorDao.ConductorDao
import com.unamad.aulago.dao.AdminDao.AdminDao
import com.unamad.aulago.models.database.UserRoleModel
import com.unamad.aulago.models.database.SystemDataModel
import com.unamad.aulago.models.database.SystemModel
import com.unamad.aulago.models.database.TaxiModel
import com.unamad.aulago.models.database.UserModel

@Database(
    entities = [
        UserModel::class,
        SystemDataModel::class,
        UserRoleModel::class,
        SystemModel::class,
        TaxiModel::class
    ],
    version = 1,
    exportSchema = false,
    autoMigrations = []
)
abstract class SystemDatabase : RoomDatabase() {
    abstract fun generalDao(): GeneralDao
    abstract fun conductorDao(): ConductorDao
    abstract fun adminDao(): AdminDao
    abstract  fun systemDao(): SystemDao

}
