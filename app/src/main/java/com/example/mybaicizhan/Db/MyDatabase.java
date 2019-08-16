package com.example.mybaicizhan.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mybaicizhan.Http.WordValue;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyDatabase {

    private static final String TAG = "DB";
    private static final String TABLE_WORDVALUE = "wordvalue";
    private static final String DBNAME = "WordValue";

    private DbOpenHandler dbOpenHandler;
    private SQLiteDatabase db;

    private Context mContext;

    public MyDatabase(Context context){
        mContext = context;
        dbOpenHandler = new DbOpenHandler(mContext,DBNAME,null,1);
    }

    public void insertWordValue(WordValue wordValue){
        if(wordValue == null){
            Log.d(TAG,"插入失败");
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put("word",wordValue.getWord());
        cv.put("psE",wordValue.getPsE());
        cv.put("pronE",wordValue.getPronE());
        cv.put("psA",wordValue.getPsA());
        cv.put("pronA",wordValue.getPronA());
        cv.put("pos",wordValue.getPos());
        cv.put("orig",wordValue.getOrig());
        cv.put("trans",wordValue.getTrans());
        db = dbOpenHandler.getWritableDatabase();
        db.insert(TABLE_WORDVALUE,null,cv);
        db.close();
        Log.d(TAG,"插入成功");
        wordValue.printInfo();
    }

    public WordValue queryWordValueByWord(String word){
        if(word == null){
            Log.d(TAG,"查询失败");
            return null;
        }
        WordValue wordValue = null;
        db = dbOpenHandler.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORDVALUE,null,"word=?",new String[]{word},null,null,null);
        if(cursor.moveToNext()){
            wordValue = new WordValue();
            wordValue.setWord(word);
            wordValue.setPsE(cursor.getString(cursor.getColumnIndex("psE")));
            wordValue.setPsA(cursor.getString(cursor.getColumnIndex("psA")));
            wordValue.setPronE(cursor.getString(cursor.getColumnIndex("pronE")));
            wordValue.setPronA(cursor.getString(cursor.getColumnIndex("pronA")));
            wordValue.setPos(cursor.getString(cursor.getColumnIndex("pos")));
            wordValue.setOrig(cursor.getString(cursor.getColumnIndex("orig")));
            wordValue.setTrans(cursor.getString(cursor.getColumnIndex("trans")));
        }
        cursor.close();
        db.close();
        if(wordValue != null)
            Log.d(TAG,"查询成功");
        return wordValue;
    }

    public WordValue[] queryWordValuesLimit3(){
        WordValue[] wordValues = new WordValue[]{null,null,null};
        int index = 0;
        db = dbOpenHandler.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORDVALUE,null,null,null,null,null,null);
        cursor.moveToLast();
        cursor.moveToNext();
        while( cursor.moveToPrevious() && index < 3){
            WordValue wordValue = new WordValue();
            wordValue.setWord(cursor.getString(cursor.getColumnIndex("word")));
            wordValue.setPsE(cursor.getString(cursor.getColumnIndex("psE")));
            wordValue.setPsA(cursor.getString(cursor.getColumnIndex("psA")));
            wordValue.setPronE(cursor.getString(cursor.getColumnIndex("pronE")));
            wordValue.setPronA(cursor.getString(cursor.getColumnIndex("pronA")));
            wordValue.setPos(cursor.getString(cursor.getColumnIndex("pos")));
            wordValue.setOrig(cursor.getString(cursor.getColumnIndex("orig")));
            wordValue.setTrans(cursor.getString(cursor.getColumnIndex("trans")));
            wordValues[index++] = wordValue;
        }
        cursor.close();
        db.close();
        if(wordValues != null)
            Log.d(TAG,"查询历史3成功");
        return wordValues;
    }

    public ArrayList<WordValue> queryWordValues(){
        ArrayList<WordValue> wordValues = new ArrayList<>();
        int index = 0;
        db = dbOpenHandler.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORDVALUE,null,null,null,null,null,null);
        cursor.moveToLast();
        cursor.moveToNext();
        while(cursor.moveToPrevious()){
            WordValue wordValue = new WordValue();
            wordValue.setWord(cursor.getString(cursor.getColumnIndex("word")));
            wordValue.setPsE(cursor.getString(cursor.getColumnIndex("psE")));
            wordValue.setPsA(cursor.getString(cursor.getColumnIndex("psA")));
            wordValue.setPronE(cursor.getString(cursor.getColumnIndex("pronE")));
            wordValue.setPronA(cursor.getString(cursor.getColumnIndex("pronA")));
            wordValue.setPos(cursor.getString(cursor.getColumnIndex("pos")));
            wordValue.setOrig(cursor.getString(cursor.getColumnIndex("orig")));
            wordValue.setTrans(cursor.getString(cursor.getColumnIndex("trans")));
            wordValues.add(wordValue);
        }
        cursor.close();
        db.close();
        if(wordValues != null)
            Log.d(TAG,"查询历史成功");
        return wordValues;
    }

    public void deleteAll(){
        db = dbOpenHandler.getWritableDatabase();
        db.delete(TABLE_WORDVALUE,null,null);
        db.close();
    }

}
