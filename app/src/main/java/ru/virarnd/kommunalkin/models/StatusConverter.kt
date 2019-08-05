package ru.virarnd.kommunalkin.models

import androidx.room.TypeConverter

class StatusConverter {
    @TypeConverter
    fun stringToStatus(statusString: String): EstateObjectStatus {
        return when (statusString) {
            "COMPLETED" -> EstateObjectStatus.COMPLETED
            "DRAFT" -> EstateObjectStatus.DRAFT
            else -> EstateObjectStatus.EMPTY
        }
    }

    @TypeConverter
    fun statusToString(status: EstateObjectStatus): String {
        return when (status) {
            EstateObjectStatus.COMPLETED -> "COMPLETED"
            EstateObjectStatus.DRAFT -> "DRAFT"
            EstateObjectStatus.EMPTY -> "EMPTY"
        }
    }
}