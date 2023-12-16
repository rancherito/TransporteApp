package com.unamad.aulago.ui.views.teacher

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.unamad.aulago.NavigationApp
import com.unamad.aulago.Utils
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.UserContact
import com.unamad.aulago.light
import com.unamad.aulago.models.database.SchedulePublishGradesModel
import com.unamad.aulago.models.query.TeacherSectionInfoQueryModel
import com.unamad.aulago.models.query.TeacherStudentQueryModel
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.toHumanString
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.components.HorizontalTextIcon
import com.unamad.aulago.ui.components.IconButtonSys
import com.unamad.aulago.ui.components.SearcherBox
import com.unamad.aulago.ui.components.AlertCard
import com.unamad.aulago.ui.components.UserContactBox
import com.unamad.aulago.ui.layouts.SectionLayout
import com.unamad.aulago.ui.layouts.rememberBoolean
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherSectionView(
    sectionId: String,
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    sectionInfo: TeacherSectionInfoQueryModel? = viewModelStorage.teacherRepository.getTeacherSectionStream(
        sectionId = sectionId
    ).observeAsState().value,
    schedulePublishGrades: List<SchedulePublishGradesModel> = viewModelStorage.teacherRepository.listTeacherSchedulePublishGradesStream(
        sectionId = sectionId
    ).observeAsState(emptyList()).value,
    studentsSection: List<TeacherStudentQueryModel> = viewModelStorage.teacherRepository.listTeacherStudentsStream(
        sectionId = sectionId
    ).observeAsState(emptyList()).value,
    representativeStudentFullNameStream: UserContact? = viewModelStorage.teacherRepository.getRepresentativeStudentFullNameStream(
        sectionId = sectionId
    ).observeAsState().value
) {

    if (sectionInfo != null) {
        Init(
            sectionId = sectionId,
            sectionInfo = sectionInfo,
            schedulePublishGrades = schedulePublishGrades,
            studentsSection = studentsSection,
            representativeStudentFullNameStream = representativeStudentFullNameStream
        )
    } else {
        EmptyCard(
            title = "Sección no encontrada",
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Init(
    sectionId: String,
    sectionInfo: TeacherSectionInfoQueryModel,
    schedulePublishGrades: List<SchedulePublishGradesModel>,
    studentsSection: List<TeacherStudentQueryModel>,
    representativeStudentFullNameStream: UserContact?,
    navHostController: NavHostController = navControllerInstance(),
    viewModelStorage: ViewModelStorage = viewModelInstance(),
) {
    val units = schedulePublishGrades.map { it.numberOfUnit }
    val context = LocalContext.current
    val courseColor =
        Color(sectionInfo.colorNumber)

    val totalEnrolled =
        "${sectionInfo.totalEnrolled} matriculados"

    val totalClasses =
        "${sectionInfo.totalClasses} clases"

    val openDialog = rememberBoolean()

    val selectStatus = rememberBoolean()
    val infoStatus = rememberBoolean()

    val isPlaying = remember { mutableStateOf(false) }
    val currentChangeRepresentative = remember { mutableStateOf<TeacherStudentQueryModel?>(null) }
    val mediaPlayer = remember {
        MediaPlayer.create(
            context,
            R.raw.whatsapp_helper
        ).apply {
            setOnCompletionListener {
                isPlaying.value = false
            }
        }
    }
    SectionLayout(
        sectionId = sectionId,
        showState = infoStatus,
        contentBottom = {
            if (representativeStudentFullNameStream != null) {
                Text(
                    text = "Delegado de grupo",
                    color = myColors().primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                UserContactBox(user = representativeStudentFullNameStream)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        onClick = { selectStatus.value = true },
                        label = {
                            Text(text = "¿Cambiar delegado?")
                        },
                        selected = true,

                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.person),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }
        },
        primaryColor = courseColor
    ) {
        SearcherBox(
            list = studentsSection,
            showState = selectStatus,
            placeholder = "Seleccione un delegado",
            onSelected = {
                currentChangeRepresentative.value = it
            }
        ) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.surface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${it.fullName} - (${it.userName})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }

        if (currentChangeRepresentative.value != null) {
            Dialog(onDismissRequest = {
                currentChangeRepresentative.value = null
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .card(shadow = true)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "¿Desea cambiar al delegado de grupo?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (representativeStudentFullNameStream != null) {
                        Text(text = "Delegado actual", fontSize = 12.sp)
                        Text(text = representativeStudentFullNameStream.fullName, fontSize = 12.sp)
                    }

                    Text(text = "Nuevo delegado", fontSize = 12.sp)
                    Text(text = currentChangeRepresentative.value!!.fullName, fontSize = 12.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(onClick = {
                            currentChangeRepresentative.value = null
                        }) {
                            Text(text = "Cancelar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            viewModelStorage.teacherRepository.saveRepresentativeStudentAsync(
                                sectionId = sectionId,
                                studentUserId = currentChangeRepresentative.value!!.userId
                            )
                            currentChangeRepresentative.value = null
                        }) {
                            Text(text = "Aceptar")
                        }
                    }
                }
            }
        }



        if (openDialog.value) {
            val text = remember { mutableStateOf(sectionInfo?.externalGroupLink ?: "") }
            val validLink = remember { mutableStateOf(false) }

            validLink.value = text.value == "" || Utils.isValidWhatsAppGroupLink(text.value)

            Dialog(onDismissRequest = {
                openDialog.value = false
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .card(shadow = true)
                        .padding(16.dp),
                    //.padding(16.dp),
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
                        text = "Para mejorar la comunicación con los estudiantes, puedes agregar un enlace de grupo de Whatsapp. Los estudiantes podrán unirse al grupo desde la aplicación. Recuerde que el delegado tambien puede agregar el enlace de grupo de Whatsapp.",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surface
                    )
                    FilterChip(
                        selected = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.sound),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        onClick = {
                            if (isPlaying.value) {
                                mediaPlayer.pause()
                            } else {
                                mediaPlayer.seekTo(0)
                                mediaPlayer.start()
                            }
                            isPlaying.value = !isPlaying.value
                        },
                        label = {
                            Text(text = if (isPlaying.value) "Detener" else "Leer esto en voz alta")
                        })
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
                        TextButton(onClick = { openDialog.value = false }) {
                            Text(text = "Cancelar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            openDialog.value = false
                            viewModelStorage.teacherRepository.saveWhatsappLinkAsync(
                                sectionId = sectionId,
                                link = text.value
                            )
                        }, enabled = Utils.isValidWhatsAppGroupLink(text.value)) {
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            if (units.isNotEmpty()) {

                AlertCard(
                    title = "Publicación de notas",
                    body = "Ya puede publicar las notas de la UNIDAD #${units.toHumanString()} en la web de INTRANET",
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .card(courseColor)
                        .clickable {

                            if (representativeStudentFullNameStream != null) infoStatus.value = true
                            else selectStatus.value = true
                        }
                        .padding(16.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (representativeStudentFullNameStream != null) {
                        Text(
                            text = "Delg.: ${representativeStudentFullNameStream.fullName}",
                            fontSize = 12.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Sin delegado",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        Text(
                            text = "(click para registrar)",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 12.sp,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            color = Color.White

                        )
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .card(myColors().success)
                        .clickable {
                            openDialog.value = true

                        }
                        .padding(horizontal = 16.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    if (sectionInfo.externalGroupLink != null) {
                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = "Whatsapp",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Text(
                                text = "click nuevamente si el link no redirecciona",
                                fontSize = 10.sp,

                                color = Color.White,
                                lineHeight = 12.sp
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Sin Grupo de Whatsapp",
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                            Text(
                                text = "(click para registrar)",
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 12.sp,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }
                    }

                    if (sectionInfo.externalGroupLink != null) {
                        IconButtonSys(
                            onClick = {
                                Utils.openLink(
                                    context,
                                    sectionInfo.externalGroupLink
                                )

                            },
                            painter = painterResource(id = R.drawable.link),
                            color = Color.White
                        )
                    }
                }
            }

            HorizontalTextIcon(
                title = "Ver estudiantes matriculados",
                icon = painterResource(id = R.drawable.book),
                description = totalEnrolled,
                iconColor = courseColor,
                modifier = Modifier.fillMaxWidth(),
                isACard = true,
                onClick = {
                    navHostController.navigate("${NavigationApp.Teacher.SECTION_STUDENTS}/$sectionId")
                }
            )
            HorizontalTextIcon(
                title = "Ver calendario de clases",
                icon = painterResource(id = R.drawable.calendar_month),
                description = totalClasses,
                iconColor = courseColor,
                modifier = Modifier.fillMaxWidth(),
                isACard = true,
                onClick = {
                    navHostController.navigate("${NavigationApp.Teacher.CALENDAR_ASSISTANCE}/$sectionId")
                }
            )
            HorizontalTextIcon(
                title = "Asistencia de estudiantes",
                icon = painterResource(id = R.drawable.list_check),
                description = "Ver y marcar asistencia de estudiantes",
                iconColor = courseColor,
                modifier = Modifier.fillMaxWidth(),
                isACard = true,
                onClick = {
                    navHostController.navigate("${NavigationApp.Teacher.ASSISTANCE}/$sectionId")
                }
            )
        }
    }
}