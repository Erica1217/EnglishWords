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
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by 10105-김유진 on 2016-06-14.
 *
 * 주관식
 */
public class Game_english extends AppCompatActivity {

    public DBManager mDbManager = null;
    String table=null;
    int recordNum=0;
    int indexRand=0;
    int correct_index=0;
    int correct_record=0;
    boolean abc = false;
    int btnCount=0;
    boolean btnClick=false;
    int correct_num=0;
    ListView mListView =null;
    AddedCursorAdapterEx mAdapter =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_english);

        Intent intent = new Intent(this.getIntent());
        table = intent.getStringExtra("tableName"); //table의 정보를 얻어온다

        mDbManager = DBManager.getInstance(this);

        TextView problem = (TextView)findViewById(R.id.problem);
        TextView file = (TextView)findViewById(R.id.table);

//        if(tableName.equals("Words")){ //Table의 정보에 따라 오른쪽 위에 파일정보를 나타낸다
//            file.setText("공부");
//        }
//        else{
//            file.setText("복습");
//        }

        String[] columns = new String[] {"english","hanguel"};
        Cursor c= mDbManager.query(table,columns, null,null,null,null,null);
        recordNum = c.getCount();

        if (recordNum < 3)
        {
            new AlertDialog.Builder(this)
                    .setTitle("단어 수 부족")
                    .setMessage("단어 수가 너무 적습니다.\n"+"단어가 적어도 3개이상 있어야 합니다.\n")
                    .setNeutralButton("닫기",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {
                            Intent intent = new Intent(getApplicationContext(), Game_menu.class);
                            intent.putExtra("tableName",table);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
        }
        else{
                int[] index_check = new int[3]; //이미 나온 번호인지 확인,
                int[] record_check = new int[recordNum] ; //이미 나온 단어인지 확인 ,
                String[] example_word = new String[3];
                int correct=0;
                int rand;

                Random random = new Random();
                correct_record = random.nextInt(recordNum); //3개의 보기중 어느 자리를 할지 번호로 뽑음

                c.moveToPosition(correct_record); //어느 것을 정답으로 할지 고름
                problem.setText(c.getString(0));  // 문제를 정답(영어)으로 setText

                correct_index = random.nextInt(3);  //correct = 번호, a= 정답(영어)의 보기번호
                example_word[correct_index]=c.getString(1); //

                index_check[correct_index] = 1; //
                record_check[correct_record] = 1; //보기에 정답이 또 나오면 안되므로 체크해둠

                int n = 0;
                for (; n != 2;)
                {
                    int place = random.nextInt(3);
                    int word = random.nextInt(recordNum);;
                    c.moveToPosition(word); //curosr를 랜덤레코드로 이동시킴. correct_record = 정답
                    if (index_check[place] == 0 && !example_word[correct_index].equals(c.getString(1)) &&  record_check[word]==0){
                        index_check[place] = 1;
                        example_word[place]=c.getString(1);
                        record_check[word] = 1;
                        n++;
                    }
                }
                int i;

                Button button1 = (Button)findViewById(R.id.example1);
                Button button2 = (Button)findViewById(R.id.example2);
                Button button3 = (Button)findViewById(R.id.example3);

                button1.setText(example_word[0]);
                button2.setText(example_word[1]);
                button3.setText(example_word[2]);

            btnClick = true;
        }
    }

    @Override
    public void onBackPressed(){
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

        String[] columns = new String[] {"_id","correct_num","english","hanguel"};
        Cursor cursor = mDbManager.query("Words",columns,"correct_num>'4'",null,null,null,null);

        int i = cursor.getCount();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            for(int t=0 ; t<cursor.getCount() ; t++)
            {
                ContentValues addRowValue = new ContentValues();
                addRowValue.put("english", cursor.getString(2));
                addRowValue.put("hanguel", cursor.getString(3));
                long insertRecordID = mDbManager.insert("Again_Words", addRowValue);
                cursor.moveToNext();
            }
            int deleteCnt = mDbManager.delete("Words", "correct_num>4", null);


            mAdapter = new AddedCursorAdapterEx(this,cursor,0); // listView
            mListView = (ListView) findViewById(R.id.list);
            new AlertDialog.Builder(Game_english.this)
                    .setTitle("다음 단어들이 복습파일로 이동하였습니다")
//                    .setCursor(cursor, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }, "english") //한글도 같이 나와야하는데 어쩌짐...

            .setAdapter(mAdapter, new DialogInterface.OnClickListener() {
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
        }
        else{
            Intent intent = new Intent(this, Game_menu.class);
            intent.putExtra("tableName",table);
            startActivity(intent);
            finish();
        }
    }

    public void onClick(View v){
        if(btnClick)
        {
            btnCount++;
            switch (v.getId()) {
                case R.id.example1:
                    if (correct_index == 0) {
                        correct();
                    } else {
                        worng();
                    }
                    break;
                case R.id.example2:
                    if (correct_index == 1) {
                        correct();
                    } else {
                        worng();
                    }
                    break;
                case R.id.example3:
                    if (correct_index == 2) {
                        correct();
                    } else {
                        worng();
                    }
                    break;
            }
            btnClick=false;
        }

    }

    public void next_btn(View v){
        TextView problem = (TextView)findViewById(R.id.problem);
        TextView file = (TextView)findViewById(R.id.table);
        TextView correct= (TextView)findViewById(R.id.correct);
        file.setText(table);
        correct.setText("");

        String[] columns = new String[] {"english","hanguel"};
        Cursor c= mDbManager.query(table,columns, null,null,null,null,null);
        recordNum = c.getCount();


        int[] index_check = {0,0,0}; //이미 나온 번호인지 확인, 0으로 초기화해야함
        int[] record_check = new int[recordNum] ; //이미 나온 단어인지 확인 , 0으로 초기화해야함
        String[] example_word = new String[3];
        int rand;

        Random random = new Random();
        correct_record = random.nextInt(recordNum);

        c.moveToPosition(correct_record);
        problem.setText(c.getString(0));  // 문제를 정답(영어)로 setText

        correct_index = random.nextInt(3);  //correct = 번호, a= 정답(영어)의 보기번호
        example_word[correct_index]=c.getString(1);

        index_check[correct_index] = 1;
        record_check[correct_record] = 1;
        int n = 0;
        for (; n != 2;)
        {
            int place = random.nextInt(3);
            int word = random.nextInt(recordNum);;
            c.moveToPosition(word); //curosr를 랜덤레코드로 이동시킴. correct_record = 정답
            if (index_check[place] == 0 && !example_word[correct_index].equals(c.getString(1)) &&  record_check[word] != 1){
                index_check[place] = 1;
                example_word[place]=c.getString(1);
                record_check[word] = 1;
                n++;
            }
        }
        int i;

        Button button1 = (Button)findViewById(R.id.example1);
        Button button2 = (Button)findViewById(R.id.example2);
        Button button3 = (Button)findViewById(R.id.example3);

        button1.setText(example_word[0]);
        button2.setText(example_word[1]);
        button3.setText(example_word[2]);

        btnClick = true;
    }

    public void correct(){
        correct_num++;
        TextView textView = (TextView)findViewById(R.id.correct);
        textView.setText("정답입니다");
        if(table.equals("Words")){   //correct +1; 테이블이 words인 경우만
            Cursor c= mDbManager.query(table,null, null,null,null,null,null);
            c.moveToPosition(correct_record);
            ContentValues updateRowValue = new ContentValues();
            updateRowValue.put("correct_num",c.getInt(3)+1);
            mDbManager.update(table,updateRowValue,"english='"+c.getString(1)+"'",null);
         }
    }

    public void worng(){
        TextView textView = (TextView)findViewById(R.id.correct);

        String[] columns = new String[] {"correct_num","english","hanguel"};
        Cursor c= mDbManager.query(table,columns, null,null,null,null,null);
        c.moveToPosition(correct_record);
        textView.setText("틀렸습니다\n"+ "정답은 " + c.getString(1) + "[ "+c.getString(2) +" ]");
    }

}