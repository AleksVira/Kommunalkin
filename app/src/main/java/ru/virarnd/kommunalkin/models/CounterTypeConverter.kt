package ru.virarnd.kommunalkin.models

import androidx.room.TypeConverter

class CounterTypeConverter {
    @TypeConverter
    fun stringToCounterType(counterType: String): CounterType {
        return when (counterType) {
            "COLD_WATER" -> CounterType.COLD_WATER
            "HOT_WATER" -> CounterType.HOT_WATER
            "GAS" -> CounterType.GAS
            else -> CounterType.COLD_WATER
        }
    }

    @TypeConverter
    fun counterTypeToString(counterType: CounterType): String {
        return when (counterType) {
            CounterType.COLD_WATER -> "COLD_WATER"
            CounterType.HOT_WATER -> "HOT_WATER"
            CounterType.GAS -> "GAS"
            CounterType.ELECTRICITY -> "ELECTRICITY"
        }
    }
}