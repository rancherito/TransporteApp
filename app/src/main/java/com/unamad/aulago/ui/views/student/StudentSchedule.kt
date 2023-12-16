package com.unamad.aulago.ui.views.student

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.ui.components.schedule.ScheduleView
import com.unamad.aulago.viewModelInstance

@Composable
fun StudentSchedule(viewModelStorage: ViewModelStorage = viewModelInstance()) {
    val schedule =  viewModelStorage.listStudentScheduleLiveData.observeAsState(listOf()).value
    ScheduleView(schedule)
}
