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
) {
}

@Preview
@Composable
fun FromToDatePreview() {
    val dateStart: LocalDateTime = LocalDateTime.now()
    val dateEnd: LocalDateTime = LocalDateTime.now().plusDays(5)
    FromToDate(dateStart, dateEnd)
}