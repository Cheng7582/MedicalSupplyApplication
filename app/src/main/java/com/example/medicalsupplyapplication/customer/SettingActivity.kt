package com.example.medicalsupplyapplication.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.medicalsupplyapplication.Login
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.databinding.ActivitySettingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val loginID = intent.getStringExtra("getID")
        val homepage = HomePageActivity()
        val product = ProductPageActivity()
        val cart = CartActivity()
        val setting = SettingActivity()
        val profile = ProfileActivity()
        val login = Login()
        val order = OrderHistoryActivity()
        val reset = ResetPasswordActivity()
        val report = ReportIssueActivity()
        val delivery = DeliveryStatusActivity()

        if(loginID == null || loginID == "") {
            val settingList = binding.settingList
            val names = arrayOf("🔜 Log In")

            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                this, android.R.layout.simple_list_item_1, names
            )

            settingList.adapter = arrayAdapter

            settingList.setOnItemClickListener { _, _, position, _ ->
                if (position == 0) {
                    goOther(login)
                }
            }
        }else{
            val settingList = binding.settingList
            val names = arrayOf("👨🏻 Profile", "🛒 Shopping Cart","🔔 Order History", "🔑 Reset Password","🚚 Delivery Status", "✉️ Contact Us", "🔙 Log Out")

            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                this, android.R.layout.simple_list_item_1, names
            )

            settingList.adapter = arrayAdapter

            settingList.setOnItemClickListener { adapterView, view, position, l ->
                if (position == 0) {
                    goOther(profile)
                }else if (position == 1) {
                    goOther(cart)
                } else if (position == 2) {
                    goOther(order)
                } else if(position == 3){
                    goOther(reset)
                }else if(position == 4){
                    goOther(delivery)
                }else if(position == 5){
                    goOther(report)
                }else if (position == 6) {
                    Firebase.auth.signOut()
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> setNewPage(homepage)
                R.id.ic_products -> setNewPage(product)
                R.id.ic_settings -> setNewPage(setting)
            }
            true
        }
    }

    private fun setNewPage(activity: Activity){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this,activity::class.java)
        intent.putExtra("getID",loginID)
        startActivity(intent)
        finish()
    }

    private fun goOther(activity: Activity){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this,activity::class.java)
        intent.putExtra("getID",loginID)
        startActivity(intent)
    }
}