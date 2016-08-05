package com.xcyo.live.fragment.search_user;

import android.view.View;

import com.xcyo.baselib.presenter.BaseFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by lovingyou on 2016/6/3.
 */
public class SearchUserFragPresenter extends BaseFragmentPresenter<SearchUserFragment,SearchUserFragRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {

    }

    public void getSearchData(String name){
        callServer(ServerEvents.CALL_SERVER_METHOD_USER_SEARCH,new PostParamHandler("name",name));
        mFragment.showLoading(true);
    }
}
