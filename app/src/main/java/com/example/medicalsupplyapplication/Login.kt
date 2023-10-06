package com.example.medicalsupplyapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.medicalsupplyapplication.admin.DashboardActivity
import com.example.medicalsupplyapplication.customer.*
import com.example.medicalsupplyapplication.databinding.ActivityLoginBinding
import com.example.medicalsupplyapplication.roomDatabase.Synchronization
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val coroutineScope: CoroutineScope = lifecycleScope


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        signUpActivityLink()


        binding.backToSArrow.setOnClickListener {
            val loginID = intent.getStringExtra("getID")
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("getID", loginID)
            startActivity(intent)
        }

        binding.showpass.setOnClickListener {
            if (binding.showpass.contentDescription.toString().equals("show")) {
                binding.enterPass.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.showpass.contentDescription = "Hide"
            } else {
                binding.enterPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showpass.contentDescription = "show"
            }
        }

        binding.loginBtn.setOnClickListener {
            val synchronization = Synchronization()

            if(synchronization.isNetworkAvailable(this)){
                coroutineScope.launch { onClick() }
            }else{
                Toast.makeText(this, "Ops you are offline!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signUpActivityLink() {
        val linkTextView = findViewById<TextView>(R.id.signup)
        linkTextView.setTextColor(Color.BLUE)
        linkTextView.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }


    suspend fun onClick() {

        val email = binding.enterCustID.text.toString()
        val password = binding.enterPass.text.toString()


        val isCustLoginSuccess = coroutineScope.async {
            val customerSnapshot =
                Database.db.collection("Customer").whereEqualTo("Email", email).get().await()

            for (customerDoc in customerSnapshot) {
                if (customerDoc.get("Status") == "Active" && password == customerDoc.data["Password"].toString()) {
                    return@async arrayOf(true, customerDoc.get("CustID").toString())
                }
            }

            return@async arrayOf(false, null)
        }


        val isAdminLoginSuccess = coroutineScope.async {
            val adminSnapshot =
                Database.db.collection("Admin").whereEqualTo("AdminID", email).get().await()

            for (adminDoc in adminSnapshot) {
                if (password == adminDoc.data["Password"].toString()) {
                    return@async arrayOf(true, adminDoc.data["AdminID"].toString())
                }
            }

            return@async arrayOf(false, null)
        }

        val custResult = isCustLoginSuccess.await()
        val adminResult = isAdminLoginSuccess.await()

        if (custResult[0] as Boolean) {
            Toast.makeText(this, "Customer Login Successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomePageActivity::class.java)
            val custID = custResult[1].toString()
            intent.putExtra("getID", custID)
            startActivity(intent)

        } else if (adminResult[0] as Boolean) {
            Toast.makeText(this, "Admin Login Successful!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("getID", adminResult.toString())
            startActivity(intent)

        } else {
            Toast.makeText(this, "Email or Password Incorrect!!!", Toast.LENGTH_SHORT).show()
        }


    }

}