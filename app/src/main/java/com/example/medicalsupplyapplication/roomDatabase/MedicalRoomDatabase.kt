package com.example.medicalsupplyapplication.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Admin::class], version = 1, exportSchema = false)
abstract class MedicalRoomDatabase : RoomDatabase() {

    abstract val medicalRoomDao: MedicalRoomDao

    companion object {
        @Volatile
        private var INSTANCE: MedicalRoomDatabase? = null

        fun getInstance(context: Context): MedicalRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MedicalRoomDatabase::class.java,
                        "MedicalRoomDatabase"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
