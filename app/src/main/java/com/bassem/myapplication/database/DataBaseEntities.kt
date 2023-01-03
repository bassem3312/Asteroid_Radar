package com.bassem.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bassem.myapplication.model.AsteroidModel


/**
 * @author Bassem Mohsen : basem3312@gmail.com on 1/3/2023.
 */

@Entity(tableName = "asteroid_table")
data class AsteroidDataBaseModel constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var codename: String,
    var closeApproachDate: String,
    var absoluteMagnitude: Double,
    var estimatedDiameter: Double,
    var relativeVelocity: Double,
    var distanceFromEarth: Double,
    var isPotentiallyHazardous: Boolean
)

fun List<AsteroidDataBaseModel>.asDomainModel(): List<AsteroidModel> {
    return map {
        AsteroidModel(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous

        )
    }
}

fun List<AsteroidModel>.asDataBaseModel(): Array<AsteroidDataBaseModel> {
    return map {
        AsteroidDataBaseModel(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
