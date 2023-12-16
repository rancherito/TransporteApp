package com.unamad.aulago

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PackageInfoFlags
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavBackStackEntry
import com.google.gson.Gson
import com.unamad.aulago.classes.ResponseData
import com.unamad.aulago.models.database.IModel
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.ui.views.teacher.vecToBitmap
import java.text.Normalizer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID
import java.util.regex.Pattern
import kotlin.reflect.KSuspendFunction1


class Utils {
    companion object {
        const val ERROR_MESSAGE: String = "AULAGO_ERROR"
        const val EmptyUUId: String = "00000000-0000-0000-0000-000000000000"
        const val PREFIX_PERUVIAN: String = "+51"
        val days = arrayOf("LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO", "DOMINGO")
        val months = arrayOf(
            "ENERO",
            "FEBRERO",
            "MARZO",
            "ABRIL",
            "MAYO",
            "JUNIO",
            "JULIO",
            "AGOSTO",
            "SEPTIEMBRE",
            "OCTUBRE",
            "NOVIEMBRE",
            "DICIEMBRE"
        )
        val currentDay: Int = LocalDateTime.now().dayOfWeek.value - 1
        fun openLink(context: Context, url: String) {
            try {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "No se puede abrir el link", Toast.LENGTH_SHORT).show()
            }
        }

        fun nowMilli() =
            LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        const val DAY: Long = 60 * 60 * 24
        const val HOUR: Long = 60 * 60
        const val MINUTE: Long = 60
        const val SECOND: Long = 1
        const val WEEK: Long = 60 * 60 * 24 * 7

        val NewUUId = fun(): String {
            return UUID.randomUUID().toString()
        }

        @Composable
        fun calculateLeftDate(
            date: LocalDateTime,
            viewModelStorage: ViewModelStorage = viewModelInstance()
        ): String {
            val now = viewModelStorage.nowMilli.observeAsState(initial = nowMilli()).value
            val dateMillis = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val distance = dateMillis - now
            val days = distance / (1000 * 60 * 60 * 24)
            val hours = (distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
            val minutes = (distance % (1000 * 60 * 60)) / (1000 * 60)
            val seconds = (distance % (1000 * 60)) / 1000

            val rDays = if (days != 0L) "${days}d " else ""
            val rHours = if (hours + days != 0L) "${hours}h " else ""
            val rMinutes = if (days != 0L) "" else "${minutes}m "
            val rSeconds = if (days + hours != 0L) "" else "${seconds}s"
            return if (distance < 0) "0m 0s" else rDays + rHours + rMinutes + rSeconds
        }

        fun showNotification(
            title: String,
            description: String,
            id: Int = 1,
            context: Context
        ) {
            Log.e("SSS", "REPE WORK")
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "activities"
            val channelName = "Notificaciones de actividades"
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            val builder = NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.logo_aula_go)

            manager.notify(id, builder.build())
        }


        fun <T> createErrorResponse(message: String): ResponseData<T> {
            return ResponseData(
                message = message,
                isSuccess = false,
                data = null
            )
        }

        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    //Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    //Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    //Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
            return false
        }

        fun checkLeapYear(year: Int): Boolean {
            return ((year % 400) == 0)
                    || (((year % 4) == 0) && ((year % 100) != 0))
        }

        fun openNumber(context: Context, phoneNumber: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phoneNumber}")
            context.startActivity(intent)
        }

        fun isValidWhatsAppGroupLink(link: String): Boolean {
            // La expresión regular para validar los enlaces de grupos de WhatsApp
            val pattern = Pattern.compile("https://chat\\.whatsapp\\.com/([a-zA-Z0-9]{20,32})")
            val matcher = pattern.matcher(link)

            // Verificar si el enlace coincide con la expresión regular
            return matcher.matches()
        }

        fun openWhatsapp(context: Context, phoneNumber: String) {

            try {
                val cleanedPhoneNumber = phoneNumber.replace("\\s+".toRegex(), "")
                    .replace("\\+".toRegex(), "")
                    .replace("-", "")
                    .replace("(", "")
                    .replace(")", "")
                val url = "https://api.whatsapp.com/send?phone=$cleanedPhoneNumber"
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: PackageManager.NameNotFoundException) {
                // TODO: Mejorar el mensaje de error
                Log.e("ERROR", e.toString())
            }
        }

        fun forceUTC(dateBegin: String): String {
            return LocalDateTime.ofInstant(
                Instant.parse("${dateBegin}Z"),
                ZoneId.systemDefault()
            ).toString()
        }

        fun localFormat(dateBegin: String): String {
            val date = LocalDateTime.ofInstant(
                Instant.parse(dateBegin),
                ZoneId.systemDefault()
            ).toString()
            Log.i("PARSE", dateBegin)
            Log.i("PARSE 2", date)

            return date
        }

        fun <T> List<T>.toJSON(): String {
            val gson = Gson()
            return gson.toJson(this)
        }

        fun <T> T.toJSON(): String {
            val gson = Gson()
            return gson.toJson(this)
        }

    }

}


