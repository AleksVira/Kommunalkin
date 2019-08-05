package ru.virarnd.kommunalkin.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "registered_users",
    indices = [Index(value = ["login"], unique = true)]
)
data class User(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Long = 0,

    @ColumnInfo(name = "login")
    val login: String = "",

    @ColumnInfo(name = "password")
    val password: String = ""
)