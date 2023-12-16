package com.unamad.aulago

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.unamad.aulago.emums.ServerAddressType

class SystemConfig {
    companion object {
        val serverAddress = ServerAddressType.Home
        @SuppressLint("StaticFieldLeak")
        var navController: NavHostController? = null
        var viewModelStorage: ViewModelStorage? = null
        const val isProduction = BuildConfig.IS_PRODUCTION
    }
}

@Composable
fun navControllerInstance(): NavHostController {
    if (SystemConfig.navController == null)
        SystemConfig.navController = rememberNavController()
    return SystemConfig.navController!!
}

@Composable
fun viewModelInstance(): ViewModelStorage {
    if (SystemConfig.viewModelStorage == null) SystemConfig.viewModelStorage = hiltViewModel()
    return SystemConfig.viewModelStorage!!
}