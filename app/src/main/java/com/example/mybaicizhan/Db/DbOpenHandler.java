package com.example.mybaicizhan.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DbOpenHandler extends SQLiteOpenHelper {

    private static final String CREATE_WORDS = "CREATE TABLE " +
            "wordvalue(_id integer primary key autoincrement," +
            " word varchar(64) unique, psE varchar(20), pronE text, psA varchar(20), pronA text," +
            " pos text, orig text, trans text , modified_time timestamp)";

    public DbOpenHandler(Context context, String name,
                         SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version); //第一次创建数据库时调用
    }
    @Override
    public void onCreate(SQLiteDatabase db) { //第一次创建数据库时调用
        db.execSQL(CREATE_WORDS);
    }
    @Override // 数据库时调用
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
    }
}
