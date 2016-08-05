package com.xcyo.live.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.live.activity.room.viewholder.ChatRoomMsgRecord;
import com.xcyo.live.activity.room.viewholder.ChatRoomMsgViewHolderBase;

import java.util.List;

/**
 * Created by caixiaoxiao on 7/7/16.
 */
public class RoomChatMsgListViewAdapter extends BaseAdapter{
    private Context mCtx;
    private List<ChatRoomMsgRecord> mListData;

    public RoomChatMsgListViewAdapter(Context ctx, List<ChatRoomMsgRecord> listData){
        mCtx = ctx;
        mListData = listData;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatRoomMsgRecord record = mListData.get(position);
        ChatRoomMsgViewHolderBase holder = ChatRoomMsgViewHolderBase.getViewHolderByMessage(record);
        convertView = holder.getView(mCtx);
        holder.setRecord(record);
        return convertView;
    }

}
