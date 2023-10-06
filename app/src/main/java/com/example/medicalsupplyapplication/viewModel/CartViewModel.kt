package com.example.medicalsupplyapplication.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalsupplyapplication.database.model.Cart

class CartViewModel (private val cartRepository: CartRepository) : ViewModel(){

    private var _cart: MediatorLiveData<Cart> = MediatorLiveData()
    var cart: MediatorLiveData<Cart>
        get() = _cart
        set(value){
            _cart = value
        }

    private var _cartList: MediatorLiveData<MutableList<Cart>> = MediatorLiveData()
    var cartList: MediatorLiveData<MutableList<Cart>>
        get() = _cartList
        set(value) {
            _cartList = value
        }

    constructor() : this(CartRepository()) {
    }

    init {
        _cartList.addSource(cartRepository.getCartList()){ newCartList->
            _cartList.value = newCartList
        }

        _cart.addSource(cartRepository.getCart()){ newCart->
            _cart.value = newCart
        }
    }

    fun initOnlineCartList(custID: String) {
        cartRepository.getCartListFirestore(custID)
    }

    fun initOnlineCart(prodID: String, custID: String) {
        cartRepository.getSingleCartFirestore(prodID, custID)
    }

    fun addSingleCartOnline(cart: Cart){
        cartRepository.addSingleCartFirestore(cart)
    }

    fun updateSingleCartOnline(cart: Cart){
        cartRepository.updateSingleCartFirestore(cart)
    }
}