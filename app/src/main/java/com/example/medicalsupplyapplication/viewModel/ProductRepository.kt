package com.example.medicalsupplyapplication.viewModel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.database.model.Product as Product
import com.example.medicalsupplyapplication.roomDatabase.Product as RoomProduct
import com.example.medicalsupplyapplication.roomDatabase.MedicalRoomDatabase
import com.google.firebase.Timestamp
import java.util.Date

class ProductRepository {
    private var product: MutableLiveData<Product> = MutableLiveData()
    private var productList: MutableLiveData<MutableList<Product>> = MutableLiveData()

    fun getProductListFirestore(): MutableLiveData<MutableList<Product>> {
        val products: MutableList<Product> = mutableListOf()

        Database.db.collection("Product")
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
                    val lastUpdated: Timestamp? = product.getTimestamp("LastUpdated")

                    val product = Product(
                        productID,
                        productName,
                        price,
                        stock,
                        desc,
                        brand,
                        category,
                        lastUpdated
                    )
                    products.add(product)
                }
                productList.postValue(products)
            }
        return productList
    }

    suspend fun getProductListRoom(context: Context): MutableLiveData<MutableList<Product>> {
        var products: MutableList<Product> = mutableListOf()

        val roomDatabase = MedicalRoomDatabase.getInstance(context)
        val medicalRoomDao = roomDatabase.medicalRoomDao

        var roomProducts: MutableList<RoomProduct>? = medicalRoomDao.getAllProduct()

        if (roomProducts != null) {
            for (roomProduct in roomProducts) {
                val lastUpdated: Timestamp = Timestamp(Date(roomProduct.lastUpdated ?: 0))

                val product = Product(
                    roomProduct.productID,
                    roomProduct.productName,
                    roomProduct.price,
                    roomProduct.stock,
                    roomProduct.desc,
                    roomProduct.brand,
                    roomProduct.category,
                    lastUpdated
                )
                products.add(product)
            }
            productList.postValue(products)
        }
        return productList
    }

    fun getSingleProductFirestore(productID: String): MutableLiveData<Product> {

        Database.db.collection("Product")
            .get()
            .addOnSuccessListener { productDatabase ->
                for (product in productDatabase) {
                    if (productID == product.data["ProductID"].toString()) {
                        val productID: String = product.data["ProductID"].toString()
                        val productName: String = product.data["ProductName"].toString()
                        val price: Int = Integer.parseInt(product.data["Price"].toString())
                        val stock: Int = Integer.parseInt(product.data["Stock"].toString())
                        val desc: String = product.data["Desc"].toString()
                        val brand: String = product.data["Brand"].toString()
                        val category: String = product.data["Category"].toString()
                        val lastUpdated: Timestamp? = product.getTimestamp("LastUpdated")

                        val product = Product(
                            productID,
                            productName,
                            price,
                            stock,
                            desc,
                            brand,
                            category,
                            lastUpdated
                        )
                        this.product.postValue(product)
                    }
                }
            }
        return product
    }

    fun updateSingleProductFirestore(product: Product) {
        val productData = mapOf(
            "ProductID" to product.productID,
            "ProductName" to product.productName,
            "Price" to product.price,
            "Stock" to product.stock,
            "Desc" to product.desc,
            "Brand" to product.brand,
            "Category" to product.category,
            "LastUpdated" to product.lastUpdated
        )

        Database.db.collection("Product").document(product.productID)
            .update(productData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error update data", it)
            }
    }

    fun addSingleProductFirestore(product: Product) {
        val productData = mapOf(
            "ProductID" to product.productID,
            "ProductName" to product.productName,
            "Price" to product.price,
            "Stock" to product.stock,
            "Desc" to product.desc,
            "Brand" to product.brand,
            "Category" to product.category,
            "LastUpdated" to product.lastUpdated
        )

        Database.db.collection("Product").document(product.productID)
            .set(productData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error add new data", it)
            }
    }

    fun getProductList(): MutableLiveData<MutableList<Product>> {
        return productList
    }

    fun getProduct(): MutableLiveData<Product> {
        return product
    }


}