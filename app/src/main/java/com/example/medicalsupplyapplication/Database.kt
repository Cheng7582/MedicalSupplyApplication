package com.example.medicalsupplyapplication

import android.content.ContentValues
import android.util.Log
import com.example.medicalsupplyapplication.database.model.Product
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date

class Database {
    companion object{
        val db = Firebase.firestore
        var customers: MutableList<Customers> = mutableListOf()
        var admins: MutableList<Admins> = mutableListOf()
        var products: MutableList<Products> = mutableListOf()
        var carts: MutableList<Carts> = mutableListOf()
        var orders: MutableList<Orders> = mutableListOf()
        var dates: MutableList<GetRegisterDate> = mutableListOf()
        var pdates: MutableList<GetOrderDate> = mutableListOf()
        var reports: MutableList<Reports> = mutableListOf()
        var deliverys: MutableList<Deliverys> = mutableListOf()
        var deliverystatuss: MutableList<DeliveryStatuss> = mutableListOf()

        fun getAllDataFromFirestore(){
            db.collection("Customer")
                .get()
                .addOnSuccessListener { custDatabase ->
                    for(customer in custDatabase){
                        val custData = Customers(customer.data["CustID"].toString(),
                            customer.data["Username"].toString(),
                            customer.data["Email"].toString(),
                            customer.data["Phone"].toString(),
                            customer.data["Password"].toString(),
                            customer.data["ConfirmPass"].toString(),
                            customer.data["Address"].toString())
                        val timestamp = customer["RegisterDate"] as com.google.firebase.Timestamp
                        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                        val sdf = SimpleDateFormat("MM/dd/yyyy")
                        val netDate = Date(milliseconds)
                        val date = sdf.format(netDate).toString()
                        val dateValue = GetRegisterDate(date)


                        dates.add(dateValue)
                        customers.add(custData)
                    }
                }

            db.collection("Admin")
                .get()
                .addOnSuccessListener { adminDatabase ->
                    for(admin in adminDatabase){
                        val adminData = Admins(admin.data["AdminID"].toString(),
                            admin.data["AdminName"].toString(),
                            admin.data["Email"].toString(),
                            admin.data["Phone"].toString(),
                            admin.data["Password"].toString(),
                            admin.data["ConfirmPass"].toString())
                        admins.add(adminData)
                    }
                }

            db.collection("Product")
                .get()
                .addOnSuccessListener { productDatabase ->
                    for(product in productDatabase){
                        val productData = Products(product.data["ProductID"].toString(),
                            product.data["ProductName"].toString(),
                            Integer.parseInt(product.data["Price"].toString()),
                            Integer.parseInt(product.data["Stock"].toString()),
                            product.data["Desc"].toString(),
                            product.data["Brand"].toString(),
                            product.data["Category"].toString())
                        products.add(productData)
                    }
                }

            db.collection("Cart")
                .get()
                .addOnSuccessListener { cartDatabase ->
                    for(cart in cartDatabase){
                        val cartData = Carts(cart.data["CartID"].toString(),
                            cart.data["ProdID"].toString(),
                            cart.data["CustID"].toString(),
                            cart.data["ProdName"].toString(),
                            Integer.parseInt(cart.data["ProdPrice"].toString()),
                            Integer.parseInt(cart.data["Qty"].toString()),
                            Integer.parseInt(cart.data["idNum"].toString())
                        )
                        carts.add(cartData)
                    }
                }

            db.collection("Order")
                .get()
                .addOnSuccessListener { orderDatabase ->
                    for(order in orderDatabase){
                        val orderData = Orders(order.data["OrderID"].toString(),
                            order.data["ProdID"].toString(),
                            order.data["CustID"].toString(),
                            order.data["PaymentID"].toString(),
                            order.data["ProdName"].toString(),
                            Integer.parseInt(order.data["ProdPrice"].toString()),
                            Integer.parseInt(order.data["ProdQty"].toString())
                        )

                        var tPrice = Integer.parseInt(order.data["ProdPrice"].toString()) * Integer.parseInt(
                            order.data["ProdQty"].toString()
                        )

                        val timestamp = order["PayTime"] as com.google.firebase.Timestamp
                        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                        val sdf = SimpleDateFormat("MM/dd/yyyy")
                        val netDate = Date(milliseconds)
                        val date = sdf.format(netDate).toString()
                        val dateValue = GetOrderDate(date,
                            Integer.parseInt(order.data["ProdQty"].toString()),
                            tPrice)

                        pdates.add(dateValue)
                        orders.add(orderData)
                    }
                }

            db.collection("Report")
                .get()
                .addOnSuccessListener { reportDatabase ->
                    for(report in reportDatabase){
                        val reportData = Reports(report.data["rID"].toString(),
                            report.data["CustID"].toString(),
                            report.data["CustEmail"].toString(),
                            report.data["IssueTitle"].toString(),
                            report.data["IssueDesc"].toString())
                        reports.add(reportData)
                    }
                }

            db.collection("Delivery")
                .get()
                .addOnSuccessListener { deliveryDatabase ->
                    for(delivery in deliveryDatabase){
                        val deliveryData = Deliverys(delivery.data["DeliveryID"].toString(),
                            delivery.data["CustID"].toString(),
                            delivery.data["PayID"].toString(),
                            delivery.data["Status"].toString())
                        deliverys.add(deliveryData)
                    }
                }

            db.collection("DeliveryStatus")
                .get()
                .addOnSuccessListener { deliverySDatabase ->
                    for(delivery in deliverySDatabase){
                        val timestamp = delivery["Date"] as com.google.firebase.Timestamp
                        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                        val sdf = SimpleDateFormat("dd MMM")
                        val netDate = Date(milliseconds)
                        val date = sdf.format(netDate).toString()
                        val deliverysData = DeliveryStatuss(delivery.data["DsID"].toString(),
                            delivery.data["DeliveryID"].toString(),
                            date,
                            delivery.data["Desc"].toString())
                        deliverystatuss.add(deliverysData)
                    }
                }
        }

        fun addCustomer(custID:String,username:String, custEmail:String,
                        custPhone:String, custPass:String, custConfPass:String, address:String){
            var newCustomer = Customers(custID,username,custEmail,custPhone,custPass,custConfPass,address)
            customers.add(newCustomer)

            val addNewData = hashMapOf(
                "CustID" to custID,
                "Username" to username,
                "Email" to custEmail,
                "Phone" to custPhone,
                "Password" to custPass,
                "ConfirmPass" to custConfPass,
                "Status" to "Active",
                "RegisterDate" to FieldValue.serverTimestamp(),
                "Address" to address
            )

            db.collection("Customer").document(custID)
                .set(addNewData)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error add new data",it)
                }
        }

