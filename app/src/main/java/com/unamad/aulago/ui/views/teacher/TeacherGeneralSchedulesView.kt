package com.unamad.aulago.ui.views.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.database.SyllabusScheduleModel
import com.unamad.aulago.models.database.UnitsScheduleModel
import com.unamad.aulago.toDateTime
import com.unamad.aulago.ui.components.FromToDate
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import java.time.LocalDateTime

@Composable
fun TeacherGeneralSchedulesView(
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    viewModelStorage.generalRepository.loadGeneralSchedule()
    InternalView()
}

@Composable
private fun InternalView(
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    unitsSchedule: List<UnitsScheduleModel> = viewModelStorage.generalRepository.unitsScheduleStream.observeAsState(
        listOf()
    ).value,
    syllabusSchedule: SyllabusScheduleModel? = viewModelStorage.generalRepository.syllabusScheduleStream.observeAsState().value
) {

    val components = unitsSchedule.sortedBy { it.numberUnit }.groupBy { it.component }.toList()
    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Fechas",
            subtitle = "Fecha de publicación de notas y silabos",
        )
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                Text(
                    text = "Notas",
                    fontSize = 18.sp,
                    color = myColors().secondary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            item {

                Column(
                    modifier = Modifier
                        .background(color = myColors().card)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    components.forEach { component ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .card(Color.White)
                                .padding(16.dp)
                        ) {

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val nameUnit = "PARA CURSOS DE  ${component.first} UNIDADES"
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = nameUnit,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                component.second.forEach { unit ->

                                    val startDate = unit.startDate?.toDateTime()
                                    val endDate = unit.endDate?.toDateTime()
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {

                                        Text(text = "Unidad ${unit.numberUnit}:", fontSize = 12.sp)
                                        FromToDate(dateStart = startDate, dateEnd = endDate)
                                    }
                                }

                            }


                        }
                    }
                }
            }
            item {
                Text(
                    text = "Silabos",
                    fontSize = 18.sp,
                    color = myColors().secondary,
                    modifier = Modifier.padding(start = 16.dp)

                )
            }
            item {
                val startDate = syllabusSchedule?.startDate?.toDateTime()
                val endDate = syllabusSchedule?.endDate?.toDateTime()
                //val description = syllabusSchedule?.typeDescription ?: "-"
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .card()
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FromToDate(dateStart = startDate, dateEnd = endDate)
                }
            }
        }
    }
}

@Preview
@Composable
fun FromToDatePreview() {
    val dateStart: LocalDateTime = LocalDateTime.now()
    val dateEnd: LocalDateTime = LocalDateTime.now().plusDays(5)
    FromToDate(dateStart, dateEnd)
}