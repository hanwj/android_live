package com.xcyo.live.activity.login_sms;

import android.content.Intent;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.activity.login_area.AreaRecord;

/**
 * Created by TDJ on 2016/6/7.
 */
public class LoginSMSActivity extends BaseActivity<LoginSMSPresent> {

    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_login_sms);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_title.setText("短信验证");

        contact = findViewById(R.id.sms_phone_contact);
        mCN = (TextView)findViewById(R.id.sms_phone_cn);
        phone_number = (EditText)findViewById(R.id.sms_phone_number);
        phone_get = (TextView)findViewById(R.id.sms_phone_get);
        phone_code = (EditText)findViewById(R.id.sms_phone_code);

        phone_next = (TextView) findViewById(R.id.sms_next);
    }

    private TextView actionbar_back, actionbar_title;

    private View contact;
    private TextView mCN;
    private EditText phone_number, phone_code;
    private TextView phone_get;
    private TextView phone_next;

    @Override
    protected void initEvents() {
        addOnClickListener(actionbar_back, "finish");
        addOnClickListener(phone_next, "next");
        addOnClickListener(phone_get, "get_msg");
        addOnClickListener(contact, "contact");
        phone_get.setClickable(false);
        phone_number.addTextChangedListener(getTextWatcher());
    }

    protected String getMobile(){
        return phone_number.getText().toString().trim();
    }

    protected String getMobileCode(){
        return phone_code.getText().toString().trim();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123){
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                android.net.Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if(cursor != null){
                    cursor.moveToFirst();
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
                    android.content.ContentResolver reContentResolverol = getContentResolver();
                    Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null,
                            null);
                    if (phone != null && phone.moveToNext()) {
                        String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phone_number.setText(usernumber);
                    }
                }

            }else if(resultCode == 1001){
                AreaRecord record = (AreaRecord)data.getSerializableExtra("area_result");
                if(record != null){
                    mCN.setText("+"+record.getNumber()+" "+record.getCh());
                }
            }
        }
    }
}
