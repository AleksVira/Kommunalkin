package ru.virarnd.kommunalkin.models

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "cold_water_counters")
data class ColdWaterCounter(
    @Ignore
    override var counterType: CounterType = CounterType.COLD_WATER
) : BaseCounter()