package com.xcyo.live.activity.appstart;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.live.R;
import com.xcyo.live.activity.login.LoginActivity;
import com.xcyo.live.activity.main.MainActivity;

/**
 * Created by meibo-design on 24/2/16.
 */
public class AppStartActivity extends BaseActivity<AppStartActPresent> {
    private int close = -1;
    @Override
    protected void initArgs() {
        if (getIntent() != null){
            close = getIntent().getIntExtra("close",-1);
        }
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_app_start);
        if (close > 0){
            finish();
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    startActivity(new Intent(AppStartActivity.this, MainActivity.class));
                    startActivity(new Intent(AppStartActivity.this, LoginActivity.class));
                }
            },2000);
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
