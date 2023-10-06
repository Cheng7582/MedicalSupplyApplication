package com.example.medicalsupplyapplication.roomDatabase

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Timestamp?): Long? {
        return value?.toDate()?.time
    }

    @TypeConverter
    fun toTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(Date(it)) }
    }
}