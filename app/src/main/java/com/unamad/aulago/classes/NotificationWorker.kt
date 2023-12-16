package com.unamad.aulago.classes

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.unamad.aulago.repository.GeneralRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val generalRepository: GeneralRepository
) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {


        val outputData = Data.Builder().putString(WORK_RESULT, "Task Finished").build()
        return Result.success(outputData)
    }

    companion object {
        const val WORK_RESULT = "work_result"
    }
}