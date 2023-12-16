package com.unamad.aulago

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unamad.aulago.api.IApiGeneral
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.classes.ResponseData
import com.unamad.aulago.classes.UserCredential
import com.unamad.aulago.emums.ServerAddressType
import com.unamad.aulago.models.apiModels.UserApiModel
import com.unamad.aulago.models.database.TeacherClassModel
import com.unamad.aulago.models.query.ScheduleCourseQueryModel
import com.unamad.aulago.repository.GeneralRepository
import com.unamad.aulago.repository.StudentRepository
import com.unamad.aulago.repository.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ViewModelStorage @Inject constructor(
    val teacherRepository: TeacherRepository,
    val studentRepository: StudentRepository,
    val generalRepository: GeneralRepository,
    private val iApiGeneral: IApiGeneral
) : ViewModel() {
    private val uuidViewModel: String = UUID.randomUUID().toString()
    private val _isLoadGlobalData = MutableLiveData<Boolean>()
    val isLoadGlobalData: LiveData<Boolean> = _isLoadGlobalData


    val isOnline = MutableLiveData(false)

    //DATABASE LIVEDATA VARIABLES INITIAL DECLARATION
    val listClassDatesSection: LiveData<List<TeacherClassModel>> =
        generalRepository.classDatesSectionStream

    val listStudentScheduleLiveData: LiveData<List<ScheduleCourseQueryModel>> =
        generalRepository.studentCoursesScheduleStream

    //INTERFACE LIVEDATA VARIABLES DECLARATION
    val headerInformation = MutableLiveData(HeaderInformation(""))

    val isContrast = MutableLiveData(false)
    val nowMilli = MutableLiveData(Utils.nowMilli())


    init {
        Log.i("unique id view_model", uuidViewModel)

        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000)
                nowMilli.postValue(Utils.nowMilli())
            }
        }
    }

    suspend fun apiVerifyAccess(userName: String, password: String): ResponseData<UserApiModel> {
        return try {
            iApiGeneral.getTokenAccess(UserCredential(userName, password))
        } catch (e: Exception) {
            if (SystemConfig.serverAddress == ServerAddressType.Production) Utils.createErrorResponse(
                "ERROR EN EL SERVIDOR"
            )
            else Utils.createErrorResponse("Validación: " + e.message)
        }
    }

    fun closeSession() {
        viewModelScope.launch(Dispatchers.IO) {
            generalRepository.closeSession()
        }
    }

    suspend fun validateCredentials(userApiModel: UserApiModel) {
        generalRepository.validateCredentials(userApiModel)
    }

    fun loadTeacherStudent(sectionId: String) {

        viewModelScope.launch(Dispatchers.IO) {
            teacherRepository.loadTeacherStudent(sectionId)
        }
    }

    fun loadTeacherClasses() {
        viewModelScope.launch(Dispatchers.IO) {
            val session = generalRepository.getSessionData() ?: return@launch
            teacherRepository.loadTeacherClasses(session)
        }
    }

    fun loadDependencyDataAsync(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoadGlobalData.postValue(true)
            loadDependencyDataSync(context)
            _isLoadGlobalData.postValue(false)
        }
    }

    fun loadInitialData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoadGlobalData.postValue(true)
                generalRepository.loadTerm()
            } catch (e: Exception) {
                viewModelScope.launch {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
            _isLoadGlobalData.postValue(false)
        }
    }

    private suspend fun loadDependencyDataSync(context: Context) {
        val session = generalRepository.getSessionData() ?: return
        try {
            if (session.role == Roles.Student) {
                studentRepository.loadDependency(session)
            }
            if (session.role == Roles.Teacher) {
                val validation = teacherRepository.loadTeacherDependency(session)
                if (!validation.isValid) throw Exception(validation.message)
            }

        } catch (e: Exception) {
            Log.i("error", e.message.toString())
            viewModelScope.launch {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun startOnlineVerification(context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            while (true) {
                delay(1000)
                isOnline.postValue(Utils.isOnline(context))
            }
        }
    }

    fun debtAndHomeworkAsync(current: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val session = generalRepository.getSessionData() ?: return@launch
                if (session.role != Roles.Student) return@launch
                studentRepository.loadDebtSync(session)
                studentRepository.loadHomeworkSync(session)

            } catch (e: Exception) {
                viewModelScope.launch {
                    Toast.makeText(current, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
