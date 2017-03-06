package com.example.a10105_.english_words;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 10105-김유진 on 2016-06-16.
 */
public class DBManager {

    //DB명, 테이블명, DB버전 정보 정의
    //==============================================================================
    static final String DB_WORDS ="Words.db";
    static final String TABLE = "Words";
    static final int DB_VERSION = 1;
    //==============================================================================
    static final String DB_AGAIN_WORDS ="Again_Words.db";
    static final String AGAIN_TABLE = "Again_Words";
   //==============================================================================
    static final String DB_RECORD_RESULT = " Record_Result.db";
    static final String RESULT_TABLE = "Record_Result";
    //==============================================================================

    Context mContext = null;

        private static DBManager mDbManager = null;
        private SQLiteDatabase mDatabase = null;

        //==============================================================================

        public static DBManager getInstance( Context context)
        {
            if(mDbManager == null)
            {
                mDbManager = new DBManager(context);
            }
            return mDbManager;
        }

        //==============================================================================

        private DBManager(Context context) {
            mContext = context;

            mDatabase = context.openOrCreateDatabase(DB_WORDS, Context.MODE_PRIVATE, null);
            mDatabase = context.openOrCreateDatabase(DB_AGAIN_WORDS, Context.MODE_PRIVATE, null);
            //==============================================================================
            mDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "english    TEXT," +
            "hanguel    TEXT," +
            "correct_num    INTEGER); ");

            mDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + AGAIN_TABLE + "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "english    TEXT," +
                    "hanguel    TEXT" + "); ");

            mDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + RESULT_TABLE + "("+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                    "correct    INTEGER,"+ //맞은 개수
                    "record     INTEGER,"+ //총 전적
                    "date    TEXT"+"); "); //시간간
       }

    public long insert(String table, ContentValues addRowValue)
    {
        if(table.equals("Words")){
            addRowValue.put("correct_num",0);
        }
        return mDatabase.insert(table,null,addRowValue);
    }

    public Cursor query( String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public int delete(String table, String whereClause, String[] whereArgs){
        return mDatabase.delete(table,whereClause,whereArgs);
    }

    public void removeTable(String tableName) {
        String sql = "drop tableName "+tableName;
        mDatabase.execSQL(sql);
    }

    public int update(String table, ContentValues updateRowValue, String whereClause, String[] whereArgs){
        return mDatabase.update(table, updateRowValue,whereClause,whereArgs);
    }

    public Cursor searchAutocomplete(String table, String updateRowValue,String whereClause){
        whereClause = "'%"+whereClause+"%'";
        return mDatabase.rawQuery("SELECT * FROM " + table + " WHERE "+ updateRowValue + " LIKE "+whereClause+";", null);
    }

}
