package com.xcyo.live.activity.room_live;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.activity.live_end.LiveEndActivity;
import com.xcyo.live.activity.room.RoomPresent;
import com.xcyo.live.dialog.live_message_list.LiveMessageListDialogFragment;
import com.xcyo.live.model.RoomModel;
import com.xcyo.live.model.UserModel;
import com.xcyo.live.record.RoomRecord;
import com.xcyo.live.utils.ServerEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/2.
 */
public class LivePresent extends RoomPresent<LiveActivity, LiveRecord> {

    private String title = null;
    private String share = null;
    private String locate;

    @Override
    public void loadDatas() {
        super.loadDatas();
        Intent mainIntent = mActivity.getMainIntent();
        title = mainIntent.getStringExtra("title");
        share = mainIntent.getStringExtra("share");
        locate = mainIntent.getStringExtra("locate");
        createRoom(title, share, locate);
    }

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        super.onServerCallback(evt, data);
        if(ServerEvents.CALL_SERVER_METHOD_ROOM_CREATE.equals(evt)){
            RoomRecord record = (RoomRecord)data.record;
            RoomModel.getInstance().setRoom(record.room);
            RoomModel.getInstance().setLive(record.live);
            RoomModel.getInstance().setSinger(record.singer);
            record().setRoom(record.room);
            record().setLive(record.live);
            record().setSinger(record.singer);
            mActivity.refreshSingerInfo();
            mActivity.setVideoPathWithStart(RoomModel.getInstance().getLive().pushUrl);
            initChatRoom();
        }
    }

    @Override
    protected void onClick(View v, Object data) {
        super.onClick(v,data);
        String action = (String) data;
        if("follow".equals(action)){
        }else if("recent".equals(action)){
            new LiveMessageListDialogFragment().show(mActivity.getSupportFragmentManager(), LiveMessageListDialogFragment.class.getName());
        }else if("music".equals(action)){
        }else if("pop".equals(action)){
            mActivity.getLiveHelper().showUpPop(v);
        }else if("exit".equals(action)){
            getActivity().startActivity(new Intent(getActivity(), LiveEndActivity.class));
            getActivity().finish();
        }
    }

    private void createRoom(String title, String share, String locate){
        List<String> args = new ArrayList<>();
        args.add("uid"); args.add(UserModel.getInstance().getUid());
//        args.add("uid"); args.add("1013387811");
        if(!TextUtils.isEmpty(title)){
            args.add("title");
            args.add(title);
        }
        if(!TextUtils.isEmpty(share)){
            args.add("share");
            args.add(share);
        }
        if(!TextUtils.isEmpty(locate)){
            args.add("locate");
            args.add(locate);
        }
        callServer(ServerEvents.CALL_SERVER_METHOD_ROOM_CREATE, new PostParamHandler(args.toArray(new String[args.size()])));
    }
}
