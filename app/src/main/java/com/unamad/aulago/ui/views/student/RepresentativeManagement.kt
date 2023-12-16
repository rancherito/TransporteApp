package com.unamad.aulago.ui.views.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.unamad.aulago.Utils
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.query.StatusRepresentativeStudentQuery
import com.unamad.aulago.ui.components.IconButtonSys
import com.unamad.aulago.ui.components.AlertCard
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.layouts.rememberBoolean
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R

@Composable
fun RepresentativeManagement(
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    representative: List<StatusRepresentativeStudentQuery> =
        viewModelStorage.studentRepository.listStatusRepresentativeStream.observeAsState(
            emptyList()
        ).value
) {

    val status = rememberBoolean()
    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Delegado de Curso",
            subtitle = "Gestión el grupo de whatsapp y notificaciones"
        ),
        contentBottom = {

        },
        stateContentBottom = status,
    ) {


        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AlertCard(
                body = "Como delegado puedes gestionar el grupo de Whatsapp de los siguientes cursos"
            )
            representative.forEach {
                val courseColor = Color(it.colorNumber)

                val statusOpen = rememberBoolean()
                Dia(sectionId = it.sectionId, link = it.externalGroupLink, statusOpen)
                Row(
                    modifier = Modifier
                        .card()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.courseName,
                        fontSize = 12.sp,
                        color = courseColor,
                        modifier = Modifier.weight(1f),

                        )
                    IconButtonSys(
                        painter = painterResource(id = R.drawable.link),
                        onClick = {
                            statusOpen.value = true
                        }
                    )
                    IconButtonSys(
                        painter = painterResource(id = R.drawable.message),
                        onClick = {},
                        enabled = false
                    )
                }

            }
        }
    }
}

@Composable
fun Dia(
    sectionId: String,
    link: String?,
    statusOpen: MutableState<Boolean>,
    validLink: MutableState<Boolean> = remember { mutableStateOf(true) },
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    text: MutableState<String> = remember {
        mutableStateOf(link ?: "")
    },
) {



    if (statusOpen.value) {
        validLink.value = text.value == "" || Utils.isValidWhatsAppGroupLink(text.value)

        Dialog(onDismissRequest = {
            statusOpen.value = false
        }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .card(shadow = true)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.whatsapp),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = myColors().green
                )
                Text(
                    text = "Como delegado puedes agregar un enlace de grupo de Whatsapp para este curso y asi tus compañeros puedan unirse.",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )

                OutlinedTextField(
                    value = text.value,
                    onValueChange = {
                        text.value = it
                    },
                    placeholder = { Text(text = "Ingrese enlace de grupo de Whatsapp") },
                    colors = if (validLink.value) TextFieldDefaults.colors() else TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Red,
                        focusedIndicatorColor = Color.Red,
                        focusedLabelColor = Color.Red,
                    ),
                    label = { Text(text = "Enlace de grupo de Whatsapp") },
                    supportingText = {
                        if (!validLink.value) {
                            Text(
                                text = "Enlace no válido",
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        } else {
                            Text(
                                text = "Ejemplo: https://chat.whatsapp.com/DK...",
                                fontSize = 12.sp
                            )
                        }
                    },

                    )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        statusOpen.value = false
                    }) {
                        Text(text = "Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {

                        viewModelStorage.studentRepository.saveWhatsappLinkAsync(
                            sectionId = sectionId,
                            link = text.value
                        )
                        statusOpen.value = false

                    }, enabled = Utils.isValidWhatsAppGroupLink(text.value)) {
                        Text(text = "Guardar")
                    }
                }
            }
        }

    }
}