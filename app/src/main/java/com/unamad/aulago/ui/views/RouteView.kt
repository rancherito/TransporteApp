package com.unamad.aulago.ui.views

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unamad.aulago.NavigationApp
import com.unamad.aulago.Roles
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.ui.views.conductor.ConductorDashboardView
import com.unamad.aulago.ui.views.admin.AdminDashboardView
import com.unamad.aulago.ui.views.admin.NewConductor
import com.unamad.aulago.ui.views.admin.TeacherGeneralSchedulesView
import com.unamad.aulago.viewModelInstance


@SuppressLint("SuspiciousIndentation")
@Composable
fun RouteView(
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    


    viewModelStorage.loadInitialData(context = LocalContext.current)

    val loginStatus = viewModelStorage.generalRepository.userSystemDataStream.observeAsState()
    val isOnline = viewModelStorage.isOnline.observeAsState(false).value

    if (loginStatus.value != null && isOnline) {
        viewModelStorage.loadDependencyDataAsync(context = LocalContext.current)
        if (!loginStatus.value!!.tokenVerify) viewModelStorage.closeSession()
    }
    val navController = navControllerInstance()

        when (loginStatus.value?.role) {
            //SI EL ROL ES DE CONDUCTOR
            Roles.Conductor -> {
                NavHost(
                    navController = navController,
                    startDestination = NavigationApp.Conductor.DASHBOARD
                ) {
                    composable(route = NavigationApp.Conductor.DASHBOARD) {
                        ConductorDashboardView()
                    }
                }
            }
            //SI EL ROL ES DE ADMINISTRADOR
            Roles.Administrador -> {
                NavHost(
                    navController = navController,
                    startDestination = NavigationApp.Administrator.DASHBOARD
                ) {
                    composable(route = NavigationApp.Administrator.DASHBOARD) {
                        AdminDashboardView()
                    }
                    composable(route = NavigationApp.Administrator.GENERAL_SCHEDULE) {
                        TeacherGeneralSchedulesView()
                    }
                    composable(route = "newConductor") {
                        NewConductor()
                    }
                }

            }
            // SI NO HAY ROL MOSTRAMOS LA VISTA DE ACCESO
            else -> {
                AccessView()
            }

        }


}

