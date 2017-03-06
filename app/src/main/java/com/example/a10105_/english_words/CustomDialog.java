package com.example.a10105_.english_words;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by KIMYUJIN on 2016-07-10.
 */
public class CustomDialog extends Dialog {
    Button mOKBtn = null;
    Button mCancelBtn = null;
    EditText mEditText = null;
    RadioButton english = null;
    RadioButton hanguel = null;
    public CustomDialog(Context context)
    {
        super(context);

        setContentView(R.layout.modify);

        mOKBtn = (Button)findViewById(R.id.ok_btn);
        mCancelBtn =(Button)findViewById(R.id.cancel_btn);
        mEditText=(EditText)findViewById(R.id.edittext_modify);
        english = (RadioButton)findViewById(R.id.radio_btn_english);
        hanguel = (RadioButton)findViewById(R.id.radio_btn_hanguel);
     }

    public void setOkClickListener(View.OnClickListener _okListener)
    {
        mOKBtn.setOnClickListener(_okListener);
    }

    public void setCancleClickListener( View.OnClickListener _cancelListener){
        mCancelBtn.setOnClickListener(_cancelListener);
    }
    
}
