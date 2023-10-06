package com.example.medicalsupplyapplication.viewModel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.database.model.Order
import com.example.medicalsupplyapplication.roomDatabase.Order as RoomOrder
import com.example.medicalsupplyapplication.roomDatabase.MedicalRoomDatabase
import com.google.firebase.Timestamp
import java.util.Date

class OrderRepository {

    private var order: MutableLiveData<Order> = MutableLiveData()
    private var orderList: MutableLiveData<MutableList<Order>> = MutableLiveData()

    fun getOrderListFirestore(): MutableLiveData<MutableList<Order>> {
        val orders: MutableList<Order> = mutableListOf()

        Database.db.collection("Order")
            .get()
            .addOnSuccessListener { orderDatabase ->
                for (order in orderDatabase) {
                    val orderID: String = order.data["OrderID"].toString()
                    val prodID: String = order.data["ProdID"].toString()
                    val custID: String = order.data["CustID"].toString()
                    val paymentID: String = order.data["PaymentID"].toString()
                    val payTime: Timestamp? = order.getTimestamp("PayTime")
                    val prodName: String = order.data["ProdName"].toString()
                    val prodPrice: Int = Integer.parseInt(order.data["ProdPrice"].toString())
                    val prodQty: Int = Integer.parseInt(order.data["ProdQty"].toString())
                    val lastUpdated: Timestamp? = order.getTimestamp("LastUpdated")

                    val order = Order(
                        orderID,
                        prodID,
                        custID,
                        paymentID,
                        payTime,
                        prodName,
                        prodPrice,
                        prodQty,
                        lastUpdated
                    )
                    orders.add(order)
                }
                orderList.postValue(orders)
            }
        return orderList
    }

    suspend fun getOrderListRoom(context: Context): MutableLiveData<MutableList<Order>> {
        var orders: MutableList<Order> = mutableListOf()

        val roomDatabase = MedicalRoomDatabase.getInstance(context)
        val medicalRoomDao = roomDatabase.medicalRoomDao

        var roomOrders: MutableList<RoomOrder>? = medicalRoomDao.getAllOrder()

        if (roomOrders != null) {
            for (roomOrder in roomOrders) {
                val lastUpdated: Timestamp = Timestamp(Date(roomOrder.lastUpdated ?: 0))
                val payTime: Timestamp = Timestamp(Date(roomOrder.payTime ?: 0))

                val order = Order(
                    roomOrder.orderID,
                    roomOrder.prodID,
                    roomOrder.custID,
                    roomOrder.paymentID,
                    payTime,
                    roomOrder.prodName,
                    roomOrder.prodPrice,
                    roomOrder.prodQty,
                    lastUpdated
                )

                orders.add(order)
            }
            orderList.postValue(orders)
        }
        return orderList
    }

    fun getSingleOrderFirestore(orderID: String): MutableLiveData<Order> {

        Database.db.collection("Order")
            .get()
            .addOnSuccessListener { orderDatabase ->
                for (order in orderDatabase) {
                    if (orderID == order.data["OrderID"].toString()) {
                        val orderID: String = order.data["OrderID"].toString()
                        val prodID: String = order.data["ProdID"].toString()
                        val custID: String = order.data["CustID"].toString()
                        val paymentID: String = order.data["PaymentID"].toString()
                        val payTime: Timestamp? = order.getTimestamp("PayTime")
                        val prodName: String = order.data["ProdName"].toString()
                        val prodPrice: Int = Integer.parseInt(order.data["ProdPrice"].toString())
                        val prodQty: Int = Integer.parseInt(order.data["ProdQty"].toString())
                        val lastUpdated: Timestamp? = order.getTimestamp("LastUpdated")

                        val order = Order(
                            orderID,
                            prodID,
                            custID,
                            paymentID,
                            payTime,
                            prodName,
                            prodPrice,
                            prodQty,
                            lastUpdated
                        )
                        this.order.postValue(order)
                    }
                }
            }
        return order
    }

    fun updateSingleOrderFirestore(order: Order) {
        val orderData = mapOf(
            "OrderID" to order.orderID,
            "ProdID" to order.prodID,
            "CustID" to order.custID,
            "PaymentID" to order.paymentID,
            "PayTime" to order.payTime,
            "ProdName" to order.prodName,
            "ProdPrice" to order.prodPrice,
            "ProdQty" to order.prodQty,
            "LastUpdated" to order.lastUpdated
        )

        Database.db.collection("Order").document(order.orderID)
            .update(orderData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error update data", it)
            }
    }

    fun addSingleOrderFirestore(order: Order) {
        val orderData = mapOf(
            "OrderID" to order.orderID,
            "ProdID" to order.prodID,
            "CustID" to order.custID,
            "PaymentID" to order.paymentID,
            "PayTime" to order.payTime,
            "ProdName" to order.prodName,
            "ProdPrice" to order.prodPrice,
            "ProdQty" to order.prodQty,
            "LastUpdated" to order.lastUpdated
        )

        Database.db.collection("Order").document(order.orderID)
            .set(orderData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error add new data", it)
            }
    }

    fun getOrderList(): MutableLiveData<MutableList<Order>> {
        return orderList
    }
    fun getOrder(): MutableLiveData<Order> {
        return order
    }


}