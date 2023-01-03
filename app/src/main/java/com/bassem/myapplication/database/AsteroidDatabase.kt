package com.bassem.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bassem.myapplication.Constants


/**
 * @author Bassem Mohsen : basem3312@gmail.com on 1/3/2023.
 */
@Database(entities = [AsteroidDataBaseModel::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract fun asteroidDatabaseDao(): AsteroidDatabaseDao

    companion object {
        private var instance: AsteroidDatabase? = null

        fun getInstance(context: Context): AsteroidDatabase {

            return instance ?: synchronized(Any()) {
                instance ?: buildInstance(context).also { instance = it }
            }
        }

        private fun buildInstance(context: Context): AsteroidDatabase {
            return Room.databaseBuilder(context.applicationContext, AsteroidDatabase::class.java, Constants.DATA_BASE_NAME).build()
        }
    }
}