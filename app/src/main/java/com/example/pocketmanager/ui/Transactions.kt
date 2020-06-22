package com.example.pocketmanager.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketmanager.R
import com.example.pocketmanager.data.SMS
import java.io.Serializable
import java.util.*

class Transactions : AppCompatActivity() {
    var transactions: ArrayList<SMS>? = null
    private val context: Context = this
    // To display list of transactions
    var transList: RecyclerView? = null
    // Custom adapter to link recycler view
    private var myAdapter: MyAdapter? = null
    // card type and color of the card received from the main activity are store in these var
    private var color: String? = null
    private var card: String? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transactions)
        val intent: Intent = getIntent()
        val bundle = intent.getBundleExtra("DATA")
        card = bundle?.getString("CARD")
        color = bundle?.getString("COLOR")
        if (card == "BANK_CARD") {
            transactions =
                bundle?.getSerializable("SMS") as ArrayList<SMS>?
        }
        // To add a custom action bar
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(Color.parseColor(color))
        transList = findViewById<RecyclerView>(R.id.transList)
        transList?.setHasFixedSize(true)
        transList?.setLayoutManager(LinearLayoutManager(this))
        myAdapter = MyAdapter(transactions, context)
        transList?.setAdapter(myAdapter)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // setting the result cashList back to the main activity on back button press
    override fun onBackPressed() {
        sendDataBack()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bank_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> sendDataBack()
            R.id.forward -> {
                val smsList1: MutableList<SMS> =
                    ArrayList()
                for (s in transactions!!) {
                    if (s.drOrCr == "DR") {
                        smsList1.add(s)
                    }
                }
                // when forward action button is clicked a bar chart is displayed whose values are calculated here
                if (smsList1.size > 0) {
                    val i = Intent(this@Transactions, Report::class.java)
                    val b = Bundle()
                    b.putSerializable("SMS", smsList1 as Serializable)
                    // color is sent to the report activity depending on click of bank or cash card
                    b.putString("color", color)
                    i.putExtra("DATA", b)
                    startActivity(i)
                } else { // if no messages are there then a toast is displayed
                    Toast.makeText(
                        this@Transactions,
                        "You have not spent money",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendDataBack() {
        finish()
    }
}