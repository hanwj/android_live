package com.xcyo.live.fragment.home;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.activity.message_list.MessageListActivity;
import com.xcyo.live.activity.search.SearchActivity;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class HomeFragPresenter extends BaseFragmentPresenter<HomeFragment,HomeFragRecord> {
    private String hotFilter = "热门";

    @Override
    public void loadDatas() {
        super.loadDatas();
        mapEvent(ServerEvents.RECENTCONTACT_LIST_UPDATE, new Event.EventCallback() {
            @Override
            public boolean onEvent(String evt, Object data) {
                mFragment.updateRecentContacts();
                return true;
            }
        });
    }

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("search".equals(str)){
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
            }else if("message".equals(str)){
                Intent intent = new Intent(getActivity(), MessageListActivity.class);
                getActivity().startActivity(intent);
            }else if ("hot".equals(str)){
                mFragment.startHomeFilterActivity("");
            }
        }
    }
}
