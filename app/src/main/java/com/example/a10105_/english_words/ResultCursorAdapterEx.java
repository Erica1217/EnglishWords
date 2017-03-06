package com.example.a10105_.english_words;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.IntegerRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by 10105-김유진 on 2016-06-24.
 */
public class ResultCursorAdapterEx extends CursorAdapter{

    Context     mContext = null;
    LayoutInflater  mLayoutInflater = null;

    public ResultCursorAdapterEx(Context context, Cursor c, int flags) {
        super(context, c,flags);

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    class ViewHolder{
        TextView record;
        TextView correct;
        TextView date;
        TextView worng;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //새로운 아이템 뷰 생성---------------------------------------------------
        View itemLayout = mLayoutInflater.inflate(R.layout.list_view_result,null);
        //------------------------------------------------------------------------
        //아이쳄에 뷰 홀더 설정---------------------------------------------------
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.date = (TextView) itemLayout.findViewById(R.id.date);
        viewHolder.record = (TextView) itemLayout.findViewById(R.id.record);
        viewHolder.correct = (TextView) itemLayout.findViewById(R.id.correct);
        viewHolder.worng = (TextView) itemLayout.findViewById(R.id.worng) ;

        itemLayout.setTag(viewHolder);
        //---------------------------------------------------------------------
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //아이템뷰에 저장된 뷰 홀더를 얻어온다.
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        //--------------------------------------------------------------------
        //현재 커서 위치에 이름, 학번, 학과 데이터를 얻어온다.
        String date = cursor.getString(cursor.getColumnIndex("date"));
        String record = cursor.getString(cursor.getColumnIndex("record"));
        String correct = cursor.getString(cursor.getColumnIndex("correct"));

        String worng = String.valueOf((Integer.parseInt(record)) - Integer.parseInt(correct));
        record+="전";
        correct+="승";
        worng+="패";
        //-------------------------------------------------------------------
        //이름, 학번, 학과 데이터를 뷰에 적용
        viewHolder.date.setText(date);
        viewHolder.record.setText(record);
        viewHolder.correct.setText(correct);
        viewHolder.worng.setText(worng);
        //--------------------------------------------------------------------
    }

}