        fun addAdmin(adminID:String, adminName:String, adminEmail:String,
                     adminPhone:String, adminPass:String, adminConfPass:String){
            var newAdmin = Admins(adminID,adminName,adminEmail,adminPhone,adminPass,adminConfPass)
            admins.add(newAdmin)

            val addNewData = hashMapOf(
                "AdminID" to adminID,
                "AdminName" to adminName,
                "Email" to adminEmail,
                "Phone" to adminPhone,
                "Password" to adminPass,
                "ConfirmPass" to adminConfPass
            )

            db.collection("Admin").document(adminID)
                .set(addNewData)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error add new data",it)
                }
        }

        fun addProduct(prodID:String, prodName:String, price:Int, stock:Int, desc:String, brand: String, category: String){
            var newProduct = Products(prodID,prodName,price,stock,desc,brand,category)
            products.add(newProduct)

            val addNewData = hashMapOf(
                "ProductID" to prodID,
                "ProductName" to prodName,
                "Price" to price,
                "Stock" to stock,
                "Desc" to desc,
                "Brand" to brand,
                "Category" to category
            )

            db.collection("Product").document(prodID)
                .set(addNewData)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error add new data",it)
                }
        }

        fun addCart(cartID:String, prodID:String, custID:String, prodName:String, prodPrice:Int, prodQty:Int, idNum:Int){
            var newCart = Carts(cartID,prodID,custID,prodName,prodPrice,prodQty,idNum)
            carts.add(newCart)

            val addNewData = hashMapOf(
                "CartID" to cartID,
                "ProdID" to prodID,
                "CustID" to custID,
                "ProdName" to prodName,
                "ProdPrice" to prodPrice,
                "Qty" to prodQty,
                "idNum" to idNum
            )

            db.collection("Cart").document(cartID)
                .set(addNewData)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error add new data",it)
                }
        }

        fun addOrder(orderID:String, prodID:String, custID:String, paymentID:String, prodName:String, prodPrice:Int, prodQty:Int){
            var newOrder = Orders(orderID,prodID,custID,paymentID,prodName,prodPrice,prodQty)
            orders.add(newOrder)

            val addNewData = hashMapOf(
                "OrderID" to orderID,
                "ProdID" to prodID,
                "CustID" to custID,
                "PaymentID" to paymentID,
                "ProdName" to prodName,
                "ProdPrice" to prodPrice,
                "ProdQty" to prodQty,
                "PayTime" to FieldValue.serverTimestamp()
            )

            db.collection("Order").document(orderID)
                .set(addNewData)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error add new data",it)
                }
        }

        fun updateCustomer(custID:String,username:String, custEmail:String,
                           custPhone:String, custPass:String, custConfPass:String,address: String, getPosition: Int){
            var updatedCustomer = Customers(custID,username,custEmail,custPhone,custPass,custConfPass,address)
            customers.set(getPosition, updatedCustomer)

            val updateCustData = mapOf(
                "CustID" to custID,
                "Username" to username,
                "Email" to custEmail,
                "Phone" to custPhone,
                "Password" to custPass,
                "ConfirmPass" to custConfPass,
                "Address" to address
            )

            db.collection("Customer").document(custID)
                .update(updateCustData)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error update data",it)
                }
        }

        fun updateAdmin(adminID:String, adminName:String, adminEmail:String,
                        adminPhone:String, adminPass:String, adminConfPass:String, getPosition: Int){
            var updatedAdmin = Admins(adminID,adminName,adminEmail,adminPhone,adminPass,adminConfPass)
            admins.set(getPosition,updatedAdmin)

            val updateAdminData = mapOf(
                "AdminID" to adminID,
                "AdminName" to adminName,
                "Email" to adminEmail,
                "Phone" to adminPhone,
                "Password" to adminPass,
                "ConfirmPass" to adminConfPass
            )

            db.collection("Admin").document(adminID)
                .update(updateAdminData)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error update data",it)
                }
        }

        fun updateProduct(prodID:String, prodName:String, price:Int, stock:Int, desc:String, brand:String, category:String, getPosition: Int){
            var updatedProduct = Products(prodID,prodName,price,stock,desc,brand,category)
            products.set(getPosition,updatedProduct)

            val updateProductData = mapOf(
                "ProductID" to prodID,
                "ProductName" to prodName,
                "Price" to price,
                "Stock" to stock,
                "Desc" to desc,
                "Brand" to brand,
                "Category" to category,
                "LastUpdated" to FieldValue.serverTimestamp()
            )

            db.collection("Product").document(prodID)
                .update(updateProductData)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot added successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error update data",it)
                }

        }

        fun deleteCustomer(custID: String){
            db.collection("Customer").document(custID)
                .delete()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot deleted successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error delete data",it)
                }
        }

        fun deleteAdmin(adminID: String){
            db.collection("Admin").document(adminID)
                .delete()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot deleted successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error delete data",it)
                }
        }

        fun deleteProduct(prodID: String){
            db.collection("Product").document(prodID)
                .delete()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot deleted successfully!")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG,"Error delete data",it)
                }
        }
    }
}

