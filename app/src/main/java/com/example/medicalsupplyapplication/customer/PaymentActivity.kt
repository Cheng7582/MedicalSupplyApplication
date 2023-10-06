package com.example.medicalsupplyapplication.customer

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.databinding.ActivityPaymentBinding
import com.google.firebase.firestore.FieldValue
import kotlin.random.Random

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.creditCardIcon.setImageResource(R.drawable.ic_payment)

        binding.backToSArrow.setOnClickListener {
            val loginID = intent.getStringExtra("getID")
            val intent = Intent(this,CartActivity::class.java)
            intent.putExtra("getID",loginID)
            startActivity(intent)
            finish()
        }

        binding.paymentBtn.setOnClickListener{
            val cardNumber=binding.addCarNumber.text.toString()
            val holderName=binding.addHolderName.text.toString()
            val expiryDate=binding.addExpiryDate.text.toString()
            val cvv=binding.addCvv.text.toString()

            if (cardNumber == "" ||!cardNumber.matches(Regex("^\\d{16}$"))) {
                Toast.makeText(this, "Please Enter Valid Card Number.", Toast.LENGTH_SHORT).show()
                binding.addCarNumber.requestFocus()
            } else if (holderName == ""||!holderName.matches(Regex("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*\$"))) {
                Toast.makeText(this, "Please Enter Valid Card Holder Name.", Toast.LENGTH_SHORT).show()
                binding.addHolderName.requestFocus()
            } else if (expiryDate == ""|| !expiryDate.matches(Regex("^(0[1-9]|1[0-2])/(\\d{2})$")) ) {
                Toast.makeText(this, "Please Enter Valid Expiry Date.", Toast.LENGTH_SHORT).show()
                binding.addExpiryDate.requestFocus()
            } else if (cvv == "" || !cvv.matches(Regex("^(\\d{3,4})$"))) {
                Toast.makeText(this, "Please Enter Valid CVV.", Toast.LENGTH_SHORT).show()
                binding.addCvv.requestFocus()
            } else{
                val loginID = intent.getStringExtra("getID")

                Database.db.collection("Cart").whereEqualTo("CustID",loginID).get().addOnSuccessListener {
                    val randomNumber = Random.nextInt(100_000, 1_000_000)
                    val p0 = "Pay_${randomNumber.toString().padStart(5, '0')}"
                    for(doc in it){
                        val cartID = doc.get("CartID").toString()
                        val prodID = doc.get("ProdID").toString()
                        val prodName = doc.get("ProdName").toString()
                        val prodPrice = Integer.parseInt(doc.get("ProdPrice").toString())
                        val qty = Integer.parseInt(doc.get("Qty").toString())


                        val idNum = Database.orders.size
                        val orderID = "OD" + String.format("%04d",(idNum +1))
                        Database.addOrder(orderID,prodID,loginID.toString(),p0.toString(),prodName,prodPrice,qty)
                        Toast.makeText(this, "Payment Successfully", Toast.LENGTH_SHORT).show();

                        Database.db.collection("Cart").document(cartID)
                            .delete()
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG, "Remove Cart Items successfully!")
                                Database.db.collection("Product").whereEqualTo("ProductID",prodID).get().addOnSuccessListener {
                                    for(doc in it){
                                        var newQty = Integer.parseInt(doc.get("Stock").toString()) - qty
                                        Database.db.collection("Product").document(prodID)
                                            .update("Stock",newQty)
                                            .addOnSuccessListener {
                                                Log.d(ContentValues.TAG,"Success update")
                                            }
                                    }
                                }

                                val dId = Database.deliverys.size + 1
                                val deID = "DR" + String.format("%04d",dId)
                                val payID = p0.toString()
                                val addNewDelivery = hashMapOf(
                                    "DeliveryID" to deID,
                                    "CustID" to loginID,
                                    "PayID" to payID,
                                    "Status" to "Preparing")

                                Database.db.collection("Delivery").document(deID)
                                    .set(addNewDelivery)
                                    .addOnSuccessListener {
                                        Log.d(ContentValues.TAG, "Success add")
                                    }.addOnFailureListener {
                                        Log.d(ContentValues.TAG,"Error add delivery data",it)
                                    }

                                val dsId = Database.deliverystatuss.size + 1
                                val dsID = "DS" + String.format("%06d",dsId)
                                val addNewStatus = hashMapOf(
                                    "DsID" to dsID,
                                    "DeliveryID" to deID,
                                    "Date" to FieldValue.serverTimestamp(),
                                    "Desc" to "Preparing to ship your parcel."
                                )

                                Database.db.collection("DeliveryStatus").document(dsID)
                                    .set(addNewStatus)
                                    .addOnSuccessListener {
                                        Log.d(ContentValues.TAG, "Success add")
                                        val intent = Intent(this, SettingActivity::class.java)
                                        intent.putExtra("getID",loginID)
                                        startActivity(intent)
                                    }.addOnFailureListener {
                                        Log.d(ContentValues.TAG,"Error add delivery data",it)
                                    }

                            }.addOnFailureListener {
                                Log.d(ContentValues.TAG,"Error delete data",it)
                            }
                    }
                }
            }

        }
    }
}