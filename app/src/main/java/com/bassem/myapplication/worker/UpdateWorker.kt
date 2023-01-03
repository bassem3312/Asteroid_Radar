package com.bassem.myapplication.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bassem.myapplication.database.AsteroidDatabase
import com.bassem.myapplication.repository.AsteroidRepository
import retrofit2.HttpException
import kotlin.math.log


/**
 * @author Bassem Mohsen : basem3312@gmail.com on 1/3/2023.
 */

class UpdateWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "UpdateWorker"
    }

    override suspend fun doWork(): Result {

        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.refreshAsteroidListIntoDB()
            Result.success()
        } catch (ex: HttpException) {
            ex.printStackTrace()
            Result.retry()
        }
    }

}