class Customers{
    private var custID:String
    private var username:String
    private var email:String
    private var phone:String
    private var password:String
    private var confirmPass:String
    private var address: String

    constructor(){
        custID = ""
        username = ""
        email = ""
        phone = ""
        password = ""
        confirmPass = ""
        address = ""
    }

    constructor(CustID:String, Username:String, Email:String, Phone:String,
                Password:String, ConfirmPass:String, Address:String){
        custID = CustID
        username = Username
        email = Email
        phone = Phone
        password = Password
        confirmPass = ConfirmPass
        address = Address
    }

    fun getID():String{
        return custID
    }

    fun getName():String{
        return username
    }

    fun getEmail():String{
        return email
    }

    fun getPhone():String{
        return phone
    }

    fun getPassword():String{
        return password
    }

    fun getConfirmPass():String{
        return confirmPass
    }

    fun getAddress():String{
        return address
    }
}

class Admins{
    private var adminID:String
    private var adminName:String
    private var email:String
    private var phone:String
    private var password:String
    private var confirmPass:String

    constructor(){
        adminID = ""
        adminName = ""
        email = ""
        phone = ""
        password = ""
        confirmPass = ""
    }

    constructor(AdminID:String, AdminName:String, Email:String, Phone:String,
                Password:String, ConfirmPass:String){
        adminID = AdminID
        adminName = AdminName
        email = Email
        phone = Phone
        password = Password
        confirmPass = ConfirmPass
    }

    fun getID():String{
        return adminID
    }

    fun getName():String{
        return adminName
    }

    fun getEmail():String{
        return email
    }

    fun getPhone():String{
        return phone
    }

    fun getPassword():String{
        return password
    }

    fun getConfirmPass():String{
        return confirmPass
    }
}

class Products{
    private var productID:String
    private var productName:String
    private var price:Int
    private var stock:Int
    private var desc:String
    private var brand:String
    private var category:String

    constructor(){
        productID = ""
        productName = ""
        price = 0
        stock = 0
        desc = ""
        brand = ""
        category = ""
    }

    constructor(ProductID:String, ProductName:String, Price:Int, Stock:Int, Desc:String, Brand:String, Category:String){
        productID = ProductID
        productName = ProductName
        price = Price
        stock = Stock
        desc = Desc
        brand = Brand
        category = Category
    }

    fun getID():String{
        return productID
    }

    fun getName():String{
        return productName
    }

    fun getPrice():Int{
        return price
    }

