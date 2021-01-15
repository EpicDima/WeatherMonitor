package by.resliv.weathermonitor.schedule

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

/**
 * @author EpicDima
 */
const val SCHEDULE_WORKER_TAG = "schedule_worker"

fun updateScheduleWork(context: Context, intervalMinutes: Long) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    val refreshTask = PeriodicWorkRequestBuilder<RefreshWorker>(intervalMinutes, TimeUnit.MINUTES)
        .addTag(SCHEDULE_WORKER_TAG)
        .setConstraints(constraints)
        .build()
    val workManager = WorkManager.getInstance(context)
    workManager.cancelAllWorkByTag(SCHEDULE_WORKER_TAG)
    workManager.enqueue(refreshTask)
}

suspend fun checkIfScheduleWorkExisting(context: Context, intervalMinutes: Long) {
    val workManager = WorkManager.getInstance(context)
    val tasks = workManager.getWorkInfosByTag(SCHEDULE_WORKER_TAG).await().size
    if (tasks == 0) {
        updateScheduleWork(context, intervalMinutes)
    }
}