package ru.virarnd.kommunalkin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.virarnd.kommunalkin.models.*

@Database(entities = [User::class, EstateObjectFootprint::class, Counter::class], version = 1, exportSchema = false)
@TypeConverters(StatusConverter::class, CounterTypeConverter::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun estateObjectDao(): EstateObjectFootprintDao
    abstract fun counterDao(): CounterDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "registered_users_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}