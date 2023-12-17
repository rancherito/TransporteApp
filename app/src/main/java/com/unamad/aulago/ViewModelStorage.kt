package com.unamad.aulago

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unamad.aulago.classes.ResponseData
import com.unamad.aulago.models.apiModels.UserApiModel
import com.unamad.aulago.repository.GeneralRepository
import com.unamad.aulago.repository.ConductorRepository
import com.unamad.aulago.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ViewModelStorage @Inject constructor(
    val adminRepository: AdminRepository,
    val conductorRepository: ConductorRepository,
    val generalRepository: GeneralRepository,
) : ViewModel() {
    private val uuidViewModel: String = UUID.randomUUID().toString()
    private val _isLoadGlobalData = MutableLiveData<Boolean>()
    val isLoadGlobalData: LiveData<Boolean> = _isLoadGlobalData


    val isOnline = MutableLiveData(false)

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

    suspend fun verifyAccess(userName: String, password: String): ResponseData<UserApiModel> {

        if (userName == "ADMIN" && password == "ADMIN") {

            return ResponseData(
                data = UserApiModel(
                    token = "dfsfs",
                    name = "ADMIN",
                    paternalSurname = "ADMIN",
                    maternalSurname = "Admin",
                    sex = 1,
                    role = "ADMIN",
                    id = UUID.randomUUID().toString()
                ),
                message = "OK",
                isSuccess = true
            )
        }




        if (userName == "USER" && password == "USER") {

            return ResponseData(
                data = UserApiModel(
                    token = "dfsfs",
                    name = "USER",
                    paternalSurname = "USER",
                    maternalSurname = "USER",
                    sex = 1,
                    role = "USER",
                    id = UUID.randomUUID().toString()
                ),
                message = "OK",
                isSuccess = true
            )
        }

        return ResponseData(
            data = null,
            message = "Usuario o contraseña incorrectos",
            isSuccess = false
        )

    }

    fun closeSession() {
        viewModelScope.launch(Dispatchers.IO) {
            generalRepository.closeSessionAsync()
        }
    }

    suspend fun validateCredentials(userApiModel: UserApiModel) {
        generalRepository.validateCredentials(userApiModel)
    }

    fun loadDependencyDataAsync(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoadGlobalData.postValue(true)
            _isLoadGlobalData.postValue(false)
        }
    }

    fun loadInitialData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoadGlobalData.postValue(true)
            } catch (e: Exception) {
                viewModelScope.launch {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
            _isLoadGlobalData.postValue(false)
        }
    }



}
