package ru.virarnd.kommunalkin.database

import androidx.room.Embedded
import androidx.room.Relation
import ru.virarnd.kommunalkin.models.Counter
import ru.virarnd.kommunalkin.models.EstateObjectFootprint

data class EstateObjectFootprintAndCounters(
    @Embedded
    val estateObjectFootprint: EstateObjectFootprint,

    @Relation(parentColumn = "footprint_id", entityColumn = "estate_object_footprint_id", entity = Counter::class)
    val counters: List<Counter>

)