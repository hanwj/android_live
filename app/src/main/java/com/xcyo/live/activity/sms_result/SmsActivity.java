package com.xcyo.live.activity.sms_result;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/7.
 */
public class SmsActivity extends BaseActivity<SmsPresent> {


    protected int R_Code = -1;

    @Override
    protected void initArgs() {
        R_Code = getIntent().getIntExtra(SmsRecord.ACTION_SMS, -1);
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_sms);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_title.setText("短信验证");

        phone_number = (EditText)findViewById(R.id.sms_phone_number);
        phone_get = (TextView)findViewById(R.id.sms_phone_get);
        phone_code = (EditText)findViewById(R.id.sms_phone_code);

        phone_next = (TextView) findViewById(R.id.sms_next);
    }

    private TextView actionbar_back, actionbar_title;

    private EditText phone_number, phone_code;
    private TextView phone_get;
    private TextView phone_next;

    @Override
    protected void initEvents() {
        addOnClickListener(actionbar_back, "finish");
        addOnClickListener(phone_next, "next");
        addOnClickListener(phone_get, "get_msg");
        phone_get.setClickable(false);
        phone_number.addTextChangedListener(getTextWatcher());
    }

    private TextWatcher getTextWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if(content == null)
                    return ;
                if(content.length() > 0){
                    String lastChar = content.substring(content.length() - 1);
                    if(!lastChar.matches("\\d")){
                        s.delete(content.length() - 1, content.length());
                        return ;
                    }
                    if(content.length() < 11){
                        phone_get.setSelected(false);
                        phone_get.setClickable(false);
                    }else if(content.length() == 11){
                        phone_get.setSelected(true);
                        phone_get.setClickable(true);
                    }else if(content.length() > 11){
                        s.delete(11, content.length());
                    }
                }else if(content.length() == 0){
                    phone_get.setSelected(false);
                    phone_get.setClickable(false);
                }
            }
        };
    }

    protected void setGetText(String text){
        if(TextUtils.isEmpty(text)){
            this.phone_get.setText("验证");
            this.phone_get.setClickable(true);
            this.phone_get.setSelected(true);
        }else{
            this.phone_get.setText(text);
            this.phone_get.setSelected(true);
            this.phone_get.setClickable(false);
        }
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter().onDestroy();
    }
}
