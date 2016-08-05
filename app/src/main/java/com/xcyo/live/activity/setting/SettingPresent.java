package com.xcyo.live.activity.setting;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.activity.about.AboutActivity;
import com.xcyo.live.activity.login.LoginActivity;
import com.xcyo.live.activity.setting_black.BlackActivity;
import com.xcyo.live.activity.setting_chatmanager.ChatManagerActivity;
import com.xcyo.live.activity.setting_security.SecurityActivity;
import com.xcyo.live.activity.user_agreement.UserAgreeMentActivity;
import com.xcyo.live.model.UserModel;

/**
 * Created by TDJ on 2016/6/17.
 */
public class SettingPresent extends BaseActivityPresenter<SettingActivity, SettingRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("finish".equals(action)){
            getActivity().finish();
        }else if("account".equals(action)){
            goSecurity();
        }else if("blacklist".equals(action)){
            goBlack();
        }else if("chatmanager".equals(action)){
            goChatManager();
        }else if("clear".equals(action)){
            mActivity.startClear();
        }else if("helper".equals(action)){

        }else if("about".equals(action)){
            goAbout();
        }else if("logout".equals(action)){
            UserModel.getInstance().clearUserInfo();
            goLoginUsr();
        }
    }

    private void goLoginUsr(){
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        mActivity.finish();
    }

    private void goSecurity(){
        getActivity().startActivity(new Intent(getActivity(), SecurityActivity.class));
    }

    private void goBlack(){
        getActivity().startActivity(new Intent(getActivity(), BlackActivity.class));
    }

    private void goChatManager(){
        getActivity().startActivity(new Intent(getActivity(), ChatManagerActivity.class));
    }

    private void goAbout(){
        getActivity().startActivity(new Intent(getActivity(), AboutActivity.class));
    }
}
