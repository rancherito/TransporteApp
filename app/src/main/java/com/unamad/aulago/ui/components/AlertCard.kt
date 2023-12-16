package com.unamad.aulago.ui.components

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unamad.aulago.card
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AlertCard(
    title: String? = null,
    body: String? = null,
    alertType: AlertType = AlertType.Success
) {

    val animation: InfiniteTransition = rememberInfiniteTransition(label = "")
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val shouldShake: MutableState<Boolean> = remember { mutableStateOf(true) }
    val angle by animation.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (true) {
                shouldShake.value = true
                delay(1000) // shake duration
                shouldShake.value = false
                delay(5000) // wait for 10 seconds
            }
        }
    }


    var bg = myColors().success.copy(alpha = 0.2f)
    var titleColor = myColors().success
    var painter = painterResource(id = R.drawable.duetone_megaphone)

    if (alertType == AlertType.Danger) {
        bg = myColors().danger.copy(alpha = 0.2f)
        titleColor = myColors().danger
        painter = painterResource(id = R.drawable.close_square)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .card(bg)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = "Success Icon",
                tint = titleColor,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(if (shouldShake.value) angle else 0f)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        fontSize = 16.sp
                    )
                }
                if (body != null) {
                    Text(
                        text = body,
                        color = titleColor,
                        fontSize = 13.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

enum class AlertType {
    Success, Danger, Warning, Info
}