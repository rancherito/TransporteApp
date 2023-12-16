package com.unamad.aulago.repository

import android.content.Context
import com.unamad.aulago.SystemDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val database: SystemDatabase,
    private val generalRepository: GeneralRepository,
    private val scope: CoroutineScope,
    @ApplicationContext private val context: Context
) {


}
