package com.example.a10105_.english_words;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by 10105-김유진 on 2016-06-14.
 */
public class Word_search extends AppCompatActivity {
    public DBManager mDbManager = null;
    String table = null;
    ListView mListView =null;
    AddedCursorAdapterEx mAdapter =null;
    CustomDialog mDialog =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_search);

        Intent intent = new Intent(this.getIntent());
        table = intent.getStringExtra("tableName");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("tableName",table);
        startActivity(intent);
        finish();
    }


    public void search_button(View view){
        DBManager.getInstance(this);
        EditText editText = (EditText)findViewById(R.id.search_word);

        String[] columns = new String[] {"_id","english","hanguel"};

        RadioButton radioButton_english = (RadioButton)findViewById(R.id.english);
        RadioButton radioButton_hanguel = (RadioButton)findViewById(R.id.hanguel);

        if(radioButton_english.isChecked() == false && radioButton_hanguel.isChecked()==false) {
            Toast.makeText(this, "무엇으로 검색할지 선택해주세요", Toast.LENGTH_SHORT).show();
        }
        else if( editText.getText().equals("")==true ){
            Toast.makeText(this, "검색할 단어를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else if( radioButton_english.isChecked() == true){
            Cursor c = mDbManager.query(table,columns,"english="+"'"+editText.getText()+"'",null,null,null,null);

            mAdapter = new AddedCursorAdapterEx(this, c,0);
            mListView = (ListView) findViewById(R.id.list);
            mListView.setAdapter(mAdapter);

            if(c.getCount()==0){
                Toast.makeText(this, "검색하려는 단어가 없습니다.", Toast.LENGTH_SHORT).show();
            }

        }
        else if(radioButton_hanguel.isChecked() == true) {
            Cursor c = mDbManager.query(table, columns, "hanguel=" + "'" + editText.getText() + "'", null, null, null, null);
            Toast.makeText(this,String.valueOf( c.getPosition())+editText.getText().toString(),Toast.LENGTH_SHORT).show();

            mAdapter = new AddedCursorAdapterEx(this, c,0);
            mListView = (ListView) findViewById(R.id.list);
            mListView.setAdapter(mAdapter);

            if(c.getCount()==0){
                Toast.makeText(this, "검색하려는 단어가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
