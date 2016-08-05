package com.xcyo.live.activity.user_edit_name;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/6.
 */
public class EditNameActivity extends BaseActivity<EditNamePresent>{
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_user_edit_name);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_finish = (TextView) findViewById(R.id.base_title_right);
        actionbar_finish.setText("保存"); actionbar_finish.setVisibility(View.VISIBLE);

        name = (EditText) findViewById(R.id.usr_edit_name_info);
        del = (ImageView) findViewById(R.id.usr_edit_name_del);
        del.setVisibility(View.GONE);
    }

    private EditText name;
    private ImageView del;

    private TextView actionbar_back, actionbar_title, actionbar_finish;

    public void clearEdit(){
        this.name.setText("");
    }

    @Override
    protected void initEvents() {
        addOnClickListener(del, "del");
        addOnClickListener(actionbar_finish, "finish_result");
        addOnClickListener(actionbar_back, "finish");
        name.addTextChangedListener(getTextWatcher());
    }

    protected String getEditContent(){
        return name.getText().toString().trim();
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    public TextWatcher getTextWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if(content == null || content.length() == 0){
                    del.setVisibility(View.GONE);
                }else if(content.length() > 0) {
                    del.setVisibility(View.VISIBLE);
                }
            }
        };
    }
}
