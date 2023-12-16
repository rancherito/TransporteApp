package com.unamad.aulago.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.map
import com.google.gson.Gson
import com.unamad.aulago.Roles
import com.unamad.aulago.SystemDatabase
import com.unamad.aulago.classes.Validation
import com.unamad.aulago.genericReplacement
import com.unamad.aulago.models.apiModels.UserApiModel
import com.unamad.aulago.models.apiModels.WhatsappLinkApiModel
import com.unamad.aulago.models.database.BuildingModel
import com.unamad.aulago.models.database.CareerModel
import com.unamad.aulago.models.database.ClassScheduleModel
import com.unamad.aulago.models.database.ClassroomModel
import com.unamad.aulago.models.database.CourseModel
import com.unamad.aulago.models.database.PaymentModel
import com.unamad.aulago.models.database.RoleUserModel
import com.unamad.aulago.models.database.SectionModel
import com.unamad.aulago.models.database.StudentSectionModel
import com.unamad.aulago.models.database.SyllabusScheduleModel
import com.unamad.aulago.models.database.SystemDataModel
import com.unamad.aulago.models.database.SystemModel
import com.unamad.aulago.models.database.TeacherSectionModel
import com.unamad.aulago.models.database.TermModel
import com.unamad.aulago.models.database.UnitsScheduleModel
import com.unamad.aulago.models.database.UserModel
import com.unamad.aulago.models.query.SessionQueryModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject


open class GeneralRepository @Inject constructor(
    val database: SystemDatabase,
    private val scope: CoroutineScope,
    @ApplicationContext val context: Context
) {

    val userSystemDataStream = database.generalDao().userSystemDataStream()

    suspend fun getSystemData(): SystemDataModel {
        val currentUserSystem = database.generalDao().getSystemData()
        if (currentUserSystem == null) {
            val sys = SystemDataModel()
            insertOrUpdateSystemData(sys, "Insert if first call")
            return sys
        }
        return currentUserSystem
    }

    private suspend fun insertSystemUser(user: UserModel, token: String, tokenVerify: Boolean) {
        val checkUser = database.generalDao().getUser(user.id)
        if (checkUser != null) {
            val currentUserSystem = getSystemData()
            currentUserSystem.currentUserId = checkUser.id
            currentUserSystem.token = token
            currentUserSystem.tokenVerify = tokenVerify
            insertOrUpdateSystemData(currentUserSystem, message = "Insert system user")
        }
    }

    suspend fun insertOrUpdateSystemData(systemDataModel: SystemDataModel, message: String) {

        Log.i("system insert", message + " || " + systemDataModel.assistanceSectionId)
        database.generalDao().insertSystemData(systemDataModel)
        database.generalDao().updateSystemData(systemDataModel)

    }

    fun closeSessionAsync() {
        scope.launch {
            database.generalDao().closeSession()
            database.systemDao().deleteAll()
        }
    }

    fun replaceUser(user: List<UserModel>) {
        genericReplacement(
            list = user,
            insertFun = database.generalDao()::insertUser,
            updateFun = database.generalDao()::updateUser
        )
    }

    private fun insertOrUpdateCredentials(roleUserModel: RoleUserModel) {
        //Verificamos que no exista duplicidad de usuarios para poder insertar caso contrario actualizamos datos
        val checkUser = database.generalDao().getUser(roleUserModel.userId)

        if (checkUser != null) {
            val credential = database.generalDao().getCredentialUser(roleUserModel.userId)
            if (credential != null) roleUserModel.id = credential.id
            database.generalDao().insertCredentials(roleUserModel)
        }
    }

    suspend fun getSessionData(): SessionQueryModel? {
        return database.generalDao().getUserSystemData()
    }


    suspend fun validateCredentials(userApiModel: UserApiModel) {
        val user = UserModel(
            id = userApiModel.id,
            name = userApiModel.name,
            paternalSurname = userApiModel.paternalSurname,
            maternalSurname = userApiModel.maternalSurname,
            sex = userApiModel.sex,
            email = userApiModel.email,
            phoneNumber = userApiModel.phoneNumber?.replace(" ", ""),
            modifyAt = LocalDateTime.now().toString()

        )
        replaceUser(listOf(user))

        insertOrUpdateCredentials(
            RoleUserModel(
                userId = userApiModel.id,
                role = userApiModel.role
            )
        )
        insertSystemUser(user, "Bearer " + userApiModel.token, tokenVerify = true)
    }


}