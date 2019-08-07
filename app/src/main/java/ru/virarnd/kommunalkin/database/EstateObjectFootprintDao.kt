package ru.virarnd.kommunalkin.database

import androidx.room.*
import ru.virarnd.kommunalkin.models.EstateObjectFootprint

@Dao
interface EstateObjectFootprintDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(estateObjectFootprint: EstateObjectFootprint) : Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(estateObjectFootprints: ArrayList<EstateObjectFootprint>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(estateObjectFootprint: EstateObjectFootprint)

    @Query("SELECT * FROM estate_objects WHERE object_id = :objectId")
    suspend fun getByEstateObjectId(objectId: Long): EstateObjectFootprint?

    @Transaction
    @Query("SELECT * FROM estate_objects WHERE owner_id = :userId")
    suspend fun getEstateObjectAndCountersByOwnerId(userId: Long): List<EstateObjectFootprintAndCounters>

    @Transaction
    @Query("SELECT * FROM estate_objects WHERE owner_id = :userId AND object_year_and_month = :yearAndMonth ")
    suspend fun getEstateObjectAndCountersByOwnerIdAndDate(userId: Long, yearAndMonth: Int): List<EstateObjectFootprintAndCounters>?



//    @Query("SELECT * FROM estate_objects WHERE owner_id = :userId")
//    suspend fun getLiveEstateObjectDataByOwnerId(userId: Long): LiveData<List<EstateObjectFootprint>>

}