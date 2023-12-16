package com.unamad.aulago.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.unamad.aulago.ui.theme.myColors

@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun FloatingTextButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    enabled: Boolean = true,
    color: Color = myColors().secondary,
    textColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit
) {

    val scale: Float by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.95f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = ""
    )
    val contentColor = if (enabled) color else myColors().gray100

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .scale(scale)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .composed {
                if (enabled)
                    this.clickable {
                        onClick()
                    }
                else this
            }
            .background(color = contentColor)
            .padding(horizontal = 16.dp)
            .height(48.dp)
            .width(IntrinsicSize.Min)

    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = textColor
            )
        }
        Text(text = text, modifier = Modifier.weight(1f), color = textColor, textAlign = TextAlign.Center)
    }

}


