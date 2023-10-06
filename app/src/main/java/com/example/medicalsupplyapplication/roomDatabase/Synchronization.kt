package com.example.medicalsupplyapplication.roomDatabase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Synchronization {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        val isCellular: Boolean =
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
        val isWiFi: Boolean =
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false


        return networkCapabilities != null && (isCellular || isWiFi)
    }

    suspend fun syncProductDataToRoom(context: Context, coroutineScope: CoroutineScope) {

        val productList: MutableList<Product> = mutableListOf()
        val db = Firebase.firestore
        db.collection("Product")
            .get()
            .addOnSuccessListener { productDatabase ->
                for (product in productDatabase) {
                    val productID: String = product.data["ProductID"].toString()
                    val productName: String = product.data["ProductName"].toString()
                    val price: Int = Integer.parseInt(product.data["Price"].toString())
                    val stock: Int = Integer.parseInt(product.data["Stock"].toString())
                    val desc: String = product.data["Desc"].toString()
                    val brand: String = product.data["Brand"].toString()
                    val category: String = product.data["Category"].toString()
                    val lastUpdated: Long? = product.getTimestamp("LastUpdated")?.toDate()?.time

                    val product = Product(
                        productID,
                        productName,
                        price,
                        stock,
                        desc,
                        brand,
                        category,
                        lastUpdated,
                        false
                    )
                    productList.add(product)
                }

                coroutineScope.launch {
                    saveProductDataToRoom(productList, context)
                }
            }
    }

    private suspend fun saveProductDataToRoom(productList: MutableList<Product>, context: Context) {

        val roomDatabase = MedicalRoomDatabase.getInstance(context)
        val medicalRoomDao = roomDatabase.medicalRoomDao

        for(product in productList){
            medicalRoomDao.insertProduct(product)
        }
    }

    suspend fun syncOrderDataToRoom(context: Context, coroutineScope: CoroutineScope) {

        val orderList: MutableList<Order> = mutableListOf()
        val db = Firebase.firestore
        db.collection("Order")
            .get()
            .addOnSuccessListener { orderDatabase ->
                for (order in orderDatabase) {
                    val orderID: String = order.data["OrderID"].toString()
                    val prodID: String = order.data["ProdID"].toString()
                    val custID: String = order.data["CustID"].toString()
                    val paymentID: String = order.data["PaymentID"].toString()
                    val payTime: Long? = order.getTimestamp("PayTime")?.toDate()?.time
                    val prodName: String = order.data["ProdName"].toString()
                    val prodPrice: Int = Integer.parseInt(order.data["ProdPrice"].toString())
                    val prodQty: Int = Integer.parseInt(order.data["ProdQty"].toString())
                    val lastUpdated: Long? = order.getTimestamp("LastUpdated")?.toDate()?.time


                    val order = Order(
                        orderID,
                        prodID,
                        custID,
                        paymentID,
                        payTime,
                        prodName,
                        prodPrice,
                        prodQty,
                        lastUpdated,
                        false
                    )
                    orderList.add(order)
                }

                coroutineScope.launch {
                    saveOrderDataToRoom(orderList, context)
                }
            }
    }

    private suspend fun saveOrderDataToRoom(orderList: MutableList<Order>, context: Context) {

        val roomDatabase = MedicalRoomDatabase.getInstance(context)
        val medicalRoomDao = roomDatabase.medicalRoomDao

        for(order in orderList){
            medicalRoomDao.insertOrder(order)
        }
    }
}