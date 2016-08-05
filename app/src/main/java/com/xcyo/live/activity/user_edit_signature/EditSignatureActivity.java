package com.xcyo.live.activity.user_edit_signature;

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
public class EditSignatureActivity extends BaseActivity<EditSignaturePresent>{
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_user_editsignature);

        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_finish = (TextView) findViewById(R.id.base_title_right);
        actionbar_finish.setText("保存"); actionbar_finish.setVisibility(View.VISIBLE);

        info = (EditText) findViewById(R.id.usr_edit_signature_info);
        leave = (TextView) findViewById(R.id.usr_edit_signature_sp);
        leave.setText("最多还可以输入"+TEXT_COUNT+"个字");

        number = (TextView) findViewById(R.id.usr_edit_signature_number);
        del = (ImageView) findViewById(R.id.usr_edit_signature_del); del.setVisibility(View.GONE);
    }

    private EditText info;
    private TextView leave;
    private TextView number;
    private ImageView del;

    private TextView actionbar_back, actionbar_title, actionbar_finish;
    private final int TEXT_COUNT = 64;

    @Override
    protected void initEvents() {
        addOnClickListener(del, "del");
        addOnClickListener(actionbar_finish, "finish");
        addOnClickListener(actionbar_back, "finish");
        info.addTextChangedListener(getTextWatcher());
    }

    public void clearEdit(){
        this.info.setText("");
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
                if(content == null){
                    del.setVisibility(View.GONE);
                    return ;
                }
                if(content.length() > 0){
                    del.setVisibility(View.VISIBLE);
                }else{
                    del.setVisibility(View.GONE);
                }
                number.setText(content.length()+"");
                leave.setText("最多还可以输入"+(TEXT_COUNT - content.length())+"个字");
            }
        };
    }
}
