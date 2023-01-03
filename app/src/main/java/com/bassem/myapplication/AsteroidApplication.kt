package com.bassem.myapplication

import android.app.Application
import android.os.Build
import androidx.work.*
import com.bassem.myapplication.worker.UpdateWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


/**
 * @author Bassem Mohsen : basem3312@gmail.com on 1/3/2023.
 */
class AsteroidApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }


    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<UpdateWorker>(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag(UpdateWorker.WORK_NAME)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            UpdateWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)

//        val repeatingRequest= OneTimeWorkRequestBuilder<UpdateWorker>()
//            .setConstraints(constraints)
//            .build()
//
//        WorkManager.getInstance().enqueue(repeatingRequest)

    }

}