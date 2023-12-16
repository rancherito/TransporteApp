package com.unamad.aulago.ui.views.teacher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.ui.components.classRoom.ClassRoom
import com.unamad.aulago.viewModelInstance


@Composable
fun TeacherClassRoomUi(
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    val schedule = viewModelStorage.teacherRepository.listTeacherScheduleStream.observeAsState(listOf()).value
    ClassRoom(schedule)
}