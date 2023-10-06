package com.example.medicalsupplyapplication.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.database.model.Cart
import com.google.firebase.Timestamp

class CartRepository {

    private var cart: MutableLiveData<Cart> = MutableLiveData()
    private var cartList: MutableLiveData<MutableList<Cart>> = MutableLiveData()

    fun getCartListFirestore(custID: String): MutableLiveData<MutableList<Cart>> {
        val carts: MutableList<Cart> = mutableListOf()

        Database.db.collection("Cart")
            .get()
            .addOnSuccessListener { cartDatabase ->
                for (cart in cartDatabase) {
                    if (custID == cart.data["CustID"].toString()) {
                        val cartID: String = cart.data["CartID"].toString()
                        val prodID: String = cart.data["ProdID"].toString()
                        val custID: String = cart.data["CustID"].toString()
                        val prodName: String = cart.data["ProdName"].toString()
                        val prodPrice: Int = Integer.parseInt(cart.data["ProdPrice"].toString())
                        val qty: Int = Integer.parseInt(cart.data["Qty"].toString())
                        val idNum: Int = Integer.parseInt(cart.data["idNum"].toString())
                        val lastUpdated: Timestamp? = cart.getTimestamp("LastUpdated")

                        val cart = Cart(
                            cartID,
                            prodID,
                            custID,
                            prodName,
                            prodPrice,
                            qty,
                            idNum,
                            lastUpdated
                        )
                        carts.add(cart)
                    }
                }
                cartList.postValue(carts)
            }
        return cartList
    }

    fun getSingleCartFirestore(prodID: String, custID: String): MutableLiveData<Cart> {

        Database.db.collection("Cart")
            .get()
            .addOnSuccessListener { cartDatabase ->
                for (cart in cartDatabase) {
                    if (prodID == cart.data["ProdID"].toString() && custID == cart.data["CustID"].toString()) {
                        val cartID: String = cart.data["CartID"].toString()
                        val prodID: String = cart.data["ProdID"].toString()
                        val custID: String = cart.data["CustID"].toString()
                        val prodName: String = cart.data["ProdName"].toString()
                        val prodPrice: Int = Integer.parseInt(cart.data["ProdPrice"].toString())
                        val qty: Int = Integer.parseInt(cart.data["Qty"].toString())
                        val idNum: Int = Integer.parseInt(cart.data["idNum"].toString())
                        val lastUpdated: Timestamp? = cart.getTimestamp("LastUpdated")

                        val cart = Cart(
                            cartID,
                            prodID,
                            custID,
                            prodName,
                            prodPrice,
                            qty,
                            idNum,
                            lastUpdated
                        )
                        this.cart.postValue(cart)
                    }
                }
            }
        return cart
    }

    fun updateSingleCartFirestore(cart: Cart) {
        val cartData = mapOf(
            "CartID" to cart.cartID,
            "ProdID" to cart.prodID,
            "CustID" to cart.custID,
            "ProdName" to cart.prodName,
            "ProdPrice" to cart.prodPrice,
            "Qty" to cart.prodQty,
            "idNum" to cart.idNum,
            "LastUpdated" to cart.lastUpdated
        )

        Database.db.collection("Cart").document(cart.cartID)
            .update(cartData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error update data", it)
            }
    }

    fun addSingleCartFirestore(cart: Cart) {
        val cartData = mapOf(
            "CartID" to cart.cartID,
            "ProdID" to cart.prodID,
            "CustID" to cart.custID,
            "ProdName" to cart.prodName,
            "ProdPrice" to cart.prodPrice,
            "Qty" to cart.prodQty,
            "idNum" to cart.idNum,
            "LastUpdated" to cart.lastUpdated
        )

        Database.db.collection("Cart").document(cart.cartID)
            .set(cartData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Error add new data", it)
            }
    }

    fun getCartList(): MutableLiveData<MutableList<Cart>> {
        return cartList
    }

    fun getCart(): MutableLiveData<Cart> {
        return cart
    }
}