package com.example.ym.myaidlcustomerdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ym on 19-3-5.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private final String TAG="MySQLiteOpenHelper";

    private static final String CREATE_BOOK="create table Book ("
            +"id integer primary key autoincrement,"
            +"autoher text,"
            +"price real,"
            +"name text,"
            +"pages integer)";

    private static final String CREATE_CATEGORY="create table Category ("
            +"id integer primary key autoincrement,"
            +"category_name text,"
            +"category_code integer)";
    private Context mContext;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Log.i(TAG,"Create succeeded");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}
