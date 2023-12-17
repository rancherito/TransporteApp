package com.unamad.aulago.ui.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.ui.layouts.BaseLayout
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AccessView() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
         AccessViewUi()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccessViewUi(
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    val coroutineScope = rememberCoroutineScope()
    val statusLogin = remember {
        mutableStateOf<Boolean?>(null)
    }
    val statusText = remember {
        mutableStateOf("")
    }

    val userInputText = remember {
        mutableStateOf("")
    }
    val passwordInputText = remember {
        mutableStateOf("")
    }
    val isAccessing = remember {
        mutableStateOf(false)

    }
    val isViewPassword = remember {
        mutableStateOf(false)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        ), label = ""
    )

    val validate: () -> Unit = {
        if (userInputText.value.length > 2 && passwordInputText.value.length > 2) {
            statusText.value = "Validando..."
            statusLogin.value = null
            coroutineScope.launch {
                isAccessing.value = true

                val response = withContext(Dispatchers.IO) {
                    viewModelStorage.verifyAccess(
                        userInputText.value,
                        passwordInputText.value
                    )
                }
                if (response.isSuccess && response.data != null) {
                    withContext(Dispatchers.IO) {
                        viewModelStorage.validateCredentials(response.data)
                    }
                    statusLogin.value = true
                } else {
                    statusText.value = response.message ?: ""
                    statusLogin.value = false
                }
                isAccessing.value = false
            }
        } else {
            statusText.value = "Mínimo 3 caracteres por campo"
            statusLogin.value = null
        }
    }

    BaseLayout() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logoaulagofull),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 64.dp)
                        .size(100.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Transporte App",
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.padding(32.dp))

                OutlinedTextField(
                    value = userInputText.value,
                    onValueChange = { userInputText.value = it },
                    placeholder = { Text(text = "Ingrese Usuario") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null
                        )
                    },
                    enabled = !isAccessing.value,
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        validate()
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp, 0.dp)
                        .focusGroup(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.padding(4.dp))
                OutlinedTextField(
                    value = passwordInputText.value,
                    onValueChange = { passwordInputText.value = it },
                    visualTransformation = if (isViewPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                    placeholder = { Text(text = "Ingrese Contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isViewPassword.value = !isViewPassword.value }) {
                            Icon(
                                painter = painterResource(id = if (isViewPassword.value) R.drawable.visibility_off else R.drawable.visibility),
                                contentDescription = null
                            )
                        }
                    },
                    singleLine = true,
                    enabled = !isAccessing.value,
                    keyboardActions = KeyboardActions(onDone = {

                        validate()
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp, 0.dp)
                        .focusGroup(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                when (statusLogin.value) {
                    // Mostrar texto correcto si es true
                    true -> Text(
                        text = "ACCESO CORRECTO",
                        color = myColors().success,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                    // Mostrar texto de error si es false
                    false -> {
                        Text(
                            text = statusText.value, color = myColors().danger, fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                        // Limpiar contraseña si es false
                        passwordInputText.value = ""
                        statusLogin.value = null

                    }
                    // Mostrar texto de validando si es null
                    else -> {
                        Text(
                            text = statusText.value, color = myColors().blackAlt, fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (statusLogin.value != true) {
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(
                        enabled = userInputText.value.length > 2 && passwordInputText.value.length > 2 && !isAccessing.value,
                        onClick = {
                            validate()
                        },
                        modifier = Modifier.focusGroup()
                    ) {
                        Text(text = "INGRESAR")
                        if (isAccessing.value) {
                            Icon(
                                painter = painterResource(id = R.drawable.loading),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(16.dp)
                                    .rotate(angle)
                            )

                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_narrow_right),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(16.dp)
                            )

                        }
                    }
                }

            }
        }
    }

}