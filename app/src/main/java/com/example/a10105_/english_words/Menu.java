package com.example.a10105_.english_words;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 10105-김유진 on 2016-06-12.
 */
public class Menu extends AppCompatActivity {

    String table=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Intent intent = new Intent(this.getIntent());
        table = intent.getStringExtra("tableName");

        DBManager mDbManager = DBManager.getInstance(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void memorize(View view){
        Intent intent = new Intent(this, Game_menu.class);
        intent.putExtra("tableName",table);
        startActivity(intent);
        finish();

    }

    public void add(View view){
        Intent intent = new Intent(this, Word_add.class);
        intent.putExtra("tableName",table);
        startActivityForResult(intent,0);
        finish();

    }

    public void delete(View view){
        Intent intent = new Intent(this, Word_delete.class);
        intent.putExtra("tableName",table);
        startActivity(intent);
        finish();

    }

    public void search(View view){
        Intent intent = new Intent(this, Word_search.class);
        intent.putExtra("tableName",table);
        startActivity(intent);
        finish();

    }

    public void home_btn(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
