package com.bus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.bus.Data.DBHelper
import com.bus.Model.BusConnection
import kotlinx.android.synthetic.main.activity_second.*
import java.util.*
import kotlin.collections.ArrayList
//import jdk.nashorn.internal.objects.NativeDate.getTime
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.text.SimpleDateFormat


class Connection_activity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    var hours = arrayOf("7:00", "10:00", "15:00")
    var db: DBHelper = DBHelper(this)
    var lstBusConnection:MutableList<BusConnection> = ArrayList()
    var lstDeparture:MutableList<String> = ArrayList()
    var lstArrival:MutableList<String> = ArrayList()
    var lstHours:MutableList<Long> = ArrayList()
    var spinner: Spinner? = null
    var map:Map<String,String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        lstBusConnection = db.listBusConnectionAll
        for (i in lstBusConnection){
            //Log.d("args",busConnection.departure.toString())
            if(!lstArrival.contains(i.arrival.toString())){
                lstArrival.add(i.arrival.toString())
            }
            if(!lstDeparture.contains(i.departure.toString())){
                lstDeparture.add(i.departure.toString())
            }
            if(!lstHours.contains(i.departureDate!!.toLong())){
                lstHours.add(i.departureDate!!.toLong())
            }



        }

        // spinner_connections -> miejsce wyjazdu
        // spinner_connections2 -> miejsce docelowe
        // spinner_connections3 -> godzina odjazdu
        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, lstDeparture)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //spinner1 symulacja dzialania
        spinner = this.spinner_connections
        spinner!!.setOnItemSelectedListener(this)
        spinner!!.setAdapter(aa)

        //aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, lstArrival)
        //spinner 2
        var cc = ArrayAdapter(this, android.R.layout.simple_spinner_item, lstArrival)
        cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.spinner_connections2!!.setAdapter(cc)

        //spinner 3
        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, lstHours)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner = this.spinner_connections3
        spinner!!.setAdapter(bb)



        //przycisk wyboru polaczenia
        btn_choose_connection.setOnClickListener {
            //Toast.makeText(this, "Kliknieto przycisk", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Preview::class.java)
            intent.putExtra("departure",this.spinner_connections.selectedItem.toString())
            intent.putExtra("arrival",this.spinner_connections2.selectedItem.toString())

            val time = this.spinner_connections3.selectedItem.toString().split(":")
            val hour = time[0].toInt()
            val minutes = time[1].toInt()

            intent.putExtra("hour",hour)
            intent.putExtra("minutes",minutes)

            startActivity(intent)

        }

        spinner_connections.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Log.d("asdasdaasdasdsads",spinner_connections.selectedItem.toString())
                lstArrival.clear()
                lstHours.clear()
                for (i in lstBusConnection){
                    if(i.departure.toString() == spinner_connections.selectedItem.toString()){
                        Log.d("args",i.arrival.toString()+" "+i.arrival.toString())
                        //lstArrival.remove(i.arrival.toString())

                        if(!lstArrival.contains(i.arrival.toString())){
                            lstArrival.add(i.arrival.toString())
                        }
                        if(!lstHours.contains(i.departureDate!!.toLong())){
                            lstHours.add(i.departureDate!!.toLong())
                        }
                    }
                }
                refreshSpinnerArrival()
            }
        }
        spinner_connections2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Log.d("asdasdaasdasdsads",spinner_connections.selectedItem.toString())
                lstHours.clear()
                for (i in lstBusConnection){
                    if(i.departure.toString() == spinner_connections.selectedItem.toString() && i.arrival.toString() == spinner_connections2.selectedItem.toString()){
                        if(!lstHours.contains(i.departureDate!!.toLong())){
                            lstHours.add(i.departureDate!!.toLong())
                        }
                    }
                }
                refreshSpinnerHours()
            }
        }
    }

    fun refreshSpinnerArrival(){
        var cc = ArrayAdapter(this, android.R.layout.simple_spinner_item, lstArrival)
        cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_connections2.setAdapter(null)
        spinner_connections2!!.setAdapter(cc)

        refreshSpinnerHours()
    }

    fun refreshSpinnerHours(){

        var a:MutableList<String> = ArrayList()
        var calendar:Calendar = Calendar.getInstance()
        for(i in lstHours){
            calendar.timeInMillis=i
            calendar.get(Calendar.HOUR_OF_DAY)
            var minutes="0"
            if(calendar.get(Calendar.MINUTE)<10)
                minutes="0"+calendar.get(Calendar.MINUTE).toString()
            else
                minutes=calendar.get(Calendar.MINUTE).toString()

            a.add(calendar.get(Calendar.HOUR_OF_DAY).toString()+":"+minutes)

        }


        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, a)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.spinner_connections3!!.setAdapter(bb)
    }


    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        //textView_msg!!.text = "Selected : "+languages[position]
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}
