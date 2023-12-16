package com.unamad.aulago

import androidx.compose.ui.graphics.Color
import com.unamad.aulago.emums.ServerAddressType

object NavigationApp {
    object Teacher {
        const val CALENDAR_ASSISTANCE = "TeacherCalendarAssistance"
        const val SECTION_STUDENTS = "TeacherStudent"
        const val ACADEMIC_CHARGE = "TeacherAcademicCharge"
        const val CLASS_SCHEDULE = "TeacherClassSchedule"
        const val DASHBOARD = "TeacherDashboard"
        const val CLASSROOM = "TeacherClassRoom"
        const val GENERAL_SCHEDULE = "TeacherGeneralSchedule"
        const val ASSISTANCE = "TeacherAssistance"
        const val SECTION = "TeacherCourse"
    }

    object Student {
        const val JUSTIFICATIONS_ABSENCES = "StudentJustificationsAbsences"
        const val ASSISTANCE = "StudentAssistance"
        const val DEBTS = "StudentDebts"
        const val ACTIVITIES = "StudentActivities"
        const val DASHBOARD = "StudentDashboard"
        const val SCHEDULE = "StudentSchedule"
        const val TEACHERS = "StudentTeachers"
        const val COMPANIONS = "StudentCompanions"
        const val CLASSROOM = "StudentClassRoom"
        const val REPRESENTATIVE_MANAGEMENT = "StudentRepresentativeManagement"
    }

    object General {
        const val SUPPORT = "Support"
    }

}

class Roles {
    companion object {
        const val Student = "Alumnos"
        const val Teacher = "Docentes"
    }
}
enum class SystemKeys(name: String) {
    STUDENT_INFO("student_info"),
    TEACHER_INFO("teacher_info")
}
data class ColorSchemeCustom(
    //Status color
    val success: Color = Color(0xFF4CAF50),
    val alert: Color = Color(0xFFFF9800),
    val danger: Color = Color(0xFFFF1744),

    //Common colors
    val black: Color = Color(0xFF0E1111),
    val blackAlt: Color = Color(0xFF3A4349),
    val gray200: Color = Color(0xFF858585),
    val gray100: Color = Color(0xFFDCDCDC),
    val gray50: Color = Color(0xfff1f1f1),

    //System colors
    val primary: Color =
        if (SystemConfig.serverAddress == ServerAddressType.Production)
            Color(0xFFf53160)
        else Color(0xFF461897),

    val secondary: Color = if (SystemConfig.serverAddress == ServerAddressType.Production) Color(
        0xFFCE4666
    ) else Color(0xFF461897),
    val textDark: Color = Color.White,
    val card: Color = Color(0xfff7f7f9),
    val background: Color = Color.White,
    val body: Color = Color(0xFF282929),
    val bodyAlt: Color = Color(0xFF3A3A3A),
    val text: Color = Color(0xFF212424),

    val red: Color = MaterialColorsList.red,
    val pink: Color = MaterialColorsList.pink,
    val purple: Color = MaterialColorsList.purple,
    val deepPurple: Color = MaterialColorsList.deepPurple,
    val indigo: Color = MaterialColorsList.indigo,
    val blue: Color = MaterialColorsList.blue,
    val cyan: Color = MaterialColorsList.cyan,
    val teal: Color = MaterialColorsList.teal,
    val green: Color = MaterialColorsList.green,
    val lightGreen: Color = MaterialColorsList.lightGreen,
    val lime: Color = MaterialColorsList.lime,

    ) {
}

class MaterialColorsList {
    companion object {
        val yellow: Color = Color(0xfff8c806)
        val red: Color = Color(0xFFF44336)
        val pink: Color = Color(0xFFE91E63)
        val purple: Color = Color(0xFF9C27B0)
        val deepPurple: Color = Color(0xFF673AB7)
        val indigo: Color = Color(0xff3f51b5)
        val blue: Color = Color(0xff2196f3)
        val cyan: Color = Color(0xFF00BCD4)
        val teal: Color = Color(0xFF009688)
        val green: Color = Color(0xFF348B38)
        val lightGreen: Color = Color(0xff8bc34a)
        val lime: Color = Color(0xff8bc34a)
    }
}

val MaterialColors = listOf(
    MaterialColorsList.red,
    MaterialColorsList.pink,
    MaterialColorsList.purple,
    MaterialColorsList.deepPurple,
    MaterialColorsList.indigo,
    MaterialColorsList.blue,
    MaterialColorsList.cyan,
    MaterialColorsList.teal,
    MaterialColorsList.green,
    MaterialColorsList.lightGreen,
    MaterialColorsList.lime,
)

