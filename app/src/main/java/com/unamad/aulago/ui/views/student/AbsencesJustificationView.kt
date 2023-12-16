package com.unamad.aulago.ui.views.student

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.R
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.database.StudentAbsenceJustificationModel
import com.unamad.aulago.models.database.toStatusStudentAbsenceJustification
import com.unamad.aulago.toDateTime
import com.unamad.aulago.toHumanDate
import com.unamad.aulago.toHumanTime
import com.unamad.aulago.ui.components.IconButtonSys
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.layouts.rememberBoolean
import com.unamad.aulago.viewModelInstance

@Composable
fun AbsencesJustificationView(
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    viewModelStorage.studentRepository.loadAbsencesJustificationsAsync()
    AbsencesJustificationInitView()
}

@Composable
fun AbsencesJustificationInitView(
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    statusContent: MutableState<Boolean> = rememberBoolean()
) {
    val context = LocalContext.current
    val list =
        viewModelStorage.studentRepository.listStudentAbsenceJustificationStream.observeAsState(
            emptyList()
        ).value

    val currentJustification = remember {
        mutableStateOf<StudentAbsenceJustificationModel?>(null)
    }
    val isLoading =
        viewModelStorage.studentRepository.isLoadingAbsenceJustification.observeAsState(false).value
    val group = list.groupBy { it.sectionId }
    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Justificaciones",
            subtitle = "Solicitudes de justificación de inasistencias",
        ),
        stateContentBottom = statusContent,
        updateAction = {
            viewModelStorage.studentRepository.loadAbsencesJustificationsAsync()
        },
        loadingState = isLoading,
        contentBottom = {
            if (currentJustification.value != null) {
                val current = currentJustification.value!!
                val status = current.status.toStatusStudentAbsenceJustification()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Solitado el ${
                            current.date.toDateTime()?.toHumanDate()
                        } ${current.date.toDateTime()?.toHumanTime()}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-8).dp),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "F. A Justificar: ${current.classDate.toDateTime()?.toHumanDate()}",
                        color = MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = "ESTADO: ${status.description}",
                        color = status.color,

                        )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Detalle",
                        color = MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = current.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            for (item in group) {
                val first = item.value.firstOrNull()
                if (first != null) {
                    item {
                        Text(
                            text = first.course,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                }

                items(item.value) {
                    val itenStatus = it.status.toStatusStudentAbsenceJustification()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .card()
                            .padding(16.dp),
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Día para Justificar: ${
                                    it.classDate.toDateTime()?.toHumanDate()
                                }",
                                color = MaterialTheme.colorScheme.surface
                            )
                            Text(
                                text = "ESTADO: ${itenStatus.description}",
                                color = Color.White,
                                modifier = Modifier
                                    .card(itenStatus.color)
                                    .padding(horizontal = 16.dp, vertical = 2.dp)
                            )
                        }
                        IconButtonSys(onClick = {
                            currentJustification.value = it
                            statusContent.value = true
                        }, painter = painterResource(id = R.drawable.info))
                    }


                }
            }


        }
    }
}