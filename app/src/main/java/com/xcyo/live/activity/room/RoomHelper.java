package com.xcyo.live.activity.room;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xcyo.baselib.server.paramhandler.PostParamHandler;
import com.xcyo.live.activity.room.viewholder.ChatRoomMsgRecord;
import com.xcyo.live.utils.ServerEvents;

/**
 * Created by TDJ on 2016/7/13.
 */
public class RoomHelper {

    private RoomPresent mPresent;

    public RoomHelper(RoomPresent present){
        this.mPresent = present;
    }

    public void sendDan(@NonNull String roomId, @NonNull String content){
        if(!TextUtils.isEmpty(roomId) && !TextUtils.isEmpty(content)){
            content = ChatRoomMsgRecord.createRoomBarrageTextMsg(content);
            mPresent.callServer(ServerEvents.CALL_SERVER_METHOD_ROOM_SEND_END, new PostParamHandler("roomId", roomId, "content", content));
        }
    }

    protected void destory(){
        this.mPresent = null;
    }
}
