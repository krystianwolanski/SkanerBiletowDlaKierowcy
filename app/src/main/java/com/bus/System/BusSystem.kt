package com.bus.System

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.bus.Data.DBHelper
import com.bus.Model.BusConnection
import com.bus.Model.Ticket

class BusSystem {

    fun scanTicket(context: Context, qrCode:String):Boolean{
        var dbHelper: DBHelper = DBHelper(context)
        val db = dbHelper.writableDatabase

        val sql1 = String.format("Select id from Tickets where qrCode='%s' and scanned=0",qrCode)

        //val sql1 = String.format("Select Tickets.id from Tickets where qrCode='%s' and scanned=0 and busConnection_id=%d",qrCode,1)

        val cursor = db.rawQuery(sql1,null)
        if(cursor.count>0){
            val values = ContentValues()
            values.put("scanned",1)
            var args = arrayOf(qrCode)
            Log.d("args",args.toString())
            db.update("Tickets",values,"qrCode=?",args)

            db.close()
            return true
        }
        db.close()
        return false
    }
    fun login(context:Context,login:String,password:String):Boolean{
        var dbHelper: DBHelper = DBHelper(context)
        return dbHelper.userExist(login,password)
    }

    fun getScannedTickets(busConnection: BusConnection): ArrayList<Ticket>{
        var scannedTickets:ArrayList<Ticket> = ArrayList()

        for(t:Ticket in busConnection.listTicket){
            if(t.scanned == true)
                scannedTickets.add(t)
        }

        return scannedTickets
    }

    fun getUnScannedTickets(busConnection: BusConnection): ArrayList<Ticket>{
        var unScannedTickets:ArrayList<Ticket> = ArrayList()

        for(t:Ticket in busConnection.listTicket){
            if(t.scanned == false)
                unScannedTickets.add(t)
        }
        return unScannedTickets
    }
}