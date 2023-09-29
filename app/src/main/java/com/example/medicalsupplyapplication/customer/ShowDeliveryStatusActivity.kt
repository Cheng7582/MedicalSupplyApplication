package com.example.medicalsupplyapplication.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.databinding.ActivityShowDeliveryStatusBinding
import java.text.SimpleDateFormat
import java.util.*

class ShowDeliveryStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowDeliveryStatusBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<DeliveryStatusAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDeliveryStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getDeliveryID = intent.getStringExtra("getDeliveryID")
        val loginID = intent.getStringExtra("getID")

        Database.db.collection("Delivery").whereEqualTo("DeliveryID",getDeliveryID)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    binding.Status.text = doc.get("Status").toString()
                }
            }

        val showstatus: MutableList<showStatus> = mutableListOf()

        Database.db.collection("DeliveryStatus").whereEqualTo("DeliveryID",getDeliveryID)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val desc = doc.get("Desc").toString()
                    val timestamp = doc["Date"] as com.google.firebase.Timestamp
                    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                    val sdf = SimpleDateFormat("dd MMM")
                    val netDate = Date(milliseconds)
                    val date = sdf.format(netDate).toString()

                    var newDeliveryStatus = showStatus(date,desc)
                    showstatus.add(newDeliveryStatus)
                }

                layoutManager = LinearLayoutManager(this)

                binding.recyclerViewDelivery.layoutManager = layoutManager

                DeliveryStatusAdapter.setFragment(loginID.toString(),this)

                adapter = DeliveryStatusAdapter(this, showstatus)

                binding.recyclerViewDelivery.adapter = adapter
            }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,DeliveryStatusActivity::class.java)
            intent.putExtra("getID",loginID)
            startActivity(intent)
        }
    }
}

class DeliveryStatusAdapter(val context: ShowDeliveryStatusActivity, val items: List<showStatus>): RecyclerView.Adapter<DeliveryStatusAdapter.ViewHolder>() {
    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : ShowDeliveryStatusActivity

        fun setFragment(getID: String, activityActi: ShowDeliveryStatusActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.show_delivery_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.date.text = item.getDID()
        holder.status.text = item.getStatus()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var date: TextView
        var status: TextView

        init {
            date = itemView.findViewById(R.id.showDate)
            status = itemView.findViewById(R.id.statusDesc)
        }
    }
}

class showStatus{
    private var date:String
    private var desc:String

    constructor(){
        date = ""
        desc = ""
    }

    constructor(Date:String,Desc:String){
        date = Date
        desc = Desc
    }

    fun getDID():String{
        return date
    }

    fun getStatus():String{
        return desc
    }
}