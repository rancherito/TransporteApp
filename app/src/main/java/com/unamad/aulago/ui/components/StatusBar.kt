package com.unamad.aulago.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.unamad.aulago.CustomOnResume
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.viewModelInstance

@Composable
fun StatusBar(
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isContrast = viewModelStorage.isContrast.observeAsState(false).value
    val isDark = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()
    CustomOnResume {
        viewModelStorage.debtAndHomeworkAsync(context)
    }

    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        darkIcons = if(isDark) !isContrast else isContrast
    )
    systemUiController.setNavigationBarColor(
        color = Color.Transparent,
        darkIcons = if(isDark) !isContrast else isContrast
    )


}
