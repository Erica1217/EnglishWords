package com.example.a10105_.english_words;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 10105-김유진 on 2016-06-14.
 */
public class Game_menu extends AppCompatActivity {

    String table = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_menu);

        Intent intent = new Intent(this.getIntent());
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

    public void choice(View view)
    {
        Intent intent = new Intent(this, Game_english.class);
        intent.putExtra("tableName",table);
        startActivity(intent);
        finish();
    }

    public void question(View view)
    {
        Intent intent = new Intent(this, Game_hanguel.class);
        intent.putExtra("tableName",table);
        startActivity(intent);
        finish();
    }

    public void home_btn(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}