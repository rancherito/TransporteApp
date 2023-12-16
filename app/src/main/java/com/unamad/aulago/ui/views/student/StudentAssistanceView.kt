package com.unamad.aulago.ui.views.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.MaterialColorsList
import com.unamad.aulago.R
import com.unamad.aulago.Utils.Companion.toJSON
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.query.StudentAssistanceQuery
import com.unamad.aulago.ui.components.IconButtonSys
import com.unamad.aulago.ui.components.AlertCard
import com.unamad.aulago.ui.components.AlertType
import com.unamad.aulago.ui.components.IconSquare
import com.unamad.aulago.ui.components.LoadingBox
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.layouts.rememberBoolean
import com.unamad.aulago.viewModelInstance

@Composable
fun StudentAssistanceView(
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    assistance: List<StudentAssistanceQuery> = viewModelStorage.studentRepository.studentAssistanceStream.observeAsState(
        emptyList()
    ).value,
    isLoadingAssistance: State<Boolean> = viewModelStorage.studentRepository.isLoadingAssistance.observeAsState(
        false
    )
) {
    val status = rememberBoolean()
    val currentAssistanceQuery = remember {
        mutableStateOf<StudentAssistanceQuery?>(null)
    }
    val isExceededAbsences = assistance.any { it.maxAbsences <= it.absences }

    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Asistencia",
            subtitle = "Aqui podras ver tu asistencia",
            indicator = "Exceder el 30% de faltas en una materia es motivo de desaprobación con nota igual a 0.00*"
        ),
        stateContentBottom = status,
        contentBottom = {
            currentAssistanceQuery.value?.let {

                if (isLoadingAssistance.value) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    ) {
                        LoadingBox(color = MaterialTheme.colorScheme.primary)
                        Text(
                            text = "Cargando...",
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconSquare(
                            painter = painterResource(id = R.drawable.duotone_layers),
                            color = Color(it.colorNumber),
                        )


                        Text(
                            text = it.course,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(it.colorNumber),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Total clases del semestre: ${it.total}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconSquare(
                                    text = "${it.dictated}",
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                Text(
                                    text = "Dictadas",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.surface
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconSquare(
                                    text = "${it.assisted}",
                                    color = MaterialColorsList.lightGreen,
                                )
                                Text(
                                    text = "Asistidas",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.surface
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconSquare(
                                    text = "${it.absences}",
                                    color = MaterialColorsList.red,
                                )
                                Text(
                                    text = "Faltas",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.surface
                                )
                            }
                        }
                        Text(
                            text = "Recuerde no superar las ${it.maxAbsences} faltas en este curso que representan aproximadamente el 30% de las ${it.total} clases totales",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = .5f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        AssistChip(
                            onClick = {
                                viewModelStorage.studentRepository.loadAssistanceStudentAsync()
                            },
                            label = { Text(text = "Actualizar") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.update),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            })
                    }
                }
            }
        }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = androidx.compose.ui.Modifier.padding(16.dp)
        ) {
            if (isExceededAbsences) {
                item {
                    AlertCard(
                        title = "Exceso de faltas",
                        body = "Ha excedido el número máximo de faltas en una o más materias. Por favor, comuníquese con su docente para discutir posibles soluciones.",
                        alertType = AlertType.Danger
                    )
                }
            }
            item(

            ) {
                //una leyeenda con 2 barras que representan que el verde es asistencias y el rojo ausencias
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "% FALTAS",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(16.dp)
                                .card(MaterialColorsList.red)
                        )

                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "% ASISTENCIAS",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(16.dp)
                                .card(MaterialColorsList.lightGreen)
                        )

                    }
                }

            }
            items(assistance) {

                AssistanceCard(info = it) {
                    status.value = true
                    currentAssistanceQuery.value = it
                }
            }
        }
    }
}

@Preview
@Composable
fun AssistanceCardPreview(
) {
    AssistanceCard(
        info = StudentAssistanceQuery(
            course = "Curso de prueba",
            colorNumber = Color.Red.toArgb().toLong(),
            assisted = 10,
            absences = 5,
            total = 36,
            dictated = 10,
            maxAbsences = 10,
            code = "123456"
        )
    )
}


@Composable
fun AssistanceCard(
    info: StudentAssistanceQuery,
    callback: StudentAssistanceQuery.() -> Unit = {}
) {
    val colorCourse = Color(info.colorNumber)
    val percentAssisted = String.format("%.1f", (info.assisted * 1f) / info.total).toFloat()
    val percentAssistedCentennial =
        String.format("%.1f", (info.assisted * 100f) / info.total).toFloat()

    val percentAbsences = String.format("%.1f", (info.absences * 1f) / info.total).toFloat()
    val percentAbsencesCentennial =
        String.format("%.1f", (info.absences * 100f) / info.total).toFloat()

    val isExceededAbsences = info.maxAbsences <= info.absences

    val colorCard =
        if (isExceededAbsences) MaterialColorsList.red.copy(.1f) else MaterialTheme.colorScheme.surfaceVariant

    Row(
        modifier = Modifier
            .card(colorCard)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = info.course,
                fontSize = 11.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.surface
            )
            //si las clases dictadas son 0, que muestre el texto de que no hay clases dictadas
            Text(
                text = if (info.dictated > 0) "Dictadas ${info.dictated} de ${info.total}" else "No hay clases dictadas",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        if (info.dictated > 0) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${percentAbsencesCentennial}%",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.surface
                )
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(10.dp)
                        .card(Color.Black.copy(alpha = 0.05f)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(percentAbsences)
                            .fillMaxWidth()
                            .card(MaterialColorsList.red)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${percentAssistedCentennial}%",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.surface
                )
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(10.dp)
                        .card(Color.Black.copy(alpha = 0.05f)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(percentAssisted)
                            .fillMaxWidth()
                            .card(MaterialColorsList.lightGreen)
                    )
                }
            }
        }
        IconButtonSys(
            onClick = {
                callback(info)
            },
            painter = painterResource(id = R.drawable.info),
            color = colorCourse
        )
    }
}