package com.xcyo.live.activity.room.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

/**
 * Created by caixiaoxiao on 8/7/16.
 */
public abstract class ChatRoomMsgViewHolderBase {
    private View view;
    private Context mCtx;
    protected ChatRoomMsgRecord mRecord;
    public View getView(Context ctx){
        mCtx = ctx;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        view = inflater.inflate(getResId(),null);
        inflate();
        view.setTag(this);
        return view;
    }

    protected View findViewById(int id){
        return view.findViewById(id);
    }
    public void setRecord(ChatRoomMsgRecord record){
        this.mRecord = record;
        bindData(record);
    }
    protected abstract void inflate();
    protected abstract int getResId();
    protected abstract void bindData(ChatRoomMsgRecord record);
    public abstract int getType();

    public static ChatRoomMsgViewHolderBase getViewHolderByMessage(ChatRoomMsgRecord record){
        ChatRoomMsgViewHolderBase holder = null;
        if (ChatRoomMsgRecord.TEXT == record.type){
            holder = new ChatRoomMsgViewHolderText();
        }
        return holder;
    }
}
