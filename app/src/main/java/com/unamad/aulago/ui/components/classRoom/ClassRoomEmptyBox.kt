package com.unamad.aulago.ui.components.classRoom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.unamad.aulago.models.query.ScheduleCourseQueryModel
import com.unamad.aulago.toHighDarkness
import com.unamad.aulago.ui.components.TextIcon
import com.unamad.aulago.R

@Composable
fun ClassRoomEmptyBox(withoutBuilding: List<ScheduleCourseQueryModel>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.building),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(16.dp)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "SIN ASIGNAR AULA",
            color = MaterialTheme.colorScheme.primary
        )
    }
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            withoutBuilding.forEach { course ->
                TextIcon(
                    text = course.courseName,
                    painter = painterResource(id = R.drawable.book),
                    tint = Color(course.colorNumber),
                    textColor = Color(course.colorNumber).toHighDarkness()
                )
            }
        }
    }
}