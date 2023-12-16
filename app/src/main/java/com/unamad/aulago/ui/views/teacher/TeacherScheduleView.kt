package com.unamad.aulago.ui.views.teacher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.ui.components.schedule.ScheduleView
import com.unamad.aulago.viewModelInstance

@Composable
fun TeacherScheduleView(viewModelStorage: ViewModelStorage = viewModelInstance()) {
    val schedule =  viewModelStorage.teacherRepository.listTeacherScheduleStream.observeAsState(listOf()).value
    ScheduleView(schedule)
}