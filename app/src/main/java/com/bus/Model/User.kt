package com.bus.Model

import android.content.Context
import com.bus.Data.DBHelper

class User {
    companion object{
        var counter_id:Int = 1
    }

    var id: Int? = null
    var login: String? = null
    var password: String? = null
    var context: Context? = null

    constructor(context: Context, login:String, password:String, addToData:Boolean){
        this.id = counter_id++
        this.login = login
        this.password = password
        this.context = context

        if(addToData){

            var db: DBHelper = DBHelper(context)
            db.addUser(this)

        }

    }
}