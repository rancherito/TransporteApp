package com.unamad.aulago.ui.layouts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unamad.aulago.ViewModelStorage
import com.unamad.aulago.card
import com.unamad.aulago.models.query.TeacherSectionInfoQueryModel
import com.unamad.aulago.navControllerInstance
import com.unamad.aulago.ui.components.ChipSquare
import com.unamad.aulago.ui.components.EmptyCard
import com.unamad.aulago.ui.components.IconSquare
import com.unamad.aulago.ui.theme.myColors
import com.unamad.aulago.viewModelInstance
import com.unamad.aulago.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("InternalInsetResource", "DiscouragedApi")
@Composable
fun SectionLayout(
    modifier: Modifier = Modifier,
    sectionId: String,
    viewModelStorage: ViewModelStorage = viewModelInstance(),
    navHostController: NavHostController = navControllerInstance(),
    contentBottom: @Composable() (() -> Unit)? = null,
    contentBottomPadding: PaddingValues = PaddingValues(16.dp),
    showState: MutableState<Boolean> = rememberBoolean(),
    primaryColor: Color? = null,
    sectionInfo: TeacherSectionInfoQueryModel? = viewModelStorage.teacherRepository.getTeacherSectionStream(
        sectionId = sectionId
    ).observeAsState().value,
    content: @Composable() (TeacherSectionInfoQueryModel.() -> Unit),
) {
    viewModelStorage.isContrast.postValue(true)
    

    BaseLayout(modifier = modifier, primaryColor = primaryColor) {
        if (contentBottom != null) {

            if (showState.value) {
                ModalBottomSheet(
                    onDismissRequest = { showState.value = false },
                    containerColor = myColors().background,
                    windowInsets = WindowInsets.navigationBarsIgnoringVisibility
                ) {
                    Column(
                        modifier = Modifier
                            .padding(contentBottomPadding)
                    ) {
                        contentBottom()
                    }

                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            IconButton(onClick = {
                navHostController.navigateUp()
            }, modifier = Modifier.offset(x = 16.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_narrow_left),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .card(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (sectionInfo != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconSquare(
                            painter = painterResource(id = R.drawable.duotone_layers),
                            color = Color.White
                        )
                        Column {
                            Text(
                                text = sectionInfo.courseName,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = sectionInfo.careerName,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ChipSquare("GRUPO ${sectionInfo.sectionCode}")
                        ChipSquare("CÓDIGO ${sectionInfo.courseCode}")

                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ){
                if (sectionInfo != null) {
                    content(sectionInfo)
                }
                else{
                    EmptyCard(
                        painter = painterResource(id = R.drawable.duotone_layers),
                    ) {
                        Text(text = "No se encontró la sección")
                    }
                }
            }
        }


    }
}
