package com.xcyo.live.fragment.user_info_home;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.activity.rank.RankActivity;

/**
 * Created by caixiaoxiao on 13/6/16.
 */
public class UserInfoHomeFragPresenter extends BaseFragmentPresenter<UserInfoHomeFragment,UserInfoHomeFragRecord>{
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("rank".equals(str)){
                Intent intent = new Intent(getActivity(), RankActivity.class);
                mFragment.startActivity(intent);
            }
        }
    }
}
