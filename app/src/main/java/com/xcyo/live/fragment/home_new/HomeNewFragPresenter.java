package com.xcyo.live.fragment.home_new;

import android.view.View;

import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by caixiaoxiao on 2/6/16.
 */
public class HomeNewFragPresenter extends BaseFragmentPresenter<HomeNewFragment,HomeNewFragRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if (str.startsWith("topic")){
                int pos = Integer.parseInt(str.substring(5)) - 1;
                mFragment.startTopicContentActivity(pos);
            }else if ("more_topic".equals(str)){
                mFragment.startTopicListActivity();
            }
        }
    }

    protected void loadSingerList(){
        callServer(ServerEvents.CALL_SERVER_METHOD_LIVE_NEW,new PostParamHandler());
    }
}
