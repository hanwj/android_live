package com.xcyo.live.fragment.home_follow;

import android.view.View;

import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.fragment.home.HomeFragment;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by caixiaoxiao on 2/6/16.
 */
public class HomeFollowFragPresenter extends BaseFragmentPresenter<HomeFollowFragment,HomeFollowFragRecord>{
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("gotoHot".equals(str)){
                ((HomeFragment)mFragment.getParentFragment()).setCurrentViewPager(1,true);
            }
        }
    }

    public void loadSingerList(){
        callServer(ServerEvents.CALL_SERVER_METHOD_LIVE_FOLLOW,new PostParamHandler());
    }
}
