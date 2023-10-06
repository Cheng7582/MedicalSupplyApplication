package com.example.medicalsupplyapplication.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MedicalRoomDao {

    //------------------------------------------------------------Product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * from product WHERE productID = :productID")
    suspend fun getProduct(productID: String): Product?

    @Query("SELECT * from product")
    suspend fun getAllProduct(): MutableList<Product>?

    @Query("DELETE FROM product WHERE productID = :productID")
    suspend fun deleteProduct(productID: String)

    @Query("DELETE FROM product")
    suspend fun clearProductTable()

    @Query("SELECT COUNT(*) FROM product")
    suspend fun getProductTableSize() : Int

    //------------------------------------------------------------Order
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Update
    suspend fun updateOrder(order: Order)

    @Query("SELECT * from 'order' WHERE orderID = :orderID")
    suspend fun getOrder(orderID: String): Order?

    @Query("SELECT * from 'order'")
    suspend fun getAllOrder(): MutableList<Order>?

    @Query("DELETE FROM 'order' WHERE orderID = :orderID")
    suspend fun deleteOrder(orderID: String)

    @Query("DELETE FROM 'order'")
    suspend fun clearOrderTable()

    @Query("SELECT COUNT(*) FROM 'order'")
    suspend fun getOrderTableSize() : Int
}
