package ru.virarnd.kommunalkin.models

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "gas_counters")
data class GasCounter(
    @Ignore
    override var counterType: CounterType = CounterType.GAS
) : BaseCounter()