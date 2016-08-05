package com.xcyo.live.fragment.user_info_live;

import android.view.View;

import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by caixiaoxiao on 13/6/16.
 */
public class UserInfoLiveFragPresenter extends BaseFragmentPresenter<UserInfoLiveFragment,UserInfoLiveFragRecord>{
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("finish".equals(action)){
            getActivity().finish();
        }else if("reNew".equals(action)){
            mFragment.handReAction(action);
            handReNewAction();
        }else if("reHot".equals(action)){
            mFragment.handReAction(action);
            handReHotAction();
        }
    }

    private void handReNewAction(){
        handReHotAction();
    }

    private void handReHotAction(){

    }
}
