package com.unamad.aulago.ui.layouts

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.ui.components.HeaderActions
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R
import com.unamad.aulago.ui.components.FloatingTextButton
import com.unamad.aulago.ui.components.LoadingBox

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("InternalInsetResource", "DiscouragedApi")
@Composable
fun CommonLayout(
    modifier: Modifier = Modifier,
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    navHostController: NavHostController = navControllerInstance(),
    headerInformation: HeaderInformation? = null,
    contentBottom: (@Composable (() -> Unit))? = null,
    paddingContentBottom: PaddingValues = PaddingValues(16.dp),
    stateContentBottom: MutableState<Boolean> = rememberBoolean(),
    primaryColor: Color? = null,
    loadingState: Boolean = false,
    updateAction: (() -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit)
) {
    viewModelStorage.isContrast.postValue(false)


    BaseLayout(modifier = modifier, primaryColor = primaryColor, loadingState = loadingState) {
        if (contentBottom != null) {

            if (stateContentBottom.value) {
                ModalBottomSheet(
                    onDismissRequest = { stateContentBottom.value = false },
                    containerColor = myColors().background,
                    windowInsets = WindowInsets.navigationBarsIgnoringVisibility
                ) {
                    Column(
                        modifier = Modifier
                            .padding(paddingContentBottom)
                    ) {
                        contentBottom()
                    }

                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            IconButton(onClick = {
                navHostController.navigateUp()
            }, modifier = Modifier.offset(x = 16.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_narrow_left),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            HeaderActions(
                headerInformation = headerInformation,
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 10.dp,
                    bottom = 16.dp
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                content(this)

            }
        }
        if (updateAction != null) {
            FloatingTextButton(
                text = "Actualizar",
                icon = painterResource(id = R.drawable.update),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-16).dp, y = (-16).dp),
                enabled = !loadingState
            ) {
                updateAction()
            }
        }
    }
}

@Composable
fun rememberBoolean(initial: Boolean = false): MutableState<Boolean> {
    val state = remember {
        mutableStateOf(initial)
    }
    return state
}
