package com.example.goptimus.yombouna;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class LocalStorage {
    SQLiteDatabase db;

    public LocalStorage(Context context) {
        DBcontroler mDbHelper = new DBcontroler(context);
        db = mDbHelper.getWritableDatabase();
    }

    public  long insert(String imei){
        ContentValues values = new ContentValues();
        values.put(DBmodel.DBCOLUMN.COLUMN_NAME_IMEI, imei);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DBmodel.DBCOLUMN.TABLE_NAME, null, values);

        return  newRowId;
    }

    public  String select (){

        String[] projection = {
                BaseColumns._ID,
                DBmodel.DBCOLUMN.COLUMN_NAME_IMEI};

        String selection = "";

        String[] selectionArgs = {};

        String sortOrder =
                DBmodel.DBCOLUMN._ID + " DESC";

        Cursor cursor = db.query(
                DBmodel.DBCOLUMN.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DBmodel.DBCOLUMN._ID));
            itemIds.add(itemId);
        }
        cursor.close();

        return  itemIds.get(0).toString();
    }

    public  int delete(int id){
        // Define 'where' part of query.
        String selection = DBmodel.DBCOLUMN._ID + " = ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = {""+id};
// Issue SQL statement.
        int deletedRows = db.delete(DBmodel.DBCOLUMN.TABLE_NAME, selection, selectionArgs);
        return  deletedRows;
    }

    public  int  update(int id,String imei){
        // New value for one column

        ContentValues values = new ContentValues();
        values.put(DBmodel.DBCOLUMN.COLUMN_NAME_IMEI, imei);

// Which row to update, based on the title
        String selection = DBmodel.DBCOLUMN._ID + " = ?";
        String[] selectionArgs = { ""+id };

        int count = db.update(
                DBmodel.DBCOLUMN.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }

}
