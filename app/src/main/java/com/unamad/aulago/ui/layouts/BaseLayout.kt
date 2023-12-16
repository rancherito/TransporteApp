package com.unamad.aulago.ui.layouts

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.unamad.aulago.SystemConfig
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.emums.ServerAddressType
import com.unamad.aulago.ui.components.DevelopmentBox
import com.unamad.aulago.ui.components.LoadingBox
import com.unamad.aulago.ui.theme.WrapperTheme
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance

@SuppressLint("DiscouragedApi", "InternalInsetResource")
@Composable
fun BaseLayout(
    modifier: Modifier = Modifier,
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    primaryColor: Color? = null,
    loadingState: Boolean = false,
    content: @Composable (BoxScope.() -> Unit)
) {
    val resources = LocalContext.current.resources

    val density: Float = resources.displayMetrics.density
    val resourceNavigationBarId =
        resources.getIdentifier("navigation_bar_height", "dimen", "android")
    val resourceStatusBarId = resources.getIdentifier("status_bar_height", "dimen", "android")

    val statusLoad = viewModelStorage.isLoadGlobalData.observeAsState(false)

    val heightNavigationBar =
        if (resourceNavigationBarId > 0) resources.getDimensionPixelSize(resourceNavigationBarId) else 0
    val heightNavigation = (heightNavigationBar / density).dp

    val heightStatusBar =
        if (resourceStatusBarId > 0) resources.getDimensionPixelSize(resourceStatusBarId) else 0
    val heightStatus = (heightStatusBar / density).dp

    WrapperTheme(
        primaryColor = primaryColor,
    ) {

        if (loadingState) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .zIndex(3f)
                    .pointerInput(Unit) {},
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingBox(color = Color.White)
                Text(
                    text = "Cargando...",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }


        Box(
            modifier = modifier
                .pointerInput(Unit) {
                }
                .fillMaxSize()
                .background(color = myColors().background)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Box(
                    Modifier
                        .height(heightStatus)
                        .fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    content()
                }
                if (statusLoad.value) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                } else {
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Box(
                    Modifier
                        .height(heightNavigation)
                        .fillMaxWidth()
                )
            }
        }
    }
}