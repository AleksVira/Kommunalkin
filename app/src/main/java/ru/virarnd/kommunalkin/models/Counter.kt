package ru.virarnd.kommunalkin.models

import androidx.room.*

@Entity(
    tableName = "counters",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = EstateObjectFootprint::class,
            parentColumns = arrayOf("footprint_id"),
            childColumns = arrayOf("estate_object_footprint_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)),
    indices = [Index(value = ["estate_object_footprint_id"], unique = false)]
)
data class Counter(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "counter_state_id")
    val counterStateId: Long = 0,

    @ColumnInfo(name = "estate_object_footprint_id")
    val estateObjectFootprintId: Long = 0,

    @ColumnInfo(name = "counter_type")
    val counterType: CounterType = CounterType.COLD_WATER,

    @ColumnInfo(name = "counter_name")
    val counterName: String = "",

    @ColumnInfo(name = "counter_reading")
    var counterReading: Double = 0.0
)
