package com.unamad.aulago.ui.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.card
import com.unamad.aulago.light
import com.unamad.aulago.toLightness
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R


@Preview
@Composable
fun TestSimpleTextIcon() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HorizontalTextIcon(
            title = "Titulo de prueba",
            icon = painterResource(id = R.drawable.book),
            description = "Una descripcion de prueba",
            iconColor = Color.Red,
            isCopyable = true,
            modifier = Modifier
                .fillMaxWidth()
                .card()
                .padding(16.dp),
        )
        HorizontalTextIcon(
            title = "Titulo de prueba 2",
            icon = painterResource(id = R.drawable.book),
            description = "Una descripcion de prueba 2",
            iconColor = Color.Blue,
            isCopyable = true,
            content = {
                Text(text = "Este es un contenido de prueba")
            },
            modifier = Modifier
                .fillMaxWidth()
                .card()
                .padding(16.dp),

            )
        HorizontalTextIcon(
            title = "Titulo de prueba 3",
            icon = painterResource(id = R.drawable.book),
            description = "Una descripcion de prueba 3",
            iconColor = Color.Magenta,
            isCopyable = true,
            modifier = Modifier
                .fillMaxWidth()
                .card()
                .padding(16.dp),
            onClick = {

            }

        )
    }
}


@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun HorizontalTextIcon(
    title: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    description: String? = null,
    iconColor: Color? = null,
    isCopyable: Boolean = false,
    textColor: Color = myColors().body,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    onClick: (() -> Unit)? = null,
    isACard: Boolean = false,
    content: (@Composable () -> Unit)? = null
) {

    val paddingCard = if (isACard) 16.dp else 0.dp
    var internalModifier = Modifier.composed {
        if (isACard) Modifier.card() else Modifier
    }

    if (onClick != null)
        internalModifier = internalModifier.clickable(enabled = true, onClick = onClick)


    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = internalModifier
            .padding(paddingCard)
            .composed { modifier }
    ) {
        IconSquare(
            painter = icon,
            color = iconColor ?: textColor,
            modifier = Modifier.padding(top = 2.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            if (description != null) {
                Text(
                    text = description,
                    fontSize = 13.sp,
                    lineHeight = 15.sp,
                    color = textColor.light(.4f),
                )
            }
            if (content != null) content()

        }

        if (isCopyable && onClick == null) {
            Icon(
                painter = painterResource(id = R.drawable.copy),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(16.dp)
                    .clip(shape = RoundedCornerShape(2.dp))
                    .clickable {
                        clipboardManager.setText(AnnotatedString(title))
                        Toast
                            .makeText(context, "Copiado: $title", Toast.LENGTH_LONG)
                            .show()

                    },
                tint = textColor.toLightness()
            )
        }
        if (onClick != null) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .width(28.dp)
                    .height(32.dp),
                tint = iconColor ?: textColor
            )
        }

    }
}