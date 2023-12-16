package com.unamad.aulago.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.unamad.aulago.Utils
import com.unamad.aulago.classes.UserContact
import com.unamad.aulago.emums.TextIconType
import com.unamad.aulago.R

@Composable
fun UserContactBox(
    user: UserContact,
    modifier: Modifier = Modifier
) {
    val prefixNumber = Utils.PREFIX_PERUVIAN

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        TextIcon(
            text = user.fullName,
            painter = painterResource(id = R.drawable.person),
            isCopyable = true,
        )
        TextIcon(
            text = if (user.phoneNumber == null) "sin número" else "${prefixNumber}${user.phoneNumber}",
            painter = painterResource(id = R.drawable.phone),
            isCopyable = true,
            fontStyle = if (user.phoneNumber == null) FontStyle.Italic else FontStyle.Normal,
            textIconType = TextIconType.PhoneNumber
        )
        TextIcon(
            text = user.email ?: "sin correo electronico",
            painter = painterResource(id = R.drawable.email),
            isCopyable = true,
            fontStyle = if (user.email == null) FontStyle.Italic else FontStyle.Normal
        )

    }
}