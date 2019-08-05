package ru.virarnd.kommunalkin.models

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "electricity_counters")
data class ElectricityCounter(
    @Ignore
    override var counterType: CounterType = CounterType.ELECTRICITY
) : BaseCounter()