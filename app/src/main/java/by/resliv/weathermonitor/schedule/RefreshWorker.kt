package by.resliv.weathermonitor.schedule

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import by.resliv.weathermonitor.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author EpicDima
 */
class RefreshWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(
    context,
    workerParams
) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        repository.getAllLocations().forEach {
            repository.refreshForecast(it, !it.auto)
        }
        Result.success()
    }
}