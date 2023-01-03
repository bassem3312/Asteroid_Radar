package com.bassem.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * @author Bassem Mohsen : basem3312@gmail.com on 1/3/2023.
 */
@Dao
interface AsteroidDatabaseDao {

    @Query("Select * From asteroid_table Order By closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<AsteroidDataBaseModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: AsteroidDataBaseModel)

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate = :startDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsDay(startDate: String): LiveData<List<AsteroidDataBaseModel>>

}