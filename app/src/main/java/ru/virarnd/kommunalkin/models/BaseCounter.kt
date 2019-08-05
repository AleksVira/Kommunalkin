package ru.virarnd.kommunalkin.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

abstract class BaseCounter {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "counter_id")
    var counterId: Long = 0

    @ColumnInfo(name = "counter_users_name")
    var counterUsersName: String? = ""

    @ColumnInfo(name = "counter_data")
    var counterData: Double = 0.0

    abstract var counterType: CounterType
}