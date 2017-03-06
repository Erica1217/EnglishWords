package com.example.a10105_.english_words;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by 10105-김유진 on 2016-06-14.
 */
public class Word_delete extends AppCompatActivity {

    public DBManager mDbManager = null;
    String table= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_delete);

        mDbManager = DBManager.getInstance(this);

        Intent intent = new Intent(this.getIntent());
        table = intent.getStringExtra("tableName");
        intent.putExtra("tableName",table);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("tableName",table);
        startActivity(intent);
        finish();
    }

    public void delete_button(View view){

        EditText deleteWord = (EditText)findViewById(R.id.delete_word);

        RadioButton eng = (RadioButton)findViewById(R.id.deleteByeng);
        RadioButton han = (RadioButton)findViewById(R.id.deleteByhan);

        if(deleteWord.getText().equals("")== true) //문자열이 아무것도 없을때 (입력 x)
        {
            Toast.makeText(this,"삭제할 단어를 입력해주세요",Toast.LENGTH_SHORT).show();
        }
        else if(eng.isChecked()==false &&han.isChecked()==false){

            Toast.makeText(this,"무엇으로 삭제할지 선택해주세요",Toast.LENGTH_SHORT).show();
        }
        else
        {
            int deleteRecordCnt=0;
            if(eng.isChecked()==true) {

                deleteRecordCnt = mDbManager.delete(table,"english='"+deleteWord.getText().toString()+"'",null);
            }
            else if(han.isChecked()==true){
                deleteRecordCnt = mDbManager.delete(table,"hanguel='"+deleteWord.getText().toString()+"'",null);

            }

            if(deleteRecordCnt==0)
            {
                Toast.makeText(this,"삭제할 단어가 없습니다. 철자를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,deleteWord.getText()+" 삭제되었습니다.",Toast.LENGTH_SHORT).show();
            }

            deleteWord.setText("");
        }
    }
}