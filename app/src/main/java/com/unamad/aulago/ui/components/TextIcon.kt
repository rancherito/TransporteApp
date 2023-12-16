package com.unamad.aulago.ui.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.Utils
import com.unamad.aulago.emums.TextIconType
import com.unamad.aulago.toLightness
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R


@Composable
fun TextIcon(
    text: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    isCopyable: Boolean = false,
    fontStyle: FontStyle = FontStyle.Normal,
    textIconType: TextIconType = TextIconType.Normal,
    tint: Color? = null,
    textColor: Color = myColors().body
) {

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(14.dp),
            tint = tint ?: textColor
        )
        Text(
            text = text,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            modifier = Modifier.weight(1f),
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontStyle = fontStyle,
            softWrap = false
        )
        if (textIconType == TextIconType.PhoneNumber) {
            Icon(
                painter = painterResource(id = R.drawable.whatsapp),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(16.dp)
                    .width(16.dp)
                    .clickable {
                        Utils.openWhatsapp(context, text)
                    },
                tint = Color(0xFF009688)
            )
            Icon(
                painter = painterResource(id = R.drawable.call),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(16.dp)
                    .width(16.dp)
                    .clickable {
                        Utils.openNumber(context, text)
                    },
                tint = Color(0xFF3F51B5)

            )
        }
        if (isCopyable) {
            Icon(
                painter = painterResource(id = R.drawable.copy),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(16.dp)
                    .width(16.dp)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(text))
                        Toast
                            .makeText(context, "Copiado: $text", Toast.LENGTH_LONG)
                            .show()

                    },
                tint = textColor.toLightness()
            )
        }

    }
}