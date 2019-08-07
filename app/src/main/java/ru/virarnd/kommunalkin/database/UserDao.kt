package ru.virarnd.kommunalkin.database

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.virarnd.kommunalkin.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: ArrayList<User>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM registered_users")
    fun clear()

    @Query("SELECT * FROM registered_users ORDER BY login")
    fun getAllUsers() : LiveData<List<User>>

    @Query("SELECT * FROM registered_users WHERE login = :login")
    suspend fun getUserByLogin(login: String) : User?

    @Query("SELECT * FROM registered_users WHERE user_id = :userId")
    suspend fun getUserById(userId: Long) : User?

    @Transaction
    @Query("SELECT * FROM registered_users")
    suspend fun getUserAndAllEstateObjects(): List<UserAndEstateObjects>

    @Transaction
    @Query("SELECT * FROM registered_users WHERE user_id = :userId")
    suspend fun getUserAndEstateObjectsById(userId: Long): UserAndEstateObjects?

//    @Transaction
//    @Query("SELECT * FROM registered_users WHERE user_id = :userId")
//    suspend fun getLiveUserAndEstateObjectsById(userId: Long): LiveData<UserAndEstateObjects>

}