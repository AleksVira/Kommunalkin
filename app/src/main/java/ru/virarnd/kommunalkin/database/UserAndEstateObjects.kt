package ru.virarnd.kommunalkin.database

import androidx.room.Embedded
import androidx.room.Relation
import ru.virarnd.kommunalkin.models.EstateObjectFootprint
import ru.virarnd.kommunalkin.models.User

data class UserAndEstateObjects(
    @Embedded
    val user: User,

    @Relation(parentColumn = "user_id", entityColumn = "owner_id", entity = EstateObjectFootprint::class)
    val estateObjectFootprints: List<EstateObjectFootprintAndCounters>
)