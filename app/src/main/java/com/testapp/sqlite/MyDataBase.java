package com.testapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.testapp.model.Pojo;

import java.util.ArrayList;

/**
 * Created by cws on 20/10/16.
 */
public class MyDataBase extends SQLiteOpenHelper {

    public static String Db_name = "TestingApp";
    public static String Table_name = "";

    public String createTable = "create table myData " +
            "(id integer primary key, name text,address text,location text, image text)";


    public MyDataBase(Context context) {
        super(context, Db_name, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS myData");
        onCreate(db);
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "myData");
        return numRows;
    }

    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + "myData");
        db.close();
    }


    public boolean addData(String name, String address, String location, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("location", location);
        contentValues.put("image", image);
        db.insert("myData", null, contentValues);
        return true;
    }

    public ArrayList<Pojo> getallData() {
        ArrayList<Pojo> array_list = new ArrayList<Pojo>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from myData", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Pojo dataObject = new Pojo();
            dataObject.setName(res.getString(res.getColumnIndex("name")));
            dataObject.setLocation(res.getString(res.getColumnIndex("address")));
            dataObject.setLocation(res.getString(res.getColumnIndex("location")));
            dataObject.setImage(res.getString(res.getColumnIndex("image")));
            array_list.add(dataObject);
            res.moveToNext();
        }

        return array_list;

    }

}
