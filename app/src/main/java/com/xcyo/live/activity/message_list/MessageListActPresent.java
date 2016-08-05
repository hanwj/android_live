package com.xcyo.live.activity.message_list;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.nim.NIMUserInfoCache;
import com.xcyo.live.utils.ServerEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovingyou on 2016/6/2.
 */
public class MessageListActPresent extends BaseActivityPresenter<MessageListActivity,MessageListActRecord> {

    @Override
    public void loadDatas() {
        super.loadDatas();
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //获取最近会话
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int i, List<RecentContact> recentContacts, Throwable throwable) {
                        updateRecents(recentContacts);
                    }
                });
            }
        }.sendEmptyMessageDelayed(0,100);

        mapEvent(ServerEvents.RECENTCONTACT_LIST_UPDATE, new Event.EventCallback() {
            @Override
            public boolean onEvent(String evt, Object data) {
                final List<RecentContact> recents = (List<RecentContact>)data;
                updateRecents(recents);
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
            if ("back".equals(str)){
                mActivity.finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void updateRecents(final List<RecentContact> recents){
        if (recents == null){
            return;
        }
        List<String> accouts = new ArrayList<String>();
        for (RecentContact recent : recents){
            accouts.add(recent.getContactId());
        }
        NIMUserInfoCache.getInstance().getUserInfoFromRemote(accouts, new RequestCallback<List<NimUserInfo>>() {
            @Override
            public void onSuccess(List<NimUserInfo> nimUserInfos) {
                mActivity.updateRecentContacts(recents);
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }
}
