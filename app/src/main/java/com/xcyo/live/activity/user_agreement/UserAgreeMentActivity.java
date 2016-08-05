package com.xcyo.live.activity.user_agreement;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/13.
 */
public class UserAgreeMentActivity extends BaseActivity<UserAgreeMentPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_agreement);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_title.setText("悠悠直播服务和隐私条款");

        content = (TextView) findViewById(R.id.agreement_content);
    }

    private TextView actionbar_back, actionbar_title;
    private TextView content ;

    @Override
    protected void initEvents() {
        addOnClickListener(actionbar_back, "finish");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected void setContent(@NonNull final CharSequence ch){
        this.content.setText(ch);
    }
}
