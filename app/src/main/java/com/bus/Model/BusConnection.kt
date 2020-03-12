package com.bus.Model

import android.content.Context
import android.database.Cursor
import com.bus.Data.DBHelper
import com.bus.Data.Data
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList

class BusConnection {

    companion object{
        var counter_id:Int = 1
    }

    enum class Places{
        Warszawa, Gdańsk, Rzeszów, Opole, Zakopane,
        Sanok, Wrocław, Katowice, Gdynia, Lublin,
        Poznań, Szczecin, Kraków, Łódź, Zabrze, Sosnowiec,
        Mielec, Białystok, Bydgoszcz
    }

    var departure:Places?=null
    var arrival:Places?=null
    var departureDate:Long?=null
    var idConnection:Int?=null
    var listTicket:ArrayList<Ticket> = ArrayList()
    var active:Boolean = true

    var context: Context? = null

    constructor(context: Context, departure: Places, arrival: Places, departureDate: Long, addToData:Boolean ) {

        this.idConnection = counter_id++
        this.departure = departure
        this.arrival = arrival
        this.departureDate = departureDate

        if(addToData){
            var db: DBHelper = DBHelper(context)
            Data.busConnections.add(this)
            db.addBusConnection(this)
        }
    }
    constructor(departure: Places, arrival: Places, departureDate: Long, addToData:Boolean ) {

        this.idConnection = counter_id++
        this.departure = departure
        this.arrival = arrival
        this.departureDate = departureDate

        if(addToData){
            var db: DBHelper = DBHelper(context)
            db.addBusConnection(this)
            Data.busConnections.add(this)
        }
    }
    constructor(context: Context,idConnection:Int,departure:Places,arrival:Places,departureDate: Long, active:Boolean){
        this.context = context
        this.idConnection = idConnection
        this.departure = departure
        this.arrival = arrival
        this.departureDate = departureDate
        this.active = active


    }

    fun set(departure: Places, arrival: Places, departureDate: Long ){
        this.departure = departure
        this.arrival = arrival
        this.departureDate = departureDate
    }
}