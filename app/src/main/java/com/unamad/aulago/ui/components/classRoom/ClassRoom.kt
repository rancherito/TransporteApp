package com.unamad.aulago.ui.components.classRoom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.query.ScheduleCourseQueryModel
import com.unamad.aulago.ui.layouts.CommonLayout

@Composable
fun ClassRoom(schedule: List<ScheduleCourseQueryModel>) {

    val buildings = schedule.filter { it.buildingName != null }.groupBy { it.buildingName!! }
    val withoutBuilding = schedule.filter { it.buildingName == null }

    CommonLayout(headerInformation = HeaderInformation(title = "Aulas", subtitle = "Pabellones, aulas y cursos")) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            buildings.forEach { building ->
                item {
                    ClassRoomBox(building.value)
                }
                if (withoutBuilding.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.padding(4.dp))
                        ClassRoomEmptyBox(withoutBuilding)
                    }
                }
            }
        }
    }
}
