package com.example.a10105_.english_words;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by 10105-김유진 on 2016-06-14.
 */

public class Word_add extends AppCompatActivity {

    String table= null;
    public DBManager mDbManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_add);

        mDbManager = DBManager.getInstance(this);

        Intent intent = getIntent();
        table = intent.getStringExtra("tableName");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tableName",table);
        startActivity(intent);
        finish();
    }

    public void add_button(View view){
        EditText eng_word = (EditText)findViewById(R.id.eng_word);;
        EditText han_word =(EditText)findViewById(R.id.han_word);

        String[] columns = new String[] {"english","hanguel"};
        Cursor data= mDbManager.query(table,columns, "english='"+eng_word.getText()+"'",null,null,null,null);
        if (data.getCount()!=0){//data.getString(0).equals(eng_word.getText())) {
            Toast.makeText(this,"이미 있는 단어입니다.",Toast.LENGTH_SHORT).show();
        } else {
            ContentValues addRowValue = new ContentValues();

            addRowValue.put("english",eng_word.getText().toString());
            addRowValue.put("hanguel",han_word.getText().toString());

            long insertRecordID = mDbManager.insert(table,addRowValue);

            Toast.makeText(this,eng_word.getText()+"["+han_word.getText()+"]"+"추가되었습니다.",Toast.LENGTH_SHORT).show();

            eng_word.setText("");
            han_word.setText("");
        }




    }
}