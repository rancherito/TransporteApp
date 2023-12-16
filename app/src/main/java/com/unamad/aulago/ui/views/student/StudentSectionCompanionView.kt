package com.unamad.aulago.ui.views.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.Utils
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.emums.TextIconType
import com.unamad.aulago.models.query.StudentSectionQueryModel
import com.unamad.aulago.ui.components.FloatingIconButton
import com.unamad.aulago.ui.components.TextIcon
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R
import kotlinx.coroutines.launch

@Composable
fun StudentSectionCompanionView(
    sectionId: String,
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    val coroutine = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val section =
        viewModelStorage.studentRepository.listStudentSectionInfoStream.observeAsState(listOf()).value.filter { it.sectionId == sectionId }
    val course = section.firstOrNull()



    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Compañeros",
            subtitle = course?.courseName ?: "Cargando",
            color = if (course == null) MaterialTheme.colorScheme.primary else Color(course.colorNumber)
        )
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize(),
            state = listState
        ) {
            itemsIndexed(section.sortedBy { it.studentFullName }) { index, student ->
                CompanionBox(index = index + 1, student = student)
            }
            item {
                Spacer(modifier = Modifier.height(42.dp))
            }
        }

        FloatingIconButton(
            icon = painterResource(id = R.drawable.arrow_up),
            modifier = Modifier.align(Alignment.BottomEnd).offset((-16).dp, (-16).dp)
        ) {
            coroutine.launch {
                listState.animateScrollToItem(0)
            }
        }
    }
}


@Composable
private fun CompanionBox(
    student: StudentSectionQueryModel,
    index: Int,
    isViewNumber: Boolean = true
) {

    val prefixNumber = Utils.PREFIX_PERUVIAN
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isViewNumber) {
            Box(
                modifier = Modifier
                    .card(myColors().card)
                    .fillMaxHeight()
                    .width(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = index.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            }
        }
        Column(
            Modifier
                .card(myColors().card)
                .padding(8.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TextIcon(
                text = student.studentFullName,
                painter = painterResource(id = R.drawable.person), isCopyable = true
            )
            TextIcon(
                text = if (student.phoneNumber == null) "Sin registro" else "${prefixNumber}${student.phoneNumber}",
                painter = painterResource(id = R.drawable.phone),
                isCopyable = true,
                textIconType = TextIconType.PhoneNumber
            )
        }
    }
}
