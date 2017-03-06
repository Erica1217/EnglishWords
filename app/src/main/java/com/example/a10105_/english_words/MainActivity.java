package com.example.a10105_.english_words;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public DBManager mDbManager = null;
    String tableName = null;
    ListView addedListview =null; //추가되어있는 영단어 리스트
    ListView resultListview = null; //전적 리스트
    AddedCursorAdapterEx addedCursorAdapter =null;
    CustomDialog mDialog =null;
    String[] columns = new String[] {"_id","english","hanguel"};
    EditText editText =null;
    ResultCursorAdapterEx resultCursorAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.search_word) ;
        resultListview = (ListView) findViewById(R.id.result_list);
        mDbManager = DBManager.getInstance(this);
        editText.addTextChangedListener(watcher);
        tableName = "Words"; //나중에 바꿀것 !!!=============================

        setAddedListview();
        setResultListview();
        addedListviewEvent();
        resultListviewEvent();
    }

    //전적 리스트뷰 ------------------------------------------------------
    public void setResultListview(){

        String[] resultColumns = new String[]{"_id", "date", "record", "correct"};
        Cursor c = mDbManager.query("Record_Result", resultColumns, null, null, null, null, null);

        resultCursorAdapter = new ResultCursorAdapterEx(this,c,0);
        resultListview.setAdapter(resultCursorAdapter);
    }
    //---------------------------------------------------------------------

    //상단바---------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    //---------------------------------------------------------------------

    //맨 밑의 '외우기(시험)'버튼 클릭--------------------------------------
    public void game(View view){
        Intent intent = new Intent(this, Game_menu.class);
        intent.putExtra("tableName", tableName);
        startActivity(intent);
        finish();
    }
    //---------------------------------------------------------------------

    //'+(단어 추가)'버튼 클릭----------------------------------------------
    public void add(View view){
        Intent intent = new Intent(this, Word_add.class);
        intent.putExtra("tableName", tableName);
        startActivity(intent);
        finish();
    }
    //---------------------------------------------------------------------

    //editText에 문자가 하나라도 입력되면 addedListView로 결과 출력---------
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String[] columns = new String[] {"_id","english","hanguel"};

            editText = (EditText)findViewById(R.id.search_word) ;
            RadioButton radioButton_english = (RadioButton)findViewById(R.id.english);
            RadioButton radioButton_hanguel = (RadioButton)findViewById(R.id.hanguel);

            if(radioButton_english.isChecked() == false && radioButton_hanguel.isChecked()==false) {
                Toast.makeText(MainActivity.this, "무엇으로 검색할지 선택해주세요", Toast.LENGTH_SHORT).show();
            }
            else if(editText.getText().equals("")){     //아무것도 입력되지 않으면 전체 출력
                Cursor data= mDbManager.query("Words",columns, null,null,null,null,null);
            }
            else if( radioButton_english.isChecked() == true){
                Cursor data = mDbManager.searchAutocomplete(tableName,"english",editText.getText().toString());
            }
            else if(radioButton_hanguel.isChecked() == true) {
                Cursor data = mDbManager.searchAutocomplete(tableName,"hanguel",editText.getText().toString());
            }
            setAddedListview();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void setAddedListview(){

        Cursor cursor= mDbManager.query(tableName,columns, null,null,null,null,null);
        addedCursorAdapter = new AddedCursorAdapterEx(this,cursor,0);

        addedListview = (ListView) findViewById(R.id.added_list);
        addedListview.setAdapter(addedCursorAdapter);


    }

    public void addedListviewEvent(){
        // 짧게 클릭했을때 (삭제)--------------------------------------------------------------
        addedListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("삭제")
                        .setMessage("삭제하시겠습니까?")
                        .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int deleteRecordCnt = mDbManager.delete(tableName,"_id='"+Long.toString(id)+"'",null); //단어삭제
                                Toast.makeText(MainActivity.this,"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                                setAddedListview();
                            }
                        })
                        .show();
            }
        });
        //-------------------------------------------------------------------------------

        //길게 클릭했을때 (수정) --------------------------------------------------------
        addedListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                mDialog = new CustomDialog(MainActivity.this);

                mDialog.setTitle("수정하시겠습니까?");
                mDialog.setOkClickListener( new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(mDialog.mEditText.equals("")==false) {
                            ContentValues updateRowValue = new ContentValues();
                            if(mDialog.english.isChecked()) {
                                updateRowValue.put("english", String.valueOf(mDialog.mEditText.getText()));
                                int updateRecordCnt = mDbManager.update(tableName,updateRowValue,"_id="+Long.toString(id),null);
                                Toast.makeText(MainActivity.this,Integer.toString(updateRecordCnt),Toast.LENGTH_SHORT).show();
                            }
                            else if(mDialog.hanguel.isChecked()){
                                updateRowValue.put("hanguel", String.valueOf(mDialog.mEditText.getText()));
                                int updateRecordCnt = mDbManager.update(tableName,updateRowValue,"_id="+Long.toString(id),null);
                            }
                            else{
                                Toast.makeText(MainActivity.this,"어떤 것으로 수정할지 선택해주세요",Toast.LENGTH_SHORT).show();
                            }
                            setAddedListview();
                        }
                        mDialog.dismiss();

                    }
                });
                mDialog.setCancleClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mDialog.dismiss();
                    }
                });
                mDialog.show();
                return true;
            }
        });
        //-------------------------------------------------------------------------------
    }

    public void resultListviewEvent(){
        // 짧게 클릭했을때 (삭제)--------------------------------------------------------------
        resultListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("삭제")
                        .setMessage("삭제하시겠습니까?")
                        .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int deleteRecordCnt = mDbManager.delete("Record_Result","_id='"+Long.toString(id)+"'",null); //단어삭제
                                Toast.makeText(MainActivity.this,"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                                setResultListview();
                            }
                        })
                        .show();
            }
        });
        //-------------------------------------------------------------------------------

    }

}
