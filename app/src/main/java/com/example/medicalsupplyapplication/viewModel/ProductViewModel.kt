package com.example.medicalsupplyapplication.viewModel

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.medicalsupplyapplication.database.model.Product

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private var _product: MediatorLiveData<Product> = MediatorLiveData()
    var product: MediatorLiveData<Product>
        get() = _product
        set(value){
            _product = value
        }

    private var _productList: MediatorLiveData<MutableList<Product>> = MediatorLiveData()
    var productList: MediatorLiveData<MutableList<Product>>
        get() = _productList
        set(value) {
            _productList = value
        }

    constructor() : this(ProductRepository()) {
    }

    init {
        _productList.addSource(productRepository.getProductList()){ newProductList->
            _productList.value = newProductList
        }

        _product.addSource(productRepository.getProduct()){ newProduct->
            _product.value = newProduct
        }
    }

    fun initOnlineProductList() {

        productRepository.getProductListFirestore()
    }

    fun initOnlineProduct(productID: String) {
        productRepository.getSingleProductFirestore(productID)
    }

    fun addSingleProductOnline(product: Product){
        productRepository.addSingleProductFirestore(product)
    }

    fun updateSingleProductOnline(product: Product){
        productRepository.updateSingleProductFirestore(product)
    }

    suspend fun initOfflineProductList(context: Context) {
        productRepository.getProductListRoom(context)
    }
}