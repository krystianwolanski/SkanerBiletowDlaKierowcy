package com.bus

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.set
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.bus.Adapter.ListTicketAdapter
import com.bus.Data.DBHelper
import com.bus.Data.Data
import com.bus.Model.BusConnection
import com.bus.Model.Ticket
import com.bus.System.BusSystem
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_preview.*
import java.lang.Integer.parseInt
import java.util.*

import kotlin.collections.ArrayList

class Preview : AppCompatActivity() {
    var busConnection_id = 1

    internal var lstTicket_skasowane:MutableList<Ticket> = ArrayList<Ticket>()
    internal var lstTicket_nieskasowane:MutableList<Ticket> = ArrayList<Ticket>()
    internal var lstTicket:MutableList<Ticket> = ArrayList<Ticket>()

    var scannedResult = ""
    var db:DBHelper = DBHelper(this)
    var busSystem:BusSystem = BusSystem()
//    var data:Data = Data(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)


        refreshData()


        //manualne wprowadzenie biletu
        // ticket_number
        btn_use_ticket_manually.setOnClickListener {
            if(busSystem.scanTicket(this,ticket_number.text.toString())){
                dialogcreate("Bilet poprawny: "+ticket_number.text)
                ticket_number.text.clear()
                refreshData()
                hideKeyboard()
            }else{
                dialogcreate("Bilet niepoprawny: "+ticket_number.text)
            }
        }


        //zeskanuj bilet
        btn_use_ticket.setOnClickListener {
            kotlin.run {
                IntentIntegrator(this).initiateScan();
            }
        }


        //przycisk zakoncz ladowanie
        btn_finish.setOnClickListener {
            val builder = AlertDialog.Builder(this@Preview)

            // Display a message on alert dialog
            builder.setMessage("Koniec skanowania biletów")

            // Display a neutral button on alert dialog
            builder.setPositiveButton("Potwierdź"){_,_ ->
                val builder2 = AlertDialog.Builder(this@Preview)
                builder2.setMessage("Koniec skanowania biletów\nZeskanowanych: "+txt_skasowane.text.toString()+"\nBrakujące: "+txt_nieskasowane.text.toString())
                builder2.setPositiveButton("Ok"){_,_ ->
                    //Log.d("idBusConnection",busConnection.idConnection.toString())
                    //db.setActive(busConnection,false)
                    this.finish()
                }
                val dialog: AlertDialog = builder2.create()
                dialog.show()
            }
            builder.setNegativeButton("Wstecz") { _, _ ->
                Toast.makeText(this,"Nie zakończono skanowania",Toast.LENGTH_SHORT).show()
            }
            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()

        }

    }


    var ilosc = 0
    var skasowane = 0
    var nieskasowane = 0

    // funkcja do listy biletow
    private fun refreshData(){
        //lstPerson = db.allPerson
        val departure = intent.getStringExtra("departure")
        val arrival = intent.getStringExtra("arrival")
        val hour = intent.getIntExtra("hour",0)
        val minutes = intent.getIntExtra("minutes",0)

        var calendar: Calendar = GregorianCalendar(2020,0,28,hour,minutes,0)

        val busConnection = db.getBusConnection(departure,arrival,calendar,true)

        if(busConnection!=null){
            lstTicket = db.getListTicket(busConnection)

            lstTicket_skasowane.clear()
            lstTicket_nieskasowane.clear()
            for (t in lstTicket){
                if(t.scanned!!){
                    lstTicket_skasowane.add(t)
                }else if (!t.scanned!!){
                    lstTicket_nieskasowane.add(t)
                }
            }
            val adapter = ListTicketAdapter(this, lstTicket)
            listTicket.adapter = adapter

            txt_ilosc.text = lstTicket.size.toString()
            txt_skasowane.text = lstTicket_skasowane.size.toString()
            txt_nieskasowane.text = lstTicket_nieskasowane.size.toString()

            //db.setActive(busConnection,false)

        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null){
            if(result.contents != null){
                scannedResult = result.contents
                if(busSystem.scanTicket(this,scannedResult)){
                    dialogcreate("Bilet poprawny: "+scannedResult)
                }else{
                    dialogcreate("Bilet niepoprawny: "+scannedResult)
                }
                refreshData()
            } else {
                Toast.makeText(this,"scan failed",Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("scannedResult", scannedResult)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            scannedResult = it.getString("scannedResult").toString()
            //txtValue.text = scannedResult
        }
    }

    fun dialogcreate(mess:String){
        val builder = AlertDialog.Builder(this@Preview)

        // Display a message on alert dialog
        builder.setMessage(mess)

        // Display a neutral button on alert dialog
        builder.setNeutralButton("Ok"){_,_ ->
            Toast.makeText(applicationContext,"Bilet został skasowany.",Toast.LENGTH_SHORT).show()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    //ukrywanie klawiatury
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //wstecz podwojne klikniecie
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()


        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 1500)
    }


}
