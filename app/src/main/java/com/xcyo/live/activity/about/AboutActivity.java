package com.xcyo.live.activity.about;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/18.
 */
public class AboutActivity extends BaseActivity<AboutPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_about);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_finish = (TextView) findViewById(R.id.base_title_right);
        actionbar_title.setText("关于我们");

        community = (RelativeLayout)findViewById(R.id.about_community);
        secret = (RelativeLayout)findViewById(R.id.about_secret);
        service = (RelativeLayout)findViewById(R.id.about_service);
        lianxi = (RelativeLayout)findViewById(R.id.about_lianxi);
    }

    private RelativeLayout community, secret, service, lianxi;

    private TextView actionbar_back, actionbar_title, actionbar_finish;

    @Override
    protected void initEvents() {
        addOnClickListener(actionbar_back, "finish");
        addOnClickListener(community, "community");
        addOnClickListener(secret, "secret");
        addOnClickListener(service, "service");
        addOnClickListener(lianxi, "lianxi");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }
}