    fun getStock():Int{
        return stock
    }

    fun getDesc():String{
        return desc
    }

    fun getBrand():String{
        return brand
    }

    fun getCategory():String{
        return category
    }
}

class Carts{
    private var cartID:String
    private var prodID:String
    private var custID:String
    private var prodName:String
    private var prodPrice:Int
    private var prodQty:Int
    private var idNum:Int

    constructor(){
        cartID = ""
        prodID = ""
        custID = ""
        prodName = ""
        prodPrice = 0
        prodQty = 0
        idNum = 0
    }

    constructor(CartID:String, ProdID:String, CustID:String, ProdName:String, ProdPrice:Int, ProdQty:Int, IDNum:Int){
        cartID = CartID
        prodID = ProdID
        custID = CustID
        prodName = ProdName
        prodPrice = ProdPrice
        prodQty = ProdQty
        idNum = IDNum
    }

    fun getCartID():String{
        return cartID
    }

    fun getProdID():String{
        return prodID
    }

    fun getCustID():String{
        return custID
    }

    fun getProdName():String{
        return prodName
    }

    fun getProdPrice():Int{
        return prodPrice
    }

    fun getProdQty():Int{
        return prodQty
    }

    fun getIDNum():Int{
        return idNum
    }
}

class Orders{
    private var orderID:String
    private var prodID:String
    private var custID:String
    private var paymentID:String
    private var prodName:String
    private var prodPrice:Int
    private var prodQty:Int

    constructor(){
        orderID = ""
        prodID = ""
        custID = ""
        paymentID = ""
        prodName = ""
        prodPrice = 0
        prodQty = 0
    }

    constructor(OrderID:String, ProdID:String, CustID:String, PaymentID:String, ProdName:String, ProdPrice:Int, ProdQty:Int){
        orderID = OrderID
        prodID = ProdID
        custID = CustID
        paymentID = PaymentID
        prodName = ProdName
        prodPrice = ProdPrice
        prodQty = ProdQty
    }

    fun getOrderID():String{
        return orderID
    }

    fun getProdID():String{
        return prodID
    }

    fun getCustID():String{
        return custID
    }

    fun getPaymentID():String{
        return paymentID
    }

    fun getProdName():String{
        return prodName
    }

    fun getProdPrice():Int{
        return prodPrice
    }

    fun getProdQty():Int{
        return prodQty
    }
}

class GetRegisterDate{
    private var date:String

    constructor(){
        date = ""
    }

    constructor(Date:String){
        date = Date
    }

    fun getDate():String{
        return date
    }
}

class GetOrderDate{
    private var date:String
    private var qty:Int
    private var price:Int

    constructor(){
        date = ""
        qty = 0
        price = 0
    }

    constructor(Date:String,Qty:Int,Price:Int){
        date = Date
        qty = Qty
        price = Price
    }

    fun getDate():String{
        return date
    }

    fun getQty():Int{
        return qty
    }

    fun getPrice():Int{
        return price
    }
}

class Reports{
    private var rId:String
    private var cId:String
    private var cEmail:String
    private var iTitle:String
    private var iDesc:String

    constructor(){
        rId = ""
        cId = ""
        cEmail = ""
        iTitle = ""
        iDesc = ""
    }

    constructor(RId:String,CId:String,CEmail:String,ITitle:String,IDesc:String){
        rId = RId
        cId = CId
        cEmail = CEmail
        iTitle = ITitle
        iDesc = IDesc
    }

    fun getRID():String{
        return rId
    }

    fun getCID():String{
        return cId
    }

    fun getCEmail():String{
        return cEmail
    }

    fun getITitle():String{
        return iTitle
    }

    fun getIDesc():String{
        return iDesc
    }
}

class Deliverys{
    private var dId:String
    private var cId:String
    private var pID:String
    private var status:String

    constructor(){
        dId = ""
        cId = ""
        pID = ""
        status = ""
    }

    constructor(DId:String,CId:String,PID:String,Status:String){
        dId = DId
        cId = CId
        pID = PID
        status = Status
    }

    fun getDID():String{
        return dId
    }

    fun getCID():String{
        return cId
    }

    fun getPID():String{
        return pID
    }

    fun getStatus():String{
        return status
    }
}

class DeliveryStatuss{
    private var dsId:String
    private var dId:String
    private var date:String
    private var desc:String

    constructor(){
        dsId = ""
        dId = ""
        date = ""
        desc = ""
    }

    constructor(DsId:String,DId:String,Date:String,Desc:String){
        dsId = DsId
        dId = DId
        date = Date
        desc = Desc
    }

    fun getDID():String{
        return dId
    }

    fun getDsID():String{
        return dsId
    }

    fun getDate():String{
        return date
    }

    fun getDesc():String{
        return desc
    }
}