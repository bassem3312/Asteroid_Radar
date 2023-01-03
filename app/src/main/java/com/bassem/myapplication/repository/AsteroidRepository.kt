package com.bassem.myapplication.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bassem.myapplication.Constants.API_KEY
import com.bassem.myapplication.api.AsteroidApiService
import com.bassem.myapplication.api.getNextSevenDays
import com.bassem.myapplication.api.getTodayDate
import com.bassem.myapplication.api.parseAsteroidsJsonResult
import com.bassem.myapplication.database.AsteroidDatabase
import com.bassem.myapplication.database.asDataBaseModel
import com.bassem.myapplication.database.asDomainModel
import com.bassem.myapplication.model.AsteroidModel
import com.bassem.myapplication.model.PictureOfDay
import com.bassem.myapplication.ui.main.AsteroidApiStatus
import com.bassem.myapplication.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * @author Bassem Mohsen : basem3312@gmail.com on 1/2/2023.
 */
class AsteroidRepository(private val database: AsteroidDatabase) {


    val loadingStatus = MutableLiveData<AsteroidApiStatus>()
    private val startDate = getTodayDate()

    private val endDate = getNextSevenDays()

    val allAsteroids: LiveData<List<AsteroidModel>> =
        Transformations.map(database.asteroidDatabaseDao().getAsteroids()) {
            it.asDomainModel()
        }

    val todayAsteroids: LiveData<List<AsteroidModel>> =
        Transformations.map(database.asteroidDatabaseDao().getAsteroidsDay(startDate.format(DateTimeFormatter.ISO_DATE))) {
            it.asDomainModel()
        }

    val weekAsteroids: LiveData<List<AsteroidModel>> =
        Transformations.map(
            database.asteroidDatabaseDao().getAsteroidsDate(
                startDate,
                endDate)
        ) {
            it.asDomainModel()
        }


    suspend fun refreshAsteroidListIntoDB() {
        withContext(Dispatchers.IO) {

            try {
                loadingStatus.postValue(AsteroidApiStatus.LOADING)
                val asteroids = AsteroidApiService.AsteroidApi.retrofitService.getAsteroids( API_KEY)
                var asteroidList = parseAsteroidsJsonResult(JSONObject(asteroids.toString()))
                database.asteroidDatabaseDao().insertAll(*asteroidList.asDataBaseModel())
                loadingStatus.postValue(AsteroidApiStatus.DONE)
            } catch (e: Exception) {
                e.printStackTrace()
                loadingStatus . postValue (AsteroidApiStatus.ERROR)
            }
        }
    }

    @Throws(Exception::class)
    suspend fun getPictureOfDay(): PictureOfDay {

        try {
            return AsteroidApiService.AsteroidApi.retrofitService.getPlanetaryApod(API_KEY)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}