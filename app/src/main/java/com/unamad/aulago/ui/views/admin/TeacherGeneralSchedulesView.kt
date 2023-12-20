package com.unamad.aulago.ui.views.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.unamad.aulago.ui.components.FromToDate
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