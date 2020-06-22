package com.example.pocketmanager.ui

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketmanager.R
import com.example.pocketmanager.data.DatabaseHandler
import com.example.pocketmanager.data.SMS
import com.example.pocketmanager.permission.PermissionManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import java.lang.Double
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val context: Context = this
    var intentFilter = IntentFilter(SMS_RECEIVED)
    var databaseHandler = DatabaseHandler(context)

    // A list to store bank list and cash sms
    private val bankList: MutableList<SMS> = ArrayList()

    // this is used to filter already read messages
    var FILTER: String? = null

    // In many places the balance is needed in String form.
    private var BALANCE: String? = "0.0"

    private val sms_receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            if (intent.action == SMS_RECEIVED) {
                readMessages()
                Toast.makeText(this@MainActivity, "Reading New SMS...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)
    }

    private fun fetchingPocketData() {
        if (PermissionManager.checkPermission(this)) {
            val savedData =
                getSharedPreferences("KEY", Context.MODE_PRIVATE)
            if (savedData.getString("BALANCE", "") != "") BALANCE =
                savedData.getString("BALANCE", "")
            if (BALANCE == "0.0") {
                val bal_dialog = Dialog(context)
                bal_dialog.setCancelable(false)
                bal_dialog.setContentView(R.layout.balance_dialog)
                val smsBal = SMS()
                smsBal.msgType = "Bank Balance"
                smsBal.msgDate = java.lang.Long.toString(System.currentTimeMillis())
                bal_dialog.setTitle("Current Bank Balance -")
                val etBalance =
                    bal_dialog.findViewById<View>(R.id.Balance) as EditText
                val saveBalance =
                    bal_dialog.findViewById<View>(R.id.save) as Button
//                bal_dialog.show()
                saveBalance.setOnClickListener {
                    BALANCE = etBalance.text.toString()
                    if (BALANCE?.trim { it <= ' ' } == "") {
                        BALANCE = "0.0"
                    }
                    bal_dialog.dismiss()
                    smsBal.msgAmt = BALANCE
                    smsBal.msgBal = BALANCE
                    bankList.add(0, smsBal)
                    databaseHandler.addBankSms(smsBal)
                    setBankBalance()
                }
            }
            try { // object read is in the form of list<Sms> so iterate over the list to extract all Sms objects.
                for (r in databaseHandler.getAllSms("bankTransactions")) {
                    bankList.add(r)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // getting the saved filter
            FILTER = savedData.getString("FILTER", "")
            setBankBalance()
            bankCard.setOnClickListener(this)
            bankReport?.setOnClickListener(this)
            readMessages()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bankCard -> if (bankList.size > 0) {
                val bank = Intent(this@MainActivity, Transactions::class.java)
                val b = Bundle()
                b.putSerializable("SMS", bankList as Serializable)
                b.putString("CARD", "BANK_CARD")
                b.putString("COLOR", "#6ed036")
                bank.putExtra("DATA", b)
                startActivity(bank)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "No Bank Transactions to display",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.bankReport -> if (getSpendList(bankList).isNotEmpty()) startFragment() else {
                Toast.makeText(
                    this@MainActivity,
                    "Not Enough Data To Display",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startFragment() {
        val bundle = Bundle()
        bundle.putSerializable("SPEND LIST", getSpendList(bankList) as Serializable)
    }

    private fun getSpendList(originalList: List<SMS>?): List<SMS> {
        val spentList: MutableList<SMS> = ArrayList()
        for (s in originalList!!) {
            if (s.drOrCr.equals("DR") && s.day.equals(
                    SimpleDateFormat(getString(R.string.dateFormat), Locale.ENGLISH).format(
                        Date(System.currentTimeMillis())
                    )
                )
            ) {
                spentList.add(s)
            }
        }
        return spentList
    }

    private fun readMessages() { // new sms object declared
        var sms: SMS
        // read sms are stored in cursor
        val c = contentResolver.query(
            Uri.parse("content://sms/inbox"),
            arrayOf("date", "body"),
            FILTER,
            null,
            null
        )
        val total = c?.count
        // all messages are read from bottom because when new sms gets inserted they are inserted in the position zero
// thus to keep the latest messages up in the list
        if (c?.moveToLast()!!) {
            loop@ for (i in 0 until total!!) {
                sms = SMS()
                // body and date read from cursor
                val date = c.getString(c.getColumnIndexOrThrow("date"))
                var body = c.getString(c.getColumnIndexOrThrow("body"))
                // keeping track of a filter to prevent reading of messages already read
                FILTER = "date>$date"
                var type = ""
                // date is set to the sms object
                sms.msgDate = date
                body = body.toLowerCase(Locale.getDefault())
                if (!Pattern.compile("(recharge|paytm|ola)").matcher(body).find()) {
                    if (Pattern.compile("(debit|transaction|withdrawn)").matcher(
                            body
                        ).find()
                    ) type =
                        "Personal Expenses" else if (Pattern.compile("(credit|deposited)").matcher(
                            body
                        ).find()
                    ) type = "Income"
                }
                when (type) {
                    "Personal Expenses" -> {
                        sms.msgType = type
                        val a = getAmount(body)
                        // getAmount is a method which gives the amount using pattern and matcher
                        if (a != null) {
                            sms.msgAmt = a
                            sms.msgBal =
                                Double.toString(BALANCE?.toDouble()!! - a.toDouble())
                            BALANCE = sms.msgBal
                            bankList.add(0, sms)
                            databaseHandler.addBankSms(sms)
                        } else {
                            c.moveToPrevious()
                            continue@loop
                        }
                    }
                    "Income" -> {
                        sms.msgType = type
                        val a1 = getAmount(body)
                        if (a1 != null) {
                            sms.msgAmt = a1
                            sms.msgBal =
                                Double.toString(BALANCE?.toDouble()!! + a1.toDouble())
                            BALANCE = sms.msgBal
                            bankList.add(0, sms)
                            databaseHandler.addBankSms(sms)
                        } else {
                            c.moveToPrevious()
                            continue@loop
                        }
                    }
                }
                c.moveToPrevious()
            }
        } else { // if no messages to read than a toast is displayed
            Toast.makeText(this@MainActivity, "No sms to read?", Toast.LENGTH_SHORT).show()
        }
        c.close()
        setBankBalance()
    }

    // getting amount by matching the pattern
    fun getAmount(data: String?): String? { // pattern - rs. **,***.**
        val pattern1 = "(inr)+[\\s]?+[0-9]*+[\\\\,]*+[0-9]*+[\\\\.][0-9]{2}"
        val regex1 = Pattern.compile(pattern1)
        // pattern - inr **,***.**
        val pattern2 = "(rs)+[\\\\.][\\s]*+[0-9]*+[\\\\,]*+[0-9]*+[\\\\.][0-9]{2}"
        val regex2 = Pattern.compile(pattern2)
        val matcher1 = regex1.matcher(data)
        val matcher2 = regex2.matcher(data)
        if (matcher1.find()) {
            try {
                var a = matcher1.group(0)
                a = a?.replace("inr", "")
                a = a?.replace(" ", "")
                a = a?.replace(",", "")
                return a
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (matcher2.find()) {
            try { // searched for rs or inr preceding number in the form of rs. **,***.**
                var a = matcher2.group(0)
                a = a?.replace("rs", "")
                a = a?.replaceFirst(".".toRegex(), "")
                a = a?.replace(" ", "")
                a = a?.replace(",", "")
                return a
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun setBankBalance() = if (bankList.size > 0) {
        bankBalance?.text = "Rs ${bankList[0].msgBal}"
        estimateDate?.text = bankList[0].formatDate
    } else {
        bankBalance?.text = getString(R.string.initAmount)
        estimateDate?.text = " "
    }

    override fun onResume() {
        super.onResume()
        fetchingPocketData()
        // registering a BroadcastReceiver to listen to incoming messages
        registerReceiver(sms_receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        save()
        unregisterReceiver(sms_receiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        save()
    }

    fun save() { // saving the total cash spent and the bank balance
        val saveSpent =
            getSharedPreferences("KEY", Context.MODE_PRIVATE)
        val editor = saveSpent.edit()
        editor.putString("BALANCE", BALANCE)
        editor.putString("FILTER", FILTER)
        editor.apply()
    }

    companion object {
        // BroadcastReceiver listening to the incoming messages
        private const val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    }
}