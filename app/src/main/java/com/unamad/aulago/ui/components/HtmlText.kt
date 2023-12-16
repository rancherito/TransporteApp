package com.unamad.aulago.ui.components

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.unamad.aulago.classes.DefaultLinkMovementMethod
import com.unamad.aulago.ui.theme.myColors
import kotlin.math.max

private const val LINK_1 = "link_1"
private const val LINK_2 = "link_2"
private const val SPACING_FIX = 3f

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    html: String,
    textStyle: TextStyle = TextStyle.Default,
    onLink1Clicked: (() -> Unit)? = null,
    onLink2Clicked: (() -> Unit)? = null
) {
    val linkColor = MaterialTheme.colorScheme.primary.toArgb()
    val color = myColors().body.toArgb()

    AndroidView(
        modifier = modifier,
        update = {
            it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS)
        },
        factory = { context ->
            val spacingReady =
                max(textStyle.lineHeight.value - textStyle.fontSize.value - SPACING_FIX, 0f)
            val extraSpacing = spToPx(spacingReady.toInt(), context)
            val gravity = when (textStyle.textAlign) {
                TextAlign.Center -> Gravity.CENTER
                TextAlign.End -> Gravity.END
                else -> Gravity.START
            }
            /*val fontResId = when (textStyle.fontWeight) {
                FontWeight.Medium -> R.font.inter_medium
                else -> R.font.inter_regular
            }*/
            // val font = ResourcesCompat.getFont(context, fontResId)

            TextView(context).apply {
                // general style
                textSize = textStyle.fontSize.value
                setLineSpacing(extraSpacing, 1f)
                setTextColor(textStyle.color.toArgb())
                setGravity(gravity)
                setTextColor(color)
                //typeface = font
                setLinkTextColor(linkColor)
                movementMethod = DefaultLinkMovementMethod() { link ->
                    when (link) {
                        LINK_1 -> onLink1Clicked?.invoke()
                        LINK_2 -> onLink2Clicked?.invoke()
                    }
                    true
                }
            }
        }
    )
}

fun spToPx(sp: Int, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        context.resources.displayMetrics
    )
}