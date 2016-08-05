package com.xcyo.live.activity.setting_security;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.activity.sms_result.SmsActivity;
import com.xcyo.live.activity.sms_result.SmsRecord;

/**
 * Created by TDJ on 2016/6/18.
 */
public class SecurityPresent extends BaseActivityPresenter<SecurityActivity, SecurityRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String)data;
        if("phone".equals(action)){
            goSmsActivity();
        }else if("finish".equals(action)){
            getActivity().finish();
        }
    }

    public void goSmsActivity(){
        Intent mIntent = new Intent(getActivity(), SmsActivity.class);
        mIntent.putExtra(SmsRecord.ACTION_SMS, SmsRecord.Q_SECURITY_CODE);
        getActivity().startActivityForResult(mIntent, 101);
    }

}
