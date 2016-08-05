package com.xcyo.live.activity.sms_result;

import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by TDJ on 2016/6/7.
 */
public class SmsPresent extends BaseActivityPresenter<SmsActivity, SmsRecord> {

    private final Handler mHandler = new Handler();
    private final ExecutorService execu = Executors.newSingleThreadExecutor();
    private final static int INIT_TIME = 6;
    private static int i = -1;

    private static final String RECORD_SMS_PHONE_NUMBER = "com.xcyo.live.user.sms.record.phone.number";
    private SharedPreferences shareference;

    @Override
    public void loadDatas() {
        super.loadDatas();
        shareference = getActivity().getSharedPreferences(RECORD_SMS_PHONE_NUMBER, 0);
        long timer = shareference.getLong(RECORD_SMS_PHONE_NUMBER, -1);
        if(timer > 0){
            i = (int)(timer - System.currentTimeMillis()) / 1000;
            if(i > 0){
                execu.execute(getRunnable());
            }
        }
    }

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("finish".equals(action)){
            getActivity().finish();
        }else if("next".equals(action)){

        }else if("get_msg".equals(action)){
            sendMsg();
        }
    }

    private void sendMsg(){
        if(execu.isShutdown()){
            execu.shutdownNow();
        }
        execu.execute(getRunnable());
        android.util.Log.d("TAG", "开始发送");
    }

    private Runnable getRunnable(){
        return new Runnable() {
            @Override
            public void run() {
                int durTime = INIT_TIME;
                if(i > 0){
                    durTime = i;
                    i = -1;
                }
                while(durTime >= 0){
                    mHandler.post(getHandlerRunnable(durTime));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    durTime = durTime - 1;
                }
                mHandler.post(getHandlerRunnable(-1));
            }
        };
    }

    private Runnable getHandlerRunnable(final int t){
        return new Runnable() {
            @Override
            public void run() {
                String content = t+"s";
                if(t < 0){
                    content = null;
                }
                ((SmsActivity)getActivity()).setGetText(content);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        String content = ((TextView)getActivity().findViewById(R.id.sms_phone_get)).getText().toString().trim();
        Matcher matcher= Pattern.compile("\\d+").matcher(content);
        if(matcher.find()){
            String s = matcher.group();
            if(!TextUtils.isEmpty(s) && s.matches("\\d+")){
                shareference.edit().putLong(RECORD_SMS_PHONE_NUMBER, System.currentTimeMillis()+(Integer.parseInt(s)*1000)).apply();
            }
        }
    }

}
