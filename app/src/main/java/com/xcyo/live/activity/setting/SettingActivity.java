package com.xcyo.live.activity.setting;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.utils.FileUtils;
import com.xcyo.live.R;
import com.xcyo.live.utils.Constants;

import java.io.File;

/**
 * Created by TDJ on 2016/6/17.
 */
public class SettingActivity extends BaseActivity<SettingPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_setting);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_finish = (TextView) findViewById(R.id.base_title_right);
        actionbar_title.setText("设置");

        account = (RelativeLayout)findViewById(R.id.setting_account);
        blacklist = (RelativeLayout)findViewById(R.id.setting_blacklist);
        chatmanager = (RelativeLayout)findViewById(R.id.setting_chatmanager);

        clear = (RelativeLayout)findViewById(R.id.setting_clear); cache = (TextView)findViewById(R.id.setting_cache_size);
        loading = findViewById(R.id.setting_clear_loading);
        helper = (RelativeLayout)findViewById(R.id.setting_helper);
        about = (RelativeLayout)findViewById(R.id.setting_about);

        logout = (TextView)findViewById(R.id.setting_logout);
        version = (TextView)findViewById(R.id.setting_version);
    }

    private TextView actionbar_back, actionbar_title, actionbar_finish;

    private RelativeLayout account;
    private RelativeLayout blacklist;
    private RelativeLayout chatmanager;

    private RelativeLayout clear;
    private TextView cache;
    private android.view.View loading;
    private RelativeLayout helper;
    private RelativeLayout about;

    private TextView logout;
    private TextView version;

    @Override
    protected void initEvents() {
        addOnClickListener(actionbar_back, "finish");
        addOnClickListener(account, "account");
        addOnClickListener(blacklist, "blacklist");
        addOnClickListener(chatmanager, "chatmanager");

        addOnClickListener(clear, "clear");
        addOnClickListener(helper, "helper");
        addOnClickListener(about, "about");

        addOnClickListener(logout, "logout");
        addOnClickListener(version, "version");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        cache.setText(countCache());
    }

    private String countCache(){
        File file = new File(Constants.SD_CACHE);
        if(file.exists()){
            return FileUtils.getFormatSize(file);
        }
        return "0B";
    }

    protected void startClear(){
        cache.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        Animation rote = AnimationUtils.loadAnimation(this, R.anim.anim_setting_cache);
        rote.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                try {
                    FileUtils.clearFile(Constants.SD_CACHE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loading.setVisibility(View.GONE);
                cache.setVisibility(View.VISIBLE);
                cache.setText(countCache());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        loading.setAnimation(rote);
        rote.start();
    }

}
