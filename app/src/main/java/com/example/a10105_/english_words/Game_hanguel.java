package com.example.a10105_.english_words;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by 10105-김유진 on 2016-06-14.
 */
public class Game_hanguel extends AppCompatActivity {

    String table= null;
    public DBManager mDbManager = null;
    int recordNum=0;
    int rand=0;
    int btnCount=0; // 전적
    int correct_num=0; //맞은 개수
    boolean btnClick=false;
    ListView mListView =null;
    AddedCursorAdapterEx mAdapter =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_hanguel);

        TextView problem = (TextView)findViewById(R.id.problem);

        Intent intent = new Intent(this.getIntent());
        table = intent.getStringExtra("tableName");
        //----------------------------------------------------------------- DBManager 생성 ,intent 값 받아옴
        mDbManager = DBManager.getInstance(this);

        String[] columns = new String[] {"english","hanguel"};
        Cursor c= mDbManager.query(table,columns, null,null,null,null,null);
        recordNum = c.getCount();

        //----------------------------------------------------------------- 맨 위에 공부인지 복습인지 알려줌
        TextView textView = (TextView)findViewById(R.id.table);
        if(table.equals("Words")){
            textView.setText("공부");
        }
        else{
            textView.setText("복습");
        }
        //-----------------------------------------------------------------

        if(recordNum==0){
            new AlertDialog.Builder(this)
                .setTitle("단어 수 부족")
                .setMessage("단어 수가 너무 적습니다.\n"+"단어가 적어도 1개이상 있어야 합니다.\n")
                .setNeutralButton("닫기",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
                        finish();
                    }
                }).show();
        }
        else{
            Random random = new Random();
            rand = random.nextInt(recordNum);
            c.moveToPosition(rand);
            problem.setText(c.getString(1));
        }

        btnClick=true;

    }

    @Override
    public void onBackPressed() { String[] columns = new String[] {"_id","correct_num","english","hanguel"};
        if(btnCount >0) {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String strCurDate = CurDateFormat.format(date);
            ContentValues contentValues = new ContentValues();

            contentValues.put("date",strCurDate);
            contentValues.put("correct",correct_num);
            contentValues.put("record",btnCount); //총 전적
            mDbManager.insert("Record_Result",contentValues);
        }


        Cursor cursor = mDbManager.query("Words",columns,"correct_num>'4'",null,null,null,null);
        int i = cursor.getCount();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            ContentValues addRowValue = new ContentValues();
            addRowValue.put("english", cursor.getString(2));
            addRowValue.put("hanguel", cursor.getString(3));
            long insertRecordID = mDbManager.insert("Again_Words", addRowValue);

            mAdapter = new AddedCursorAdapterEx(this,cursor,0); // listView
            mListView = (ListView) findViewById(R.id.list);
            new AlertDialog.Builder(Game_hanguel.this)
                    .setTitle("다음 단어들이 복습파일로 이동하였습니다").
                    setAdapter(mAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNeutralButton("닫기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {
                            Intent intent = new Intent(getApplicationContext(), Game_menu.class);
                            intent.putExtra("tableName",table);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
            int deleteCnt = mDbManager.delete("Words", "correct_num>4", null);
        }
        else{

            Intent intent = new Intent(this, Game_menu.class);
            intent.putExtra("tableName",table);
            startActivity(intent);
            finish();
        }
    }

    public void input(View view){

        if(btnClick) {
            btnCount++;
            EditText dap = (EditText) findViewById(R.id.dap);
            Button dapBtn = (Button) findViewById(R.id.dap_btn);
            TextView correct = (TextView) findViewById(R.id.correct);

            Cursor c = mDbManager.query(table, null, null, null, null, null, null);
            c.moveToPosition(rand);
            if (dap.getText().toString().equals(c.getString(1).toString())) {
                correct.setText("정답입니다.");
                if (table.equals("Words")) {   //correct +1; 테이블이 words인 경우만
                    ContentValues updateRowValue = new ContentValues();
                    updateRowValue.put("correct_num", c.getInt(3) + 1);
                    mDbManager.update(table, updateRowValue, "english='" + c.getString(1) + "'", null);
                    correct_num++;
                }
            } else {
                correct.setText("틀렸습니다\n" + "정답은 " + c.getString(1) + " 입니다.");
            }
            btnClick = false;
        }
    }

    public void next_btn(View view){

        TextView problem = (TextView)findViewById(R.id.problem);
        TextView correct = (TextView)findViewById(R.id.correct);
        EditText dap = (EditText)findViewById(R.id.dap);

        correct.setText("");
        dap.setText("");
        //----------------------------------------------------------------- DBManager 생성 ,intent 값 받아옴
//        String[] columns = new String[] {"english","hanguel"};
        Cursor c= mDbManager.query(table,null, null,null,null,null,null);
        recordNum = c.getCount();

        Intent intent = new Intent(this.getIntent());
        table = intent.getStringExtra("tableName");

        //-----------------------------------------------------------------

        Random random = new Random();
        rand = random.nextInt(recordNum);

        c.moveToPosition(rand);
        problem.setText(c.getString(2));

        btnClick=true;

    }
}
