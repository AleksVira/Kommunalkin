package ru.virarnd.kommunalkin.models

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "hot_water_counters")
data class HotWaterCounter(
    @Ignore
    override var counterType: CounterType = CounterType.HOT_WATER
) : BaseCounter()