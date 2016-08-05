package com.xcyo.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.xcyo.live.fragment.message.viewholder.MsgViewHolderBase;

import java.util.List;

/**
 * Created by caixiaoxiao on 5/7/16.
 */
public class MessageListViewAdapter extends BaseAdapter{
    private Context mCtx;
    private List<IMMessage> mListData;
    public MessageListViewAdapter(Context ctx,List<IMMessage> listData){
        this.mCtx = ctx;
        this.mListData = listData;
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
        IMMessage msg = mListData.get(position);
        if (convertView == null){
            convertView = getView(msg);
        }
        MsgViewHolderBase holder = (MsgViewHolderBase)convertView.getTag();
        holder.refresh(msg);
        return convertView;
    }

    private View getView(IMMessage msg){
        MsgViewHolderBase holder = MsgViewHolderBase.getViewHolderByMessage(msg);
        View view = holder.getView(LayoutInflater.from(mCtx));
        view.setTag(holder);
        return view;
    }
}
