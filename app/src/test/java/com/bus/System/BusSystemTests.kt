package com.bus.System

import android.app.Instrumentation
import androidx.test.platform.app.InstrumentationRegistry
import com.bus.Data.Data
import com.bus.MainActivity
import com.bus.Model.BusConnection
import com.bus.Model.BusConnection.Places
import com.bus.Model.Ticket
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import java.util.*
import kotlin.collections.ArrayList

class BusSystemTests {

    private var busSystem:BusSystem = BusSystem()
    private val a = MainActivity
    @After
    fun clearData(){
        Data.tickets.clear()
        Data.busConnections.clear()
    }

    @Test
    fun ticketScanWithCorrectlyData(){


        var busConnection =
            a.context?.let { BusConnection(it,Places.Szczecin,Places.Zakopane, 543534L,true) }
        var ticket = busConnection?.let { Ticket(it,true) }

        if (ticket != null) {
            MainActivity.context?.let { busSystem.scanTicket(it,ticket.qrCode.toString()) }?.let {
                assert(
                    it
                )
            }
        }
    }
    @Test
    fun ticketScanWithWrongData(){
       var busConnection =
           a.context?.let { BusConnection(it,Places.Szczecin,Places.Zakopane, 543534L,true) }
        a.context?.let { busSystem.scanTicket(it,"230242342342353454325") }?.let { assertFalse(it) }
    }

    @Test
    fun ticketScanFromAnotherConnection(){
        var busConnection1 =
            a.context?.let { BusConnection(it,Places.Szczecin,Places.Zakopane, 543534L,true) }
        var busConnection2 =
            a.context?.let { BusConnection(it,Places.Szczecin,Places.Zakopane, 543534L,true) }
        val ticket1 = busConnection1?.let { Ticket(it,true) }

        if (ticket1 != null) {
            a.context?.let { busSystem.scanTicket(it,ticket1.qrCode.toString()) }?.let {
                assertFalse(
                    it
                )
            }
        }
    }
    @Test
    fun ticketScanWhenTicketIsScanned(){
        var busConnection1 =
            a.context?.let { BusConnection(it,Places.Szczecin,Places.Zakopane, 543534L,true) }
        var ticket = busConnection1?.let { Ticket(it,true) }


        if (ticket != null) {
            a.context?.let { busSystem.scanTicket(it,ticket.qrCode.toString()) }
        }
        if (ticket != null) {
            ticket.qrCode?.let { a.context?.let { it1 -> busSystem.scanTicket(it1, it) } }?.let { assertFalse(it) }
        }
    }
    @Test
    fun fieldScannedChangesToTrueAfterScan(){
        var busConnection1 =
            a.context?.let { BusConnection(it,Places.Szczecin,Places.Zakopane, 543534L,true) }
        var ticket = busConnection1?.let { Ticket(it,true) }

        if (ticket != null) {
            assert(ticket.scanned == false)
        }

        val qrCode = ticket?.qrCode
        a.context?.let { busSystem.scanTicket(it,qrCode.toString()) }

        if (ticket != null) {
            assert(ticket.scanned == true)
        }
    }








}