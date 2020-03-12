package com.bus.Data

import android.content.Context
import com.bus.Model.BusConnection
import com.bus.Model.BusConnection.Places
import com.bus.Model.Ticket
import com.bus.Model.User
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class Data {

    companion object{
        val tickets:ArrayList<Ticket> = ArrayList()
        val busConnections:ArrayList<BusConnection> = ArrayList()
    }
    var context:Context? = null
    constructor(context: Context) {

        this.context = context

        var calendar:Calendar = GregorianCalendar(2020,0,28,7,0,0)
        var calendar2:Calendar = GregorianCalendar(2020,0,28,15,0,0)
        var calendar3:Calendar = GregorianCalendar(2020,0,28,12,0,0)
        var calendar4:Calendar = GregorianCalendar(2020,0,28,11,0,0)
        var calendar5:Calendar = GregorianCalendar(2020,0,28,17,0,0)


        val busConnection1 = BusConnection(context,Places.Szczecin,Places.Wrocław,calendar.timeInMillis,true)
        val busConnection2 = BusConnection(context, Places.Rzeszów,Places.Kraków,calendar2.timeInMillis,true)
        val busConnection3 = BusConnection(context, Places.Rzeszów,Places.Kraków,calendar3.timeInMillis,true)
        val busConnection4 = BusConnection(context, Places.Szczecin,Places.Kraków,calendar4.timeInMillis,true)
        val busConnection5 = BusConnection(context, Places.Białystok,Places.Kraków,calendar5.timeInMillis,true)
        val busConnection6 = BusConnection(context, Places.Rzeszów,Places.Katowice,calendar3.timeInMillis,true)
        val busConnection7 = BusConnection(context, Places.Rzeszów,Places.Białystok,calendar4.timeInMillis,true)
        //val busConnection3 = BusConnection(context, Places.Gdańsk,Places.Warszawa,calendar.timeInMillis,true)

        User(context,"admin","admin",true)
        Ticket(context, busConnection2,true)
        Ticket(context, busConnection1,true)
        Ticket(context, busConnection2,true)
        Ticket(context, busConnection4,true)
        Ticket(context, busConnection4,true)
        Ticket(context, busConnection1,true)
        Ticket(context, busConnection2,true)
        Ticket(context, busConnection3,true)
        Ticket(context, busConnection1,true)
        Ticket(context, busConnection3,true)
        Ticket(context, busConnection4,true)
        Ticket(context, busConnection5,true)
        Ticket(context, busConnection6,true)
        Ticket(context, busConnection7,true)
        Ticket(context, busConnection6,true)
        Ticket(context, busConnection3,true)
        Ticket(context, busConnection1,true)
        Ticket(context, busConnection7,true)

        /*  Ticket(busConnection1,true)
          Ticket(busConnection2,true)
          Ticket(busConnection2,true)
          Ticket(busConnection2,true)
          Ticket(busConnection2,true)
          Ticket(busConnection2,true)
          Ticket(busConnection3,true)
          Ticket(busConnection3,true)
          Ticket(busConnection3,true)
          Ticket(busConnection3,true)
          Ticket(busConnection3,true)
          Ticket(busConnection3,true)
          Ticket(busConnection3,true)
          Ticket(busConnection3,true)
          Ticket(busConnection3,true)
          Ticket(busConnection3,true)*/
    }
}