fun PackageManager.getPackageInfoAlt(name: String): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(name, PackageInfoFlags.of(0))
    } else {
        getPackageInfo(name, PackageManager.GET_ACTIVITIES)
    }
}

fun Int.toHour(): HourInfo {
    val t = if (this > 12) this - 12 else this
    return HourInfo(
        time = "${if (t < 10) "0" else ""}$t",
        meridian = if (this < 12) "a.m." else "p.m."
    )

}

class HourInfo(
    val time: String,
    val meridian: String
) {
    override fun toString(): String {
        return "$time $meridian"
    }
}

fun LocalDateTime.diff(timeSeconds: Long, fromDate: LocalDateTime): Long {
    val from = fromDate.toEpochSecond(ZoneOffset.UTC)
    val to = this.toEpochSecond(ZoneOffset.UTC)
    return (from - to) / timeSeconds
}

fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

fun LocalDateTime.toHumanDate(short: Boolean = false): String {
    if (short) {
        val now = LocalDateTime.now()
        val nowFormalized = LocalDateTime.of(now.year, now.monthValue, now.dayOfMonth, 0, 0)
        val thisNormalize = LocalDateTime.of(this.year, this.monthValue, this.dayOfMonth, 0, 0)

        val diff = thisNormalize.diff(Utils.DAY, nowFormalized)
        if (diff == 0L) return "HOY"
        if (diff == -1L) return "MAÑANA"
        if (diff == -2L) return "EN 2 DÍAS"
        if (diff == 1L) return "AYER"
        if (diff == 2L) return "HACE 2 DÍAS"
    }
    val month = if (this.dayOfMonth > 10) this.dayOfMonth else "0" + this.dayOfMonth
    return "${
        Utils.days[this.dayOfWeek.value - 1].substring(
            0,
            3
        )
    }, $month ${
        Utils.months[this.monthValue - 1].substring(
            0,
            3
        )
    }"
}

fun LocalDateTime.toHumanTime(): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("KK:mm a")
    return this.format(formatter)
}

fun Color.toHsl(): FloatArray {
    val hsl = FloatArray(3)
    ColorUtils.RGBToHSL(
        (255 * this.red).toInt(),
        (255 * this.green).toInt(),
        (255 * this.blue).toInt(),
        hsl
    )
    return hsl
}

fun String.toDateTime(formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME): LocalDateTime? {
    return try {
        LocalDateTime.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        null
    }
}

fun Float.percent(): String {
    return String.format(
        "%.2f",
        this * 100
    ) + "%"
}

fun Double.percent(): String {
    return String.format(
        "%.2f",
        this * 100
    ) + "%"
}

fun Modifier.borderLef(color: Color): Modifier {

    return this.drawBehind {

        val strokeWidth = 4.dp.value * density
        drawLine(
            color,
            Offset(strokeWidth / 2, 0f),
            Offset(strokeWidth / 2, size.height),
            strokeWidth
        )
    }
}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.card(color: Color? = null, shadow: Boolean = false): Modifier = composed {

    this
        .composed {
            if (shadow)
                this.shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp)
                )
            else
                this

        }
        .clip(
            shape = RoundedCornerShape(16.dp)
        )
        .background(
            color = color ?: myColors().card
        )
}


fun NavBackStackEntry.getPath(path: String): String {
    return this.arguments?.getString(path)!!
}

fun NavBackStackEntry.getParameter(parameter: String): String? {
    return this.arguments?.getString(parameter)
}


fun Color.toHighLightness(): Color {
    val hsl = this.toHsl()
    return Color.hsl(hsl[0], hsl[1], 0.85f)
}

fun Color.light(lightness: Float): Color {
    val hsl = this.toHsl()
    return Color.hsl(hsl[0], hsl[1], lightness)
}

fun Color.toLightness(): Color {
    val hsl = this.toHsl()
    return Color.hsl(hsl[0], hsl[1], 0.7f)
}

fun Color.toDarkness(): Color {
    val hsl = this.toHsl()
    return Color.hsl(hsl[0], hsl[1], 0.3f)
}

fun Color.toHighDarkness(): Color {
    val hsl = this.toHsl()
    return Color.hsl(hsl[0], hsl[1], 0.15f)
}

fun getAppVersion(context: Context): String {
    try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionCode =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode.toString()
            else packageInfo.versionCode.toString()

        return "${packageInfo.versionName} ($versionCode)"
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "Desconocido"
}

/**
 * This function insert, update and delete values from database
 *
 * @param list the new data list for insert into database
 * @param oldList the old data list for delete from database
 * @param deleteFun function for drop values from database
 * @param updateFun function for update values from database
 * @param insertFun function for insert values into database
 * @return return the new values inserted
 */
