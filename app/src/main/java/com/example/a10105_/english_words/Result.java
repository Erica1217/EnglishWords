package com.example.a10105_.english_words;

/**
 * Created by KIMYUJIN on 2016-07-06.
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class Result extends AppCompatActivity {

    public DBManager mDbManager = null;
    String table = null;
    ListView mListView = null;
    ResultCursorAdapterEx mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        Intent intent = new Intent(this.getIntent());
        table = intent.getStringExtra("tableName");

        mDbManager = DBManager.getInstance(this);

        String[] columns = new String[]{"_id", "date", "record", "correct"};
        Cursor data = mDbManager.query("Record_Result", columns, null, null, null, null, null);

        mAdapter = new ResultCursorAdapterEx(this,data,0); // listView
        mListView = (ListView) findViewById(R.id.list_result);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}