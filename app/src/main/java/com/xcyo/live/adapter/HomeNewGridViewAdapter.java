package com.xcyo.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.live.R;
import com.xcyo.live.record.SingerRecord;

import java.util.List;

/**
 * Created by caixiaoxiao on 2/6/16.
 */
public class HomeNewGridViewAdapter extends BaseAdapter {
    private Context mCtx;
    private List<SingerRecord> mListData;
    public HomeNewGridViewAdapter(Context ctx,List<SingerRecord> listData){
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
        ViewHolder holder = null;
        if (convertView == null){
            View v = LayoutInflater.from(mCtx).inflate(R.layout.item_home_new_singer_small_card,null);
            holder = new ViewHolder();
            holder.name = (TextView)v.findViewById(R.id.item_name);
            holder.lvl = (TextView)v.findViewById(R.id.item_lvl);
            holder.head = (ImageView)v.findViewById(R.id.item_head);
            v.setTag(holder);
            convertView = v;
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
//        if (position%3 == 0){
//            convertView.setPadding(Util.dp(8),0,0,0);
//        }else if(position%3 == 2){
//            convertView.setPadding(0,0,Util.dp(8),0);
//        }else {
//            convertView.setPadding(0,0,0,0);
//        }
        SingerRecord record = mListData.get(position);
        holder.name.setText(record.alias);
        return convertView;
    }

    class ViewHolder{
        public TextView name;
        public TextView lvl;
        public ImageView head;
    }
}
