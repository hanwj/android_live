package com.xcyo.live.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.activity.user_info.UserInfoActivity;
import com.xcyo.live.record.SingerRecord;

import java.util.List;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class HomeSingerListViewAdapter extends BaseAdapter {

    private List<SingerRecord> mListData;
    private Context mCtx;
    public HomeSingerListViewAdapter(Context ctx, List<SingerRecord> listData){
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
        ViewHolder holder = null;
        if (convertView != null){
            holder = (ViewHolder)convertView.getTag();
        }else {
            holder = new ViewHolder();
            View v = LayoutInflater.from(mCtx).inflate(R.layout.item_home_singer_card,null);
            holder.name = (TextView)v.findViewById(R.id.item_name);
            holder.location = (TextView)v.findViewById(R.id.item_location);
            holder.num = (TextView)v.findViewById(R.id.item_num);
            holder.topic = (TextView)v.findViewById(R.id.item_topic);
            holder.topicLayout = v.findViewById(R.id.item_topic_layout);
            holder.icon = (ImageView)v.findViewById(R.id.item_head);
            holder.photo = (ImageView)v.findViewById(R.id.item_photo);
            holder.liveStatus = (ImageView)v.findViewById(R.id.item_live_status);
            v.setTag(holder);
            convertView = v;
        }
        final SingerRecord record = mListData.get(position);
        holder.name.setText(record.alias);
        holder.location.setText(record.locate);
        holder.num.setText(record.memberNum);
        if (TextUtils.isEmpty(record.title)){
            holder.topicLayout.setVisibility(View.GONE);
        }else {
            holder.topicLayout.setVisibility(View.VISIBLE);
            holder.topic.setText(record.title);
        }
        holder.liveStatus.setVisibility(record.isLive ? View.VISIBLE : View.GONE);

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, UserInfoActivity.class);
                intent.putExtra("uid",record.uid);
                intent.putExtra("alias",record.alias);
                mCtx.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        public TextView name;
        public TextView location;
        public TextView num;
        public TextView topic;
        public View topicLayout;
        public ImageView icon;
        public ImageView photo;
        public ImageView liveStatus;

    }
}
