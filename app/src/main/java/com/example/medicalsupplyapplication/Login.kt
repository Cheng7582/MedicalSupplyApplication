package com.example.medicalsupplyapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
import android.widget.Toast
import com.example.medicalsupplyapplication.admin.DashboardActivity
import com.example.medicalsupplyapplication.customer.*
import com.example.medicalsupplyapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


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
            onClick()
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


    fun onClick() {

        var loginUnsucess: Boolean = false

        val email = binding.enterCustID.text.toString()
        val password = binding.enterPass.text.toString()

        Database.db.collection("Customer").whereEqualTo("Email", email).get().addOnSuccessListener {
            for (doc in it) {
                if (doc.get("Status") == "Active") {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Database.db.collection("Customer").whereEqualTo("Email", email).get()
                                .addOnSuccessListener {
                                    for (doc in it) {
                                        val user = Firebase.auth.currentUser

                                        Toast.makeText(
                                            this,
                                            "Login Successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(this, HomePageActivity::class.java)
                                        val custID = doc.get("CustID").toString()
                                        intent.putExtra("getID", custID)
                                        Database.db.collection("Customer").document(custID)
                                            .update(
                                                "Password", password,
                                                "ConfirmPass", password
                                            )
                                            .addOnSuccessListener {
                                                startActivity(intent)
                                                loginUnsucess = true
                                            }

                                    }
                                }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Email or Password Incorrect!!!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else if (doc.get("Status") == "Inactive") {
                    Toast.makeText(this, "Account Inactive!", Toast.LENGTH_SHORT).show()
                    binding.enterCustID.requestFocus()
                }
            }
        }


        for (admin in Database.admins) {
            if (admin.getID() == binding.enterCustID.text.toString() && admin.getPassword() == binding.enterPass.text.toString()) {
                val ID = binding.enterCustID.text.toString()
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("getID", ID)
                startActivity(intent)
                loginUnsucess = true
            }
        }

    }
}