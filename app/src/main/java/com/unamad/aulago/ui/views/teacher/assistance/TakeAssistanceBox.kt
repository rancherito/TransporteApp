package com.unamad.aulago.ui.views.teacher.assistance

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.*
import com.unamad.aulago.emums.IsLoadData
import com.unamad.aulago.models.apiModels.AssistanceStudentApiModel
import com.unamad.aulago.models.database.TeacherClassModel
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.components.FloatingTextButton
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R
import java.time.LocalDateTime

@SuppressLint("UnrememberedMutableState")
@Composable
fun TakeAssistanceBox(
    teacherClassModel: TeacherClassModel,
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    isSavingStream: State<Boolean> = viewModelStorage.teacherRepository.isSavingAssistance.observeAsState(
        false
    )
) {
    val now =
        LocalDateTime.parse(teacherClassModel.startTime)
    val isLoadingStatus: IsLoadData? =
        viewModelStorage.teacherRepository.assistanceStudentIsLoadData.observeAsState(IsLoadData.Initializing).value
    val listStudent = viewModelStorage.teacherRepository.assistanceData.observeAsState(listOf())

    Crossfade(
        targetState = isLoadingStatus != IsLoadData.Finish,
        label = ""
    ) { stateLoad ->
        if (stateLoad)
            EmptyCard(title = "Los datos aún están cargando")
        else {
            val advanceAssistance =
                listStudent.value.count { it.isAbsent !== null }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(24.dp, 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "F",
                            modifier = Modifier.width(32.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Text(
                            text = "A",
                            modifier = Modifier.width(32.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.surface
                        )

                    }
                    Divider(thickness = 0.5.dp)
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 64.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        item {
                            Row(
                                modifier = Modifier
                                    .padding(8.dp, 0.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "",
                                    maxLines = 2,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f)
                                )

                                Checkbox(
                                    modifier = Modifier.size(32.dp),
                                    checked = listStudent.value.all { it.isAbsent == true },
                                    onCheckedChange = { bb ->
                                        viewModelStorage.teacherRepository.assistanceData.postValue(
                                            listStudent.value.map { ff ->
                                                ff.copy(isAbsent = if (bb) true else null)
                                            }.toList()
                                        )

                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = myColors().danger,
                                        uncheckedColor = myColors().danger
                                    )
                                )
                                Checkbox(
                                    modifier = Modifier.size(32.dp),
                                    checked = listStudent.value.all { it.isAbsent == false },
                                    onCheckedChange = { bb ->
                                        viewModelStorage.teacherRepository.assistanceData.postValue(
                                            listStudent.value.map { ff ->
                                                ff.copy(isAbsent = if (bb) false else null)
                                            }.toList()
                                        )

                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = myColors().success,
                                        uncheckedColor = myColors().success
                                    )
                                )
                            }
                        }
                        itemsIndexed(listStudent.value) { index, dd ->
                            StudentAssistanceBox(index, listStudent.value[index]) {

                                val lll = listStudent.value.mapIndexed { i, ff ->
                                    if (i == index)  ff.copy(isAbsent = it)
                                    else ff.copy()
                                }.toList()

                                viewModelStorage.teacherRepository.assistanceData.postValue(lll)
                            }
                        }
                    }


                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .align(Alignment.BottomCenter)
                        .pointerInput(Unit) {
                            this.detectTapGestures {

                            }
                        },
                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    val advance =
                        if (advanceAssistance == listStudent.value.size) "OK" else "${advanceAssistance}/${listStudent.value.size}"
                    if (advanceAssistance != listStudent.value.size)
                        FloatingTextButton(
                            text = advance,
                            textColor = myColors().body,
                            color = myColors().background,
                            onClick = {}
                        )
                    FloatingTextButton(
                        text = now.toHumanDate(),
                        modifier = Modifier.weight(1f),
                        textColor = myColors().body,
                        color = myColors().background,
                        onClick = {}
                    )
                    FloatingTextButton(
                        icon = painterResource(id = R.drawable.save),
                        text = "Guardar",
                        enabled = listStudent.value.isNotEmpty() && listStudent.value.all { it.isAbsent != null } && !isSavingStream.value
                    ) {
                        viewModelStorage.teacherRepository.saveTeacherAssistanceList()
                    }
                }

            }
        }
    }
}


@Composable
private fun StudentAssistanceBox(
    index: Int,
    student: AssistanceStudentApiModel,
    out: (state: Boolean?) -> Unit
) {
    val colorState = when (student.isAbsent) {
        true -> myColors().danger
        false -> myColors().success
        else -> myColors().card
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Box(
            modifier = Modifier
                .background(
                    color = colorState,
                    shape = RoundedCornerShape(4.dp)
                )
                .fillMaxHeight()
                .width(8.dp)
        )
        Row(
            modifier = Modifier
                .card()
                .padding(8.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${index + 1}) ${student.fullName}",
                maxLines = 2,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.surface
            )
            Checkbox(
                modifier = Modifier.size(32.dp),
                checked = student.isAbsent == true,
                onCheckedChange = {
                    if (it) out(true)
                    else out(null)

                },
                colors = CheckboxDefaults.colors(
                    checkedColor = myColors().danger,
                    uncheckedColor = myColors().danger,
                    checkmarkColor = Color.White
                )
            )
            Checkbox(
                modifier = Modifier.size(32.dp),
                checked = student.isAbsent == false,
                onCheckedChange = {
                    if (it) out(false)
                    else out(null)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = myColors().success,
                    uncheckedColor = myColors().success,
                    checkmarkColor = Color.White

                )
            )
            /*Checkbox(
                modifier = Modifier.size(32.dp),
                checked = student.isAbsent == false,
                onCheckedChange = {
                    if (it) out(false)
                    else out(null)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = ColorCustom.Alert,
                    uncheckedColor = ColorCustom.Alert
                ),
                enabled = false
            )*/
        }
    }
}