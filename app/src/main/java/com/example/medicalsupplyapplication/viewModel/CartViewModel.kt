package com.example.medicalsupplyapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalsupplyapplication.Carts
import com.example.medicalsupplyapplication.Database

class CartViewModel : ViewModel() {

    private var _cartObjects = MutableLiveData<MutableList<Carts>>()


    val cartID: LiveData<MutableList<Carts>> get() = _cartObjects

    init {

    }

    fun getFromRemoteDatabase(cartID: String) {
        Database.db.collection("Cart")
            .get()
            .addOnSuccessListener { cartDatabase ->
                for (cart in cartDatabase) {
                    val cartID: String = cart.data["CartID"].toString()
                    val prodID: String = cart.data["ProdID"].toString()
                    val custID: String = cart.data["CustID"].toString()
                    val prodName: String = cart.data["ProdName"].toString()
                    val prodPrice: Int = Integer.parseInt(cart.data["ProdPrice"].toString())
                    val prodQty: Int = Integer.parseInt(cart.data["Qty"].toString())
                    val idNum: Int = Integer.parseInt(cart.data["idNum"].toString())
                    val cart = Carts(cartID, prodID, custID, prodName, prodPrice, prodQty, idNum)
                    _cartObjects.value?.add(cart)
                }
            }
    }




}