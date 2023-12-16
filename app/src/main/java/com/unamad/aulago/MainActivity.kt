package com.unamad.aulago

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.unamad.aulago.repository.GeneralRepository
import com.unamad.aulago.repository.StudentRepository
import com.unamad.aulago.ui.components.StatusBar
import com.unamad.aulago.ui.views.RouteView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.messaging.isAutoInitEnabled = true

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            InitApplication()
        }
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun InitApplication() {
    RouteView()

}


@Composable
fun CustomOnResume(
    callback: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = context as? LifecycleOwner
    val updatedLifecycleOwner by rememberUpdatedState(lifecycleOwner)

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) callback?.invoke()

        }
        updatedLifecycleOwner?.lifecycle?.addObserver(observer)
        onDispose {
            updatedLifecycleOwner?.lifecycle?.removeObserver(observer)
        }
    }
}