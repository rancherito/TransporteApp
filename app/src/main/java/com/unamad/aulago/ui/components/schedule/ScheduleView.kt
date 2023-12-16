package com.unamad.aulago.ui.components.schedule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.*
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.query.ScheduleCourseQueryModel
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.components.TextIcon
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.layouts.rememberBoolean
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val heightCourseBox = 60.dp

@Composable
fun ScheduleView(
    schedule: List<ScheduleCourseQueryModel>
) {

    val showInfoCourse = remember {
        mutableStateOf<ScheduleCourseQueryModel?>(null)
    }
    ScheduleUi(
        schedule = schedule,
        showInfoCourse = showInfoCourse
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScheduleUi(
    schedule: List<ScheduleCourseQueryModel>,
    showInfoCourse: MutableState<ScheduleCourseQueryModel?>,
    scope: CoroutineScope = rememberCoroutineScope()
) {

    var maxDayOfWeek = schedule.filter { it.weekDay != null }.maxOfOrNull { it.weekDay!! } ?: 4
    maxDayOfWeek = if (maxDayOfWeek < 5) 4 else maxDayOfWeek

    var listTimes = mutableListOf<Int>()
    schedule.filter { it.weekDay != null }.forEach {
        listTimes.addAll(it.startTime!!..it.endTime!!)
    }


    listTimes = listTimes.distinct().sorted().toMutableList()

    while (listTimes.size <= 7 && (listTimes.minOrNull() ?: 8) > 7)
        listTimes.add(0, (listTimes.minOrNull() ?: 8) - 1)

    while (listTimes.size <= 7 && (listTimes.maxOrNull() ?: 20) < 21)
        listTimes.add((listTimes.maxOrNull() ?: 20) + 1)


    val courseDays = schedule.filter { it.weekDay != null }.map {
        CourseDay(it.sectionId, it.sectionCode, it.weekDay!!)
    }.distinct()
    //Filter courses without schedule
    val courseIsolated = schedule.filter { it.weekDay == null }


    val tabs = (0..maxDayOfWeek).map {
        Utils.days[it].substring(0, 2)
    }


    val pagerState: PagerState = rememberPagerState(
        initialPage = Utils.currentDay,
        initialPageOffsetFraction = 0f
    ) {
        tabs.size
    }
    val showModal = rememberBoolean()
    //UI
    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Horario académico",
            Utils.days[pagerState.currentPage]
        ),
        stateContentBottom = showModal,
        contentBottom = {
            ScheduleInfoCourseBox(
                course = showInfoCourse.value,
                courseDays = courseDays.filter { showInfoCourse.value?.sectionId == it.sectionId && showInfoCourse.value?.sectionCode == it.sectionCode }
            )
        }
    ) {


        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        contentColor = Color.Transparent,
                        containerColor = Color.Transparent,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, dayText ->

                            Tab(
                                text = {
                                    Text(
                                        text = dayText,
                                        color = myColors().secondary
                                    )
                                },
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) { page ->

                        //Filter courses selected by user or  by default current day
                        val coursesOfSelectedDay =
                            schedule.filter { it.weekDay != null && it.weekDay == page }
                                .sortedBy { it.startTime }

                        val courses = mutableListOf<TimeCourse>()


                        //grouping normal and cross courses
                        listTimes.forEachIndexed { index, time ->
                            courses.add(
                                TimeCourse(
                                    size = 1,
                                    list = coursesOfSelectedDay.filter { time >= it.startTime!! && time < if (it.endTime!! == 0) 24 else it.endTime }
                                        .toMutableList()
                                )
                            )
                            if (index != 0) {
                                if (courses[index - 1].list == courses[index].list) {
                                    courses[index].size += courses[index - 1].size
                                    courses[index - 1].size = 0
                                    courses[index - 1].list = mutableListOf()

                                }
                            }
                        }

                        if (coursesOfSelectedDay.isNotEmpty())
                            DayCoursesBox(
                                modifier = Modifier.weight(1f),
                                listTimes = listTimes,
                                courses = courses,
                            ){
                                showInfoCourse.value = it
                                showModal.value = true
                            }
                        else
                            EmptyCard(
                                modifier = Modifier.weight(1f),
                                title = "${Utils.days[page]}, DÍA LIBRE"
                            )


                    }
                }



                if (courseIsolated.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = myColors().textDark)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Cursos sin horario",
                            fontSize = 12.sp,
                            lineHeight = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        courseIsolated.forEach { course ->
                            TextIcon(
                                text = course.courseName,
                                painter = painterResource(id = R.drawable.book)
                            )
                        }

                    }
                }
            }
        }
    }
}

data class CourseDay(val sectionId: String, val sectionCode: String, val weekDay: Int)

@Composable
fun DayCoursesBox(
    modifier: Modifier = Modifier,
    listTimes: MutableList<Int>,
    courses: MutableList<TimeCourse>,
    onSelectCourse: (ScheduleCourseQueryModel) -> Unit = {}
) {
    val coroutine = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .height(heightCourseBox * listTimes.size)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column {
                    listTimes.forEach {
                        Text(
                            text = it.toHour().toString(),
                            fontSize = 11.sp,
                            color = myColors().body,
                            modifier = Modifier
                                .height(
                                    heightCourseBox
                                )
                                .offset(0.dp, (-4).dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .height((heightCourseBox * (listTimes.size - 1)) + 8.dp)
                        .width(1.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    courses.forEach {

                        Row(
                            modifier = Modifier
                                .height(heightCourseBox * it.size)
                                .fillMaxWidth()
                        ) {
                            it.list.forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp)
                                ) {
                                    ScheduleCourseBox(
                                        course = item
                                    ) { selected ->
                                        onSelectCourse(selected)

                                    }
                                }
                            }
                        }

                    }
                }

            }

        }
    }
}

class TimeCourse(
    var size: Int,
    var list: MutableList<ScheduleCourseQueryModel>
)