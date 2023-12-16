package com.unamad.aulago.ui.views.teacher

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.VectorDrawable
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unamad.aulago.*
import com.unamad.aulago.classes.CalendarDimensions
import com.unamad.aulago.classes.HeaderInformation
import com.unamad.aulago.models.database.TeacherClassModel
import com.unamad.aulago.models.query.SimpleCourseSectionQueryModel
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.layouts.CommonLayout
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.temporal.TemporalAdjusters
import java.util.UUID

@Composable
fun TeacherCalendarAssistanceUi(
    sectionId: String,
    navController: NavHostController = navControllerInstance(),
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    teacherClassesAssistance: List<TeacherClassModel> =
        viewModelStorage.listClassDatesSection.observeAsState(listOf()).value,
    sectionInfo: SimpleCourseSectionQueryModel? =
        viewModelStorage.teacherRepository.listTeacherCoursesStream.observeAsState(listOf()).value.find { it.sectionId == sectionId }
) {
    viewModelStorage.loadTeacherClasses()

    val years = teacherClassesAssistance.sortedBy { LocalDateTime.parse(it.startTime) }.map {
        val date = LocalDateTime.parse(it.startTime)
        MonthInfo(year = date.year, month = date.month)
    }.distinct().groupBy { it.year }
    //UI DESIGN
    CommonLayout(
        headerInformation = HeaderInformation(
            title = "Calendario de clases",
            subtitle = if (sectionInfo != null) "${sectionInfo.sectionCode} ${sectionInfo.courseName}" else "Cargando...",
            indicator = "${sectionInfo?.careerName}",
            color = if (sectionInfo?.colorNumber == null) null else Color(sectionInfo.colorNumber)
        )
    ) {
        if (sectionInfo != null) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .padding(32.dp + 18.dp, 4.dp, 16.dp, 4.dp)
                ) {
                    Utils.days.forEach { day ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = myColors().card,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.substring(0, 1),
                                color = Color(sectionInfo.colorNumber)
                            )
                        }
                    }
                }
                Divider()
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.weight(1f)
                ) {

                    years.forEach { year ->

                        //group by months
                        val months = (year.value.groupBy { it.month }).toList()

                        items(months) { it ->
                            val month = it.first.value
                            //filter teacherClassesAssistance of current month
                            val teacherClassesAssistanceMonth =
                                teacherClassesAssistance.filter { teacherClass ->
                                    val date = LocalDateTime.parse(teacherClass.startTime)
                                    date.year == year.key && date.month.value == month
                                }.map {
                                    //return DayInfoCalendar
                                    val date = LocalDateTime.parse(it.startTime)
                                    DayInfoCalendar(
                                        year = date.year,
                                        month = date.month,
                                        day = date.dayOfMonth,
                                        isChecked = it.isDictated,
                                        id = it.id
                                    )
                                }

                            ClassesCalendar(
                                year.key,
                                month,
                                teacherClassesAssistanceMonth
                            ) { id ->
                                navController.navigate("${NavigationApp.Teacher.ASSISTANCE}/$sectionId?classId=$id")
                            }
                        }
                    }
                }
            }
        } else {
            EmptyCard()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TestMatrix() {
    val context = LocalContext.current
    val now = LocalDateTime.now()
    //make 5 days actives
    val list = mutableListOf<DayInfoCalendar>()
    for (i in 0..5) {
        list.add(
            DayInfoCalendar(
                year = now.year,
                month = now.month,
                day = now.dayOfMonth + i,
                isChecked = true
            )
        )
    }


    ClassesCalendar(year = now.year, month = now.monthValue, days = list) {
        Toast.makeText(context, "Círculo $it", Toast.LENGTH_SHORT).show()
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun ClassesCalendar(
    year: Int = LocalDateTime.now().year,
    month: Int = LocalDateTime.now().monthValue,
    days: List<DayInfoCalendar> = listOf(),
    spacing: Dp = 4.dp, onRectangleClick: ((String) -> Unit)? = null
) {
    val bgCell = Color(0xFFEDEDED)
    val holidayText = Color(0xFFB81737)

    val bgCellActive = myColors().success
    val textCell = Color.Black
    val now = LocalDateTime.now()
    val context = LocalContext.current
    val strokeColor = MaterialTheme.colorScheme.primary
    val otherDaysColor = myColors().background
    val heightBox = 30.dp
    val spacingPx = with(LocalDensity.current) { spacing.toPx() }
    val rectHeight = with(LocalDensity.current) { heightBox.toPx() }

    val calendarGrid = calculateCalendarRowsAndColumns(year, month)
    val rows = calendarGrid.rows
    val columns = calendarGrid.columns
    val maxHeightCanvas = (heightBox + spacing) * rows - spacing

    val iconSize = with(LocalDensity.current) { 12.dp.toPx() }


    val myImageBitmap = vecToBitmap(id = R.drawable.lock, height = iconSize)

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .vertical()
                .rotate(-90f)
                .width(maxHeightCanvas)
                .height(32.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "$year - ${Utils.months[month - 1]}",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.surface
                //color = if (firstDay.monthValue == LocalDateTime.now().monthValue) MaterialTheme.colorScheme.primary else myColors().blackAlt
            )
        }

        Canvas(
            modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth()
                .height(maxHeightCanvas)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        var initialCounter = 0

                        val rectWidth: Float = (size.width - spacingPx * (columns - 1)) / columns

                        for (i in 0 until rows) {
                            for (j in 0 until columns) {
                                val topLeftX: Float = j * (rectWidth + spacingPx)
                                val topLeftY = i * (rectHeight + spacingPx)
                                if (i == 0 && j < calendarGrid.initColumn) continue
                                initialCounter++
                                if (initialCounter > calendarGrid.days) return@detectTapGestures
                                val dayClass = days.find { it.day == initialCounter }

                                val currentDate =
                                    LocalDateTime.of(year, month, initialCounter, 0, 0)
                                val dayDiff = currentDate.dayOfYear - now.dayOfYear

                                if (initialCounter > 0) {
                                    val rectangle = Rect(
                                        offset = Offset(topLeftX, topLeftY),
                                        size = androidx.compose.ui.geometry.Size(
                                            rectWidth,
                                            rectHeight
                                        )
                                    )
                                    if (rectangle.contains(offset)) {
                                        if (onRectangleClick == null || dayClass == null) return@detectTapGestures

                                        if (currentDate.isAfter(now)) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Aún faltan $dayDiff días",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                            return@detectTapGestures
                                        }
                                        onRectangleClick(
                                            dayClass.id
                                        )
                                        return@detectTapGestures
                                    }
                                }

                            }
                        }
                    }
                },
        ) {

            val rectWidth = (size.width - spacingPx * (columns - 1)) / columns
            var initialCounter = 0

            for (i in 0 until rows) {
                for (j in 0 until columns) {
                    val topLeftX = j * (rectWidth + spacingPx)
                    val topLeftY = i * (rectHeight + spacingPx)

                    if (i == 0 && j < calendarGrid.initColumn) continue
                    initialCounter++


                    if (initialCounter > calendarGrid.days) {
                        return@Canvas
                    }
                    val currentDate = LocalDateTime.of(year, month, initialCounter, 0, 0)

                    if (initialCounter > 0) {

                        val isNow =
                            initialCounter == now.dayOfMonth && month == now.monthValue && year == now.year


                        var color = otherDaysColor
                        val dayClass = days.find { it.day == initialCounter }
                        if (dayClass != null) {
                            color = if (dayClass.isChecked) bgCellActive else bgCell
                        }

                        var colorText = if (currentDate.dayOfWeek.value == 7) holidayText else textCell

                        colorText = if (dayClass?.isChecked == true) otherDaysColor else colorText

                        drawRoundRect(
                            color = color,
                            topLeft = Offset(topLeftX, topLeftY),
                            size = androidx.compose.ui.geometry.Size(rectWidth, rectHeight),
                            cornerRadius = CornerRadius(12f, 12f)
                        )


                        if (isNow)
                            drawRoundRect(
                                color = strokeColor,
                                topLeft = Offset(topLeftX, topLeftY),
                                size = androidx.compose.ui.geometry.Size(rectWidth, rectHeight),
                                cornerRadius = CornerRadius(12f, 12f),
                                style = Stroke(width = 2.dp.toPx())
                            )

                        drawIntoCanvas { canvas ->
                            val paint = Paint()
                            paint.color = colorText.toArgb()
                            paint.textSize = 14.dp.toPx()
                            paint.textAlign = Paint.Align.CENTER

                            val x = topLeftX + rectWidth / 2
                            val y =
                                topLeftY + rectHeight / 2 - (paint.fontMetrics.ascent + paint.fontMetrics.descent) / 2

                            canvas.nativeCanvas.drawText(initialCounter.toString(), x, y, paint)
                        }

                        if (currentDate.isAfter(now) && dayClass != null){

                            val x = topLeftX + 5
                            val y = topLeftY + 5

                            drawImage(
                                image = myImageBitmap.image,
                                topLeft = Offset(x, y),
                                alpha = 0.4f
                            )
                        }

                    }

                }
            }
        }
    }
}

fun calculateCalendarRowsAndColumns(year: Int, month: Int): CalendarDimensions {
    val date = LocalDate.of(year, month, 1)
    val firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth())
    val lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth())

    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7 // Monday = 0, Tuesday = 1, etc.
    val lastDayOfWeek = (lastDayOfMonth.dayOfWeek.value + 6) % 7
    val daysInMonth = lastDayOfMonth.dayOfMonth

    val daysAfterLast = 6 - lastDayOfWeek
    val totalDays = firstDayOfWeek + daysInMonth + daysAfterLast

    val rows = (totalDays + 6) / 7  // Rounding up to the next integer
    val columns = 7

    return CalendarDimensions(columns, rows, firstDayOfWeek, days = daysInMonth)
}

fun vectorToImageBitmap(vectorDrawable: VectorDrawable, width: Int, height: Int): ImageBitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return bitmap.asImageBitmap()
}

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun vecToBitmap(
    @DrawableRes id: Int,
    width: Float? = null,
    height: Float? = null
): VecToBitmapInfo {
    val context = LocalContext.current
    val vectorDrawable = context.resources.getDrawable(id, null) as VectorDrawable
    val originalWidth = vectorDrawable.intrinsicWidth
    val originalHeight = vectorDrawable.intrinsicHeight

    val finalWidth: Int
    val finalHeight: Int

    when {
        width != null && height != null -> {
            finalWidth = width.toInt()
            finalHeight = height.toInt()
        }

        width != null -> {
            finalWidth = width.toInt()
            finalHeight = (width.toFloat() / originalWidth * originalHeight).toInt()
        }

        height != null -> {
            finalHeight = height.toInt()
            finalWidth = (height.toFloat() / originalHeight * originalWidth).toInt()
        }

        else -> {
            finalWidth = originalWidth
            finalHeight = originalHeight
        }
    }

    val image = vectorToImageBitmap(vectorDrawable, finalWidth, finalHeight)
    return  VecToBitmapInfo(
        width = finalWidth.toFloat(),
        height = finalHeight.toFloat(),
        image = image
    )
}

data class MonthInfo(
    val year: Int,
    val month: Month,
)
data class VecToBitmapInfo(
    val width: Float,
    val height: Float,
    val image: ImageBitmap
)
data class DayInfoCalendar(
    val year: Int,
    val month: Month,
    val day: Int,
    val isChecked: Boolean = false,
    val id: String = UUID.randomUUID().toString()
)