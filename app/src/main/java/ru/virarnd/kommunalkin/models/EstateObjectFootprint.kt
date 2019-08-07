package ru.virarnd.kommunalkin.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "estate_objects",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("owner_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ),
    indices = [Index(value = ["owner_id"], unique = false)]
)
data class EstateObjectFootprint(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "footprint_id")
    val footprintId: Long = 0,

    @ColumnInfo(name = "object_id")
    val objectId: Int = 0,

    @ColumnInfo(name = "owner_id")
    val ownerId: Long = 0,

    @ColumnInfo(name = "object_name")
    val name: String = "",

    @ColumnInfo(name = "comment")
    val comment: String?,

    @ColumnInfo(name = "object_year_and_month")
    val objectDate: Int = 0,

    @ColumnInfo(name = "Status")
    var status: EstateObjectStatus = EstateObjectStatus.EMPTY

)