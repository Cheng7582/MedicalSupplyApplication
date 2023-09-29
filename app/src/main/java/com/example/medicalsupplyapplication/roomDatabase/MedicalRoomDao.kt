package com.example.medicalsupplyapplication.roomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MedicalRoomDao {

    //----------------------------------------------Admin
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: Admin)

    @Update
    suspend fun updateAdmin(admin: Admin)

    @Query("SELECT * from admin WHERE adminID = :adminID")
    suspend fun getAdmin(adminID: String): Admin?

    @Query("DELETE FROM admin WHERE adminID = :adminID")
    suspend fun deleteAdmin(adminID: String)

    @Query("DELETE FROM admin")
    suspend fun clearAdminTable()

    //------------------------------------------------------------Cart

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(admin: Admin)

    @Update
    suspend fun updateCart(admin: Admin)

    @Query("SELECT * from admin WHERE adminID = :adminID")
    suspend fun getCart(adminID: String): Admin?

    @Query("DELETE FROM admin WHERE adminID = :adminID")
    suspend fun deleteCart(adminID: String)

    @Query("DELETE FROM admin")
    suspend fun clearCartTable()

//    @Query("SELECT * FROM admin ORDER BY nightId DESC LIMIT 1")
//    suspend fun getTonight(): SleepNight?
//
//    @Query("SELECT * FROM admin ORDER BY nightId DESC")
//    fun getAllNights(): LiveData<List<SleepNight>>


}
