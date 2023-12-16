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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.unamad.aulago.toLightness
import com.unamad.aulago.ui.theme.myColors

@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun FloatingIconButton(
    icon: Painter,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = myColors().secondary,
    onClick: () -> Unit
) {

    val scale: Float by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.95f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = ""
    )
    val contentColor = if (enabled) color else color.toLightness()

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .scale(scale)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable(enabled) { onClick() }
            .background(color = contentColor)
            .size(48.dp)
            .width(IntrinsicSize.Min)

    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colorScheme.surfaceVariant
        )
    }

}