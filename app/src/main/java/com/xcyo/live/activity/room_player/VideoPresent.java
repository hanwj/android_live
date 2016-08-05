package com.xcyo.live.activity.room_player;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.activity.live_end.LiveEndActivity;
import com.xcyo.live.activity.room.RoomPresent;
import com.xcyo.live.anim_live.SlidGift;
import com.xcyo.live.dialog.live_gift.LiveGiftDialogFragment;
import com.xcyo.live.dialog.live_gift.LiveGiftDialogPresent;
import com.xcyo.live.dialog.live_message_list.LiveMessageListDialogFragment;
import com.xcyo.live.dialog.live_share.LiveShareDialogFragment;
import com.xcyo.live.model.RoomModel;
import com.xcyo.live.record.RoomRecord;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by TDJ on 2016/6/2.
 */
public class VideoPresent extends RoomPresent<VideoActivity, VideoRecord> {

    @Override
    public void loadDatas() {
        super.loadDatas();
        callServer(ServerEvents.CALL_SERVER_METHOD_ROOM_LIVE,new PostParamHandler("roomId",mActivity.getRoomId()));
    }

    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {
        super.onServerCallback(evt, data);
        if(ServerEvents.CALL_SERVER_METHOD_ROOM_LIVE.equals(evt)){
            RoomRecord record = (RoomRecord)data.record;
            RoomModel.getInstance().setRoom(record.room);
            RoomModel.getInstance().setLive(record.live);
            RoomModel.getInstance().setSinger(record.singer);
            record().setRoom(record.room);
            record().setLive(record.live);
            record().setSinger(record.singer);
            mActivity.refreshSingerInfo();
            mActivity.setVideoPathWithStart(record.live.rtmpPullUrl);
            initChatRoom();
        }
    }

    @Override
    protected void onClick(View v, Object data) {
        super.onClick(v,data);
        String action = (String) data;
        if("recent".equals(action)){
            new LiveMessageListDialogFragment().show(mActivity.getSupportFragmentManager(),LiveMessageListDialogFragment.class.getName());
        }else if("share".equals(action)){
            new LiveShareDialogFragment().show(mActivity.getSupportFragmentManager(), LiveShareDialogFragment.class.getName());
        }else if("gift".equals(action)){
            new LiveGiftDialogFragment().setGiftCallBack(getGiftListener()).show(mActivity.getSupportFragmentManager(), LiveGiftDialogFragment.class.getName());
        }else if("exit".equals(action)){
            getActivity().startActivity(new Intent(getActivity(), LiveEndActivity.class));
            getActivity().finish();
        }
    }


    private LiveGiftDialogPresent.OnGiftCallBack getGiftListener(){
        return new LiveGiftDialogPresent.OnGiftCallBack() {
            @Override
            public void gift() {
                SlidGift.GiftRecord record = new SlidGift.GiftRecord();
                record.gid = ((int)(Math.random()*10))+"";
                record.disc = "兄弟会之剑";
                record.giftNumber = ((int)(Math.random()*100))+"";
                record.name = "哈哈";
                mActivity.getGiftManager().put(record);
            }
        };
    }
}
