package com.bus.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bus.Model.BusConnection;
import com.bus.Model.Ticket;
import com.bus.Model.User;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import kotlin.Experimental;
import kotlin.time.ExperimentalTime;

public class DBHelper extends SQLiteOpenHelper {

    private final static int DB_VERSION = 9;
    private final static String DB_NAME = "BusDB.db";
    private Context context;

    public DBHelper(Context context){

        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table BusConnections (" +
                        "idConnection integer primary key," +
                        "departure text," +
                        "arrival text," +
                        "departureDate long,"+
                        "active integer);"+
                        "");
        db.execSQL(
                "create table Tickets (" +
                        "id integer primary key," +
                        "scanned integer," +
                        "busConnection_id integer references BusConnections(idConnection)," +
                        "qrCode text);"+
                        "");

        db.execSQL(
                "create table Users(" +
                        "id integer primary key," +
                        "login text," +
                        "password text);"+
                        "");
   /*     db.execSQL(
                "create table BusConnections_Tickets (" +
                        "busConnectionId integer references BusConnections(idConnection)," +
                        "ticketId integer references Tickets(id)," +
                        "primary key (ticketId));"+
                        "");*/
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Tickets");
        db.execSQL("DROP TABLE IF EXISTS " + "BusConnections");
        db.execSQL("DROP TABLE IF EXISTS " + "BusConnections_Tickets");
        db.execSQL("DROP TABLE IF EXISTS " + "Users");
        onCreate(db);
    }
    public void clearDb(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + "Tickets");
        db.execSQL("DROP TABLE IF EXISTS " + "BusConnections");
        db.execSQL("DROP TABLE IF EXISTS " + "BusConnections_Tickets");
        db.execSQL("DROP TABLE IF EXISTS " + "Users");
        onCreate(db);
    }

    public void addUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id",user.getId());
        values.put("login",user.getLogin());
        values.put("password",user.getPassword());

        db.insertOrThrow("Users",null,values);
    }
    public boolean userExist(String login, String password){
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format("Select * from Users where login='%s' and password='%s'",login,password);
        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.getCount()>0){
            return true;
        }else
            return false;
    }

    public void addTicket(Ticket ticket){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id",ticket.getIdTicket());
        values.put("scanned",ticket.getScanned());
        values.put("busConnection_id",ticket.getBusConnection().getIdConnection());
        values.put("qrCode",ticket.getQrCode());

        db.insertOrThrow("Tickets",null,values);
/*
        Cursor cursor = getCursorTickets();
        cursor.moveToLast();
        int id = cursor.getInt(0);*/
//        addBusConnection_Ticket(ticket.getBusConnection().getIdConnection(),ticket.getIdTicket());
    }
    public void addBusConnection(BusConnection busConnection){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("idConnection",busConnection.getIdConnection());
        values.put("departure",busConnection.getDeparture().toString());
        values.put("arrival",busConnection.getArrival().toString());
        values.put("departureDate",busConnection.getDepartureDate().toString());
        values.put("active",busConnection.getActive());
        db.insertOrThrow("BusConnections",null,values);
    }
    public void addBusConnection_Ticket(int busConnection_id, int ticket_id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("busConnectionId",busConnection_id);
        values.put("ticketId",ticket_id);

        db.insertOrThrow("BusConnections_Tickets",null,values);
    }


    public Cursor getCursorTickets(){
        String[] columns = {"id","scanned","busConnection_id","qrCode"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Tickets",columns,null,null,null,null,null);

        return cursor;
    }
    public Cursor getCursorBusConnections(){
        String[] columns = {"idConnection","departure","arrival","departureDate", "active"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("BusConnections",columns,null,null,null,null,null);

        return cursor;
    }
    public Cursor getCursorBusConnectionsTickets(){
        String[] columns = {"busConnectionId","ticketId"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("BusConnections_Tickets",columns,null,null,null,null,null);

        return cursor;

    }

    public List<BusConnection> getListOfAllBusConnections(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format("Select * from BusConnections");
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor != null) {
            List<BusConnection>listBusConnections = new ArrayList<>();

            while(cursor.moveToNext()){
                int idConnection = cursor.getInt(0);
                String departure = cursor.getString(1);
                String arrival = cursor.getString(2);
                Long departureDate = cursor.getLong(3);
                int active = cursor.getInt(4);

                boolean bActive = active == 1;
                BusConnection busConnection = new BusConnection(context,idConnection, BusConnection.Places.valueOf(departure), BusConnection.Places.valueOf(arrival),departureDate,bActive);
                listBusConnections.add(busConnection);
            }
            cursor.close();

            return listBusConnections;
        }
        return null;
    }
    public List<Ticket> getListOfAllTickets(){
        SQLiteDatabase db = getReadableDatabase();
        String string = String.format("Select * from Tickets");
        Cursor cursor = db.rawQuery(string,null);

        if(cursor!=null){
            List<Ticket> ticketsList = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int scanned = cursor.getInt(1);
                int busConnection_id = cursor.getInt(2);
                String qrCode = cursor.getString(3);

                boolean bScanned = scanned == 1;
                Ticket ticket = new Ticket(context,id,bScanned,busConnection_id,qrCode);

                ticketsList.add(ticket);
            }
            cursor.close();

            return ticketsList;
        }

        return null;

    }
    public BusConnection getBusConnection(int id){
        SQLiteDatabase db = getReadableDatabase();
        String string = String.format("Select * from BusConnections where idConnection=%d",id);
        Cursor cursor = db.rawQuery(string,null);
        cursor.moveToFirst();

        int idConnection = cursor.getInt(0);
        String departure = cursor.getString(1);
        String arrival = cursor.getString(2);
        Long departureDate = cursor.getLong(3);
        int active = cursor.getInt(4);

        boolean bActive = active == 1;

        BusConnection busConnection = new BusConnection(context,idConnection, BusConnection.Places.valueOf(departure), BusConnection.Places.valueOf(arrival), departureDate,bActive);

        return busConnection;
    }
    public BusConnection getBusConnection(String departure, String arrival, Calendar departureDate, Boolean active) {

        SQLiteDatabase db = getReadableDatabase();
        String string = String.format("Select * from BusConnections Where departure='%s' and arrival='%s' and departureDate=%d and active=1", departure, arrival, departureDate.getTimeInMillis());
        Cursor cursor = db.rawQuery(string, null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            int idConnection = cursor.getInt(0);

            BusConnection busConnection = new BusConnection(context, idConnection, BusConnection.Places.valueOf(departure), BusConnection.Places.valueOf(arrival), departureDate.getTimeInMillis(),active);

            return busConnection;
        }
        return null;

    }
    public List<BusConnection> getListBusConnection(String departure, String arrival, Calendar date){
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format("Select * from BusConnections Where departure='%s' and arrival='%s' and departureDate=%d",departure,arrival,date.getTimeInMillis());
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor != null) {
            List<BusConnection>listBusConnections = new ArrayList<>();

            while(cursor.moveToNext()){
                int idConnection = cursor.getInt(0);
                Long departureDate = cursor.getLong(3);
                int active = cursor.getInt(4);

                boolean bActive = active == 1;
                BusConnection busConnection = new BusConnection(context,idConnection, BusConnection.Places.valueOf(departure), BusConnection.Places.valueOf(arrival),departureDate,bActive);
                listBusConnections.add(busConnection);
            }
            cursor.close();

            return listBusConnections;
        }
        return null;
    }
    public List<BusConnection> getListBusConnectionAll(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = String.format("Select *  from BusConnections WHERE active=1");
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.getCount()>0) {
            List<BusConnection>listBusConnections = new ArrayList<>();

            while(cursor.moveToNext()){
                int idConnection = cursor.getInt(0);
                String departure = cursor.getString(1);
                String arrival = cursor.getString(2);
                Long departureDate = cursor.getLong(3);
                int active = cursor.getInt(4);

                boolean bActive = active == 1;
                BusConnection busConnection = new BusConnection(context,idConnection, BusConnection.Places.valueOf(departure), BusConnection.Places.valueOf(arrival),departureDate,bActive);
                listBusConnections.add(busConnection);
            }
            cursor.close();

            return listBusConnections;
        }
        cursor.close();
        return null;
    }



    public List<Ticket> getListTicket(BusConnection busConnection){
        SQLiteDatabase db = getReadableDatabase();
        String string = String.format("Select * from Tickets where busConnection_id=%d",busConnection.getIdConnection());
        Cursor cursor = db.rawQuery(string,null);

        List<Ticket> ticketsList = new ArrayList<>();
        if(cursor.getCount()>0){
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int scanned = cursor.getInt(1);
                int busConnection_id = cursor.getInt(2);
                String qrCode = cursor.getString(3);

                boolean bScanned = scanned == 1 ? true : false;
                Ticket ticket = new Ticket(context,id,bScanned,busConnection_id,qrCode);

                ticketsList.add(ticket);
            }
        }
        cursor.close();

        return ticketsList;
    }
    public List<Ticket> getListOfScannedTicket(BusConnection busConnection){
        SQLiteDatabase db = getReadableDatabase();
        String string = String.format("Select * from Tickets where busConnection_id=%d and scanned = 1",busConnection.getIdConnection());
        Cursor cursor = db.rawQuery(string,null);

        List<Ticket> ticketsList = new ArrayList<>();
        if(cursor.getCount()>0){
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int busConnection_id = cursor.getInt(2);
                String qrCode = cursor.getString(3);

                Ticket ticket = new Ticket(context,id,true,busConnection_id,qrCode);

                ticketsList.add(ticket);
            }
        }
        cursor.close();

        return ticketsList;
    }

    public boolean setActive(BusConnection busConnection, boolean active){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("active",active);
        String args[] = {busConnection.getIdConnection()+""};

        db.update("BusConnections",values,"idConnection=?",args);

        db.close();

        return true;
    }
//    public List<Ticket> getListOfScannedTicket(BusConnection busConnection){
//        SQLiteDatabase db = getReadableDatabase();
//        String string = String.format("Select * from Tickets where busConnection_id=%d and scanned = 1",busConnection.getIdConnection());
//        Cursor cursor = db.rawQuery(string,null);
//
//        List<Ticket> ticketsList = new ArrayList<>();
//        if(cursor.getCount()>0){
//            while (cursor.moveToNext()) {
//                int id = cursor.getInt(0);
//                int busConnection_id = cursor.getInt(2);
//                String qrCode = cursor.getString(3);
//
//                Ticket ticket = new Ticket(context,id,true,busConnection_id,qrCode);
//
//                ticketsList.add(ticket);
//            }
//        }
//        cursor.close();
//
//        return ticketsList;
//    }

}
