package com.bus.Model

import com.bus.Model.BusConnection.Places
import com.bus.Data.Data
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.*

class BusConnectionTest {


    @Test
    fun notAddToTheListWhenAddToDataIsFalse(){
        BusConnection(Places.Białystok,Places.Lublin, 3443L,false)

        assert(Data.busConnections.isEmpty())
    }



}