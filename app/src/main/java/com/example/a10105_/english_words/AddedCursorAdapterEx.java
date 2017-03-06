package com.example.a10105_.english_words;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by 10105-김유진 on 2016-06-24.
 */
public class AddedCursorAdapterEx extends CursorAdapter{

    Context     mContext = null;
    LayoutInflater  mLayoutInflater = null;

    public AddedCursorAdapterEx(Context context, Cursor c, int flags) {
        super(context, c,flags);

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    class ViewHolder{
        TextView english; //추가된 영어단어
        TextView hanguel; //추가된 영어단어의 뜻

        TextView record;
        TextView correct;
        TextView date;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //새로운 아이템 뷰 생성---------------------------------------------------
        View itemLayout = mLayoutInflater.inflate(R.layout.list_view_item,null);
        //------------------------------------------------------------------------
        //아이쳄에 뷰 홀더 설정---------------------------------------------------
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.english = (TextView) itemLayout.findViewById(R.id.list_eng);
        viewHolder.hanguel = (TextView) itemLayout.findViewById(R.id.list_han);

        viewHolder.date = (TextView) itemLayout.findViewById(R.id.date);
        viewHolder.record = (TextView) itemLayout.findViewById(R.id.record);
        viewHolder.correct = (TextView) itemLayout.findViewById(R.id.correct);

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
        String eng = cursor.getString(cursor.getColumnIndex("english"));
        String han = cursor.getString(cursor.getColumnIndex("hanguel"));
        //-------------------------------------------------------------------
        //이름, 학번, 학과 데이터를 뷰에 적용
        viewHolder.english.setText(eng);
        viewHolder.hanguel.setText(han);
        //--------------------------------------------------------------------
    }
    
}
