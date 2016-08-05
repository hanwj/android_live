package com.xcyo.live.activity.about;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.activity.user_agreement.UserAgreeMentActivity;
import com.xcyo.live.model.UserModel;

/**
 * Created by TDJ on 2016/6/18.
 */
public class AboutPresent extends BaseActivityPresenter<AboutActivity, AboutRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("finish".equals(action)){
            getActivity().finish();
        }else if("community".equals(action)){
        }else if("secret".equals(action)){
            getActivity().startActivity(new Intent(getActivity(), UserAgreeMentActivity.class));
        }else if("service".equals(action)){
        }else if("lianxi".equals(action)){
        }
    }
}
