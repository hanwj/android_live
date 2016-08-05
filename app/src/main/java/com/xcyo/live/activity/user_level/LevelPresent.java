package com.xcyo.live.activity.user_level;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.activity.user_right.RightActivity;

/**
 * Created by TDJ on 2016/6/6.
 */
public class LevelPresent extends BaseActivityPresenter<LevelActivity, LevelRecord>{
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("rightDetail".equals(action)){
            goRightDetail();
        }
    }

    private void goRightDetail(){
        getActivity().startActivity(new Intent(getActivity(), RightActivity.class));
    }
}
