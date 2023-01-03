package com.bassem.myapplication.ui.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.bassem.myapplication.database.AsteroidDatabase
import com.bassem.myapplication.model.AsteroidModel
import com.bassem.myapplication.model.PictureOfDay
import com.bassem.myapplication.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class AsteroidApiStatus { LOADING, ERROR, DONE }
enum class FilterAsteroid { TODAY, WEEK, ALL }
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = AsteroidDatabase.getInstance(app.applicationContext)

    private val asteroidRepository = AsteroidRepository(database)

    private var _filterAsteroid = MutableLiveData(FilterAsteroid.ALL)

    var asteroidList = Transformations.switchMap(_filterAsteroid) {
        when (it!!) {
            FilterAsteroid.WEEK -> asteroidRepository.weekAsteroids
            FilterAsteroid.TODAY -> asteroidRepository.todayAsteroids
            else -> asteroidRepository.allAsteroids
        }
    }


    val status = asteroidRepository.loadingStatus


    private val _navigateToSelectedAsteroid = MutableLiveData<AsteroidModel?>()
    val navigateToSelectedAsteroid: MutableLiveData<AsteroidModel?>
        get() = _navigateToSelectedAsteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        status.value = AsteroidApiStatus.LOADING
        getAsteroidList()
        getImageOfTheDay()
    }

    private fun getAsteroidList() {
        viewModelScope.launch {
//            asteroidList = asteroidRepository.asteroids
            asteroidRepository.refreshAsteroidListIntoDB()
        }
    }

    private fun getImageOfTheDay() {

        viewModelScope.launch {
            try {
                _pictureOfDay.postValue(asteroidRepository.getPictureOfDay())
            } catch (ex: Exception) {
                ex.printStackTrace()
                _pictureOfDay.postValue(PictureOfDay("ss", "ss", "ss"))
            }
        }
    }

    fun onAsteroidItemClicked(asteroidModel: AsteroidModel) {
        _navigateToSelectedAsteroid.value = asteroidModel
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    fun onChangeFilter(filterAsteroid: FilterAsteroid) {
        _filterAsteroid.postValue(filterAsteroid)
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }


}
