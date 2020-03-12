package com.bus

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.bus.Data.DBHelper
import com.bus.Data.Data
import com.bus.Model.BusConnection
import com.bus.Model.Ticket
import com.bus.System.BusSystem
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.days
import kotlin.time.seconds

open class MainActivity : AppCompatActivity() {

    companion object{
        var context: Context? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TextViews do testów
        //var textView:TextView = findViewById(R.id.textView)
        //var textView2:TextView = findViewById(R.id.textView2)
        //var textView3:TextView = findViewById(R.id.textView3)
        context = this
        //Stworzenie helpera do bazy danych
        var db:DBHelper = DBHelper(this)
        //Wyczyszczenie bazy dancyh, by uniknąć redundancji przy każdo razowym włączeniu programu
        db.clearDb()

        //Stworzenie danych na podstawie modeli i dodanie ich do bazy danych
        Data(this)
//
//        //Testowo wypełnienie textView2 wszystkimi BusConnectionami
//        //fillTextViewWithBusConnections(textView2)
//
//        //Pobranie z bazy danych przejazdu o id 1
//        val busconnection = db.getBusConnection(1)
//        //Pobranie z bazy danych listy biletów z przejazdy pobranego wyżej
//        var listTicket = db.getListTicket(busconnection)
//        //Stworzenie obiektu BusSystem z którego można wywołać metodę skanowania biletu
//        val busSystem:BusSystem = BusSystem()
//        //Zeskanowanie pierwszego biletu z listy pobranej wyżej
//        busSystem.scanTicket(this,listTicket[0].qrCode.toString())
//        //Pobranie znów listy biletów z przejazdu podanego wyżej(Bez tej operacji w zmiennej listTicket byłby bilet sprzed wywołania metody 'scanTicket')
//        listTicket = db.getListTicket(busconnection)
//        //Sprawdzenie czy faktycznie bilet został zeskanowany
//        //textView3.text = listTicket[0].scanned.toString()
//
//        //Wypełnienie textView danymi o biletach
//        //fillTextViewWithTickets(textView)
        loginButton.setOnClickListener {

            val login = loginText.text
            val password = passwordText.text

            val busSystem = BusSystem()

            if(busSystem.login(this,login.toString(),password.toString())){
                val intent = Intent(this,Connection_activity::class.java)
                startActivity(intent)
            }else{
                val toast:Toast = Toast.makeText(this,"Niepoprawne dane logowania",Toast.LENGTH_LONG)
                toast.show()
            }



        }


    }
    fun fillTextViewWithTickets(textView: TextView){
        var db:DBHelper = DBHelper(this)
        var k:Cursor = db.cursorTickets
        var text:String = String()
        while(k.moveToNext()){
            var id = k.getString(0)
            var scanned = k.getString(1)
            var busId = k.getString(2)
            var qrCode = k.getString(3)


            text = text+"Id biletu: "+id+" Czy zeskanowany: "+scanned +" Id przejazdu: "+busId+" QrCode: "+qrCode+"\n"
        }
        textView.text = text
        k.close()
    }
    fun fillTextViewWithBusConnections(textView: TextView){
        var db:DBHelper = DBHelper(this)
        var k:Cursor = db.cursorBusConnections
        var text:String = String()
        while(k.moveToNext()){
            var id = k.getString(0)
            var departure = k.getString(1)
            var arrival = k.getString(2)
            var departureDate = k.getLong(3)
            var active = k.getInt(4)

            val bActive = active==1

            text = text+"Id przejazdu: "+id+" Miejsce odjazdu: "+departure +" Miejsce przyjazdu: "+arrival+" Data odjazdu: "+Date(departureDate)+" Active: "+bActive+"\n"
        }
        textView.text = text
        k.close()
    }
    fun fillTextViewWithBusConnectionsTickets(textView: TextView){
        var db:DBHelper = DBHelper(this)
        var k:Cursor = db.cursorBusConnectionsTickets
        var text:String = String()
        while(k.moveToNext()){
            var busConnectionId = k.getString(0)
            var ticketId = k.getString(1)


            text = text+busConnectionId+" "+ticketId +"\n"
            db.readableDatabase

        }
        textView.text = text
        k.close()
    }
}