@Suppress("UNCHECKED_CAST")
fun <T> genericReplacement(
    list: List<T>,
    oldList: List<T> = listOf(),
    deleteFun: ((list: List<T>) -> Unit)? = null,
    updateFun: (list: List<T>) -> Unit,
    insertFun: (list: List<T>) -> Unit
): List<T> {
    val listDelete = (oldList as List<IModel>).filter { old ->
        (list as List<IModel>).find { current -> old.id == current.id } == null
    }

    val newData = (list as List<IModel>).filter { newInfo ->
        (oldList as List<IModel>).find { old -> newInfo.id == old.id } == null
    }

    if (deleteFun != null) {
        deleteFun(listDelete as List<T>)
    }
    for (a in list) {
        insertFun(listOf(a as T))
        updateFun(listOf(a as T))
    }
    return (newData as List<T>)
}

fun <T> List<T>.toHumanString(): String {
    return when (val size = this.size) {
        0 -> ""
        1 -> this[0].toString()
        2 -> "${this[0]} y ${this[1]}"
        else -> {
            val stringBuilder = StringBuilder()
            for (i in 0 until size) {
                stringBuilder.append(this[i])
                if (i < size - 2) {
                    stringBuilder.append(", ")
                } else if (i == size - 2) {
                    stringBuilder.append(" y ")
                }
            }
            stringBuilder.toString()
        }
    }
}

suspend inline fun <reified T : Any> genericReplacement2(
    parameters: Array<String>,
    insertList: List<T>,
    oldList: List<T> = listOf(),
    deleteFun: KSuspendFunction1<List<T>, Unit>? = null,
    updateFun: KSuspendFunction1<List<T>, Unit>,
    insertFun: KSuspendFunction1<List<T>, Unit>
): List<T> {

    val sizeList = insertList.size
    if (sizeList == 0 && deleteFun != null) {
        deleteFun(oldList)
        return emptyList()
    }


    val clazz = T::class.java


    val filterListForDelete = dynamicFilter(oldList, insertList, parameters, true)

    val listDelete = oldList.filter { old ->
        filterListForDelete.find { current ->
            val field = clazz.getDeclaredField("id")
            field.isAccessible = true
            field.get(current) == field.get(old)
        } == null
    }

    val newData = insertList.filter { newInfo ->
        oldList.find { old ->
            val field = clazz.getDeclaredField("id")
            field.isAccessible = true
            val oldId = field.get(old)
            val newId = field.get(newInfo)
            oldId == newId
        } == null
    }

    if (deleteFun != null) {
        deleteFun(listDelete)
    }
    for (a in insertList) {
        insertFun(listOf(a))
        updateFun(listOf(a))
    }
    return newData
}

fun <T : Any> dynamicFilter(
    sourceList: List<T>,
    filterItems: List<T>,
    searchCriteria: Array<String>,
    changeIds: Boolean = false
): List<T> {
    val itemClass = filterItems.firstOrNull()?.javaClass ?: return emptyList()

    val filteredLists = filterItems.map { filterItem ->
        val foundItem = sourceList.find { sourceItem ->
            searchCriteria.all { criteriaField ->
                val field = itemClass.getDeclaredField(criteriaField)
                field.isAccessible = true
                field.get(sourceItem) == field.get(filterItem)
            }
        }
        when {
            foundItem == null -> null
            changeIds -> {
                val idField = itemClass.getDeclaredField("id")
                idField.isAccessible = true
                val newId = idField.get(foundItem)
                idField.set(filterItem, newId)
                filterItem
            }

            else -> filterItem
        }
    }

    return filteredLists.filterNotNull()
}


fun colorToColorMatrix(color: Color): ColorMatrix {
    val red = color.red
    val green = color.green
    val blue = color.blue
    val alpha = color.alpha
    return ColorMatrix(
        floatArrayOf(
            red, 0f, 0f, 0f, 0f,  // Red
            0f, green, 0f, 0f, 0f,  // Green
            0f, 0f, blue, 0f, 0f,  // Blue
            0f, 0f, 0f, alpha, 0f   // Alpha
        )
    )
}


@Composable
fun bitmapFilter(
    @DrawableRes id: Int,
    color: Color = MaterialTheme.colorScheme.primary
): ImageBitmap {
    val res = vecToBitmap(id)
    val imageBitmap = res.image
    val matrix = colorToColorMatrix(color)

    val androidBitmap = imageBitmap.asAndroidBitmap()
    val width = androidBitmap.width
    val height = androidBitmap.height
    val destBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(destBitmap)
    val paint = Paint()
    val grayMatrix = ColorMatrix()
    grayMatrix.setSaturation(0f)
    grayMatrix.postConcat(matrix)
    paint.colorFilter = ColorMatrixColorFilter(grayMatrix)
    canvas.drawBitmap(androidBitmap, 0f, 0f, paint)

    return destBitmap.asImageBitmap()
}

fun String.removeAccents(): String {
    val nfdNormalizedString = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Regex("[\\p{InCombiningDiacriticalMarks}]").replace(nfdNormalizedString, "")
}