package com.unamad.aulago.classes

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.unamad.aulago.Utils
import com.unamad.aulago.repository.GeneralRepository
import com.unamad.aulago.repository.StudentRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val studentRepository: StudentRepository,
    val generalRepository: GeneralRepository
) :
    CoroutineWorker(context, workerParameters) {
    private var startTime = LocalDateTime.now()
    override suspend fun doWork(): Result {

        Log.i("MESSAGE NOTIFICATION", this.id.toString())

        //TODO: MAKE A NOTIFICATION
        val res = withContext(Dispatchers.IO) {
            val session = generalRepository.getSessionData()
            if (session != null)
                studentRepository.loadHomeworkSync(session)
            else listOf()
        }
        Log.i("MESSAGE NOTIFICATION", res.size.toString())

        res.forEachIndexed { index, homework ->
            Utils.showNotification(
                title = homework.homeworkTitle ?: "",
                description = homework.homeworkDescription ?: "",
                id = index + 1,
                context = applicationContext
            )
        }
        startTime = LocalDateTime.now()


        val outputData = Data.Builder().putString(WORK_RESULT, "Task Finished").build()
        return Result.success(outputData)
    }

    companion object {
        const val WORK_RESULT = "work_result"
    }
}