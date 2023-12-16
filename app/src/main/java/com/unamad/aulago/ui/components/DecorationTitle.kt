package com.unamad.aulago.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.unamad.aulago.Roles
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R

@Composable
fun DecorationTitle(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    viewModelStorage: ViewModelStorage = viewModelInstance()
) {
    val isOnline = viewModelStorage.isOnline.observeAsState(false).value
    val session = viewModelStorage.generalRepository.userSystemDataStream.observeAsState().value

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,

        modifier = modifier.fillMaxWidth()
    ) {

        Icon(
            painter = painterResource(id = if (isOnline) R.drawable.wifi else R.drawable.wifi_off),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )

        if (arrayOf(
                Roles.Student,
                Roles.Teacher
            ).contains(session?.role)
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            IconButton(
                onClick = { viewModelStorage.closeSession() },
                modifier = Modifier.size(26.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.log_out),
                    contentDescription = null,
                    tint = color
                )
            }

        }

    }
}