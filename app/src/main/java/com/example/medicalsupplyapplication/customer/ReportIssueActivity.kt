package com.example.medicalsupplyapplication.customer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicalsupplyapplication.databinding.ActivityReportIssueBinding

class ReportIssueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportIssueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginID = intent.getStringExtra("getID")

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            intent.putExtra("getID",loginID)
            startActivity(intent)
        }

        binding.submitBtn.setOnClickListener {
            val iTitle = binding.issueTitle.text.toString().trim()
            val iDesc = binding.reportDesc.text.toString().trim()
            val email = "medicalsupply202205@gmail.com".trim()

            if(iTitle == ""){
                Toast.makeText(this, "Please enter the email subject.", Toast.LENGTH_SHORT).show()
                binding.issueTitle.requestFocus()
            }else if(iDesc == ""){
                Toast.makeText(this, "Please enter message.", Toast.LENGTH_SHORT).show()
                binding.reportDesc.requestFocus()
            }else{
                val intent = Intent(Intent.ACTION_SEND)

                intent.data = Uri.parse("mailto:")
                intent.type = "message/rfc822"
                intent.putExtra(Intent.EXTRA_EMAIL,arrayOf(email))
                intent.putExtra(Intent.EXTRA_SUBJECT,iTitle)
                intent.putExtra(Intent.EXTRA_TEXT,iDesc)

                startActivity(Intent.createChooser(intent,"Choose email"))

                Toast.makeText(this, "Email sent.", Toast.LENGTH_SHORT).show()
                val bIntent = Intent(this,SettingActivity::class.java)
                bIntent.putExtra("getID",loginID)
                startActivity(bIntent)
            }
        }

    }
}