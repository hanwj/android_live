package com.xcyo.live.activity.user_wxbind;

import android.view.View;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/7.
 */
public class BindWXActivity extends BaseActivity<BindWXPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_bindwx);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_finish = (TextView) findViewById(R.id.base_title_right);
        actionbar_title.setText("微信绑定");

        goBind = (TextView) findViewById(R.id.bindwx_btn);
    }

    private TextView goBind;

    private TextView actionbar_back, actionbar_title, actionbar_finish;

    @Override
    protected void initEvents() {
        addOnClickListener(goBind, "goBind");
        addOnClickListener(actionbar_finish, "finish");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }
}
