package com.bus.Model

import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bus.Data.DBHelper
import com.bus.Data.Data
import com.bus.MainActivity
import java.sql.Date
import kotlin.coroutines.coroutineContext

class Ticket{

    companion object{
        var counter_id:Int = 1
    }
    var idTicket:Int?=null
    var scanned:Boolean?=null
    var busConnection: BusConnection?=null
    var qrCode:String?= null
    var context: Context? = null

    constructor(context: Context, busConnection: BusConnection, addToData:Boolean){


        this.idTicket = counter_id++
        this.scanned = false
        this.busConnection = busConnection
        this.qrCode = idTicket.toString()+busConnection.idConnection
//        this.qrCode = idTicket.toString()+busConnection.idConnection + busConnection.departureDate
        this.context = context
        if(addToData){
            var db:DBHelper = DBHelper(context)
            db.addTicket(this)
        }

    }
    constructor(busConnection: BusConnection, addToData:Boolean){


        this.idTicket = counter_id++
        this.scanned = false
        this.busConnection = busConnection
        this.qrCode = idTicket.toString()+busConnection.idConnection
//        this.qrCode = idTicket.toString()+busConnection.idConnection + busConnection.departureDate
        this.context = context
        if(addToData){
            var db:DBHelper = DBHelper(context)
            Data.tickets.add(this)
            db.addTicket(this)
        }

    }
    constructor(context:Context, idTicket:Int, scanned: Boolean, busConnection_id:Int, qrCode:String){
        this.idTicket = idTicket
        this.scanned = scanned

        this.context = context

        var dbHelper:DBHelper = DBHelper(context)
        val db = dbHelper.readableDatabase
        val string = String.format("select * from BusConnections where idConnection=%d",busConnection_id)
        var cursor:Cursor = db.rawQuery(string,null)

        try {
            cursor.moveToFirst()

            val idConnection = cursor.getInt(0)
            val departure = cursor.getString(1)
            val arrival = cursor.getString(2)
            val departureDate = cursor.getLong(3)
            val active = cursor.getInt(4)

            val bActive = active==1
            this.busConnection = BusConnection(context,idConnection,BusConnection.Places.valueOf(departure),
                BusConnection.Places.valueOf(arrival), departureDate,bActive)

            this.qrCode = qrCode

        }catch (e:NullPointerException){
            e.stackTrace
        }finally {
            cursor.close()
        }
    }

    fun set(scanned: Boolean?, busConnection: BusConnection){
        this.scanned = scanned
        this.busConnection = busConnection
    }
    fun set(busConnection: BusConnection){
        this.busConnection = busConnection
    }


}