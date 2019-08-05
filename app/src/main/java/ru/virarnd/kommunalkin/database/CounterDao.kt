package ru.virarnd.kommunalkin.database

import androidx.room.*
import ru.virarnd.kommunalkin.models.Counter

@Dao
interface CounterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(counter: Counter) : Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(countersList: ArrayList<Counter>)

    @Query("SELECT * FROM counters WHERE estate_object_footprint_id = :footprint")
    suspend fun selectCountersByFootprint(footprint: Long): List<Counter>?
}