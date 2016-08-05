package com.xcyo.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.live.R;
import com.xcyo.live.record.TopicRecord;

import java.util.List;

/**
 * Created by caixiaoxiao on 3/6/16.
 */
public class TopicListViewAdapter extends BaseAdapter {
    private Context mCtx;
    private List<TopicRecord> mListData;

    public TopicListViewAdapter(Context ctx,List<TopicRecord> listData){
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
        if (convertView == null){
            View v = LayoutInflater.from(mCtx).inflate(R.layout.item_one_topic,null);
            holder = new ViewHolder();
            holder.name = (TextView)v.findViewById(R.id.item_name);
            holder.num = (TextView)v.findViewById(R.id.item_num);
            v.setTag(holder);
            convertView = v;
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        TopicRecord record = mListData.get(position);
        holder.name.setText(record.topic);
        holder.num.setText(record.num + "直播");
        return convertView;
    }

    class ViewHolder{
        public TextView name;
        public TextView num;
    }
}
