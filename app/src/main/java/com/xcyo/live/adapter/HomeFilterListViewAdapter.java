package com.xcyo.live.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xcyo.live.view.HomeFilterItemView;

import java.util.List;

/**
 * Created by caixiaoxiao on 7/6/16.
 */
public class HomeFilterListViewAdapter extends BaseAdapter {
    private Context mCtx;
    private List<String> mListData;

    public HomeFilterListViewAdapter(Context ctx,List<String> listData){
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
        if (convertView == null){
            View v = new HomeFilterItemView(mCtx);
            convertView = v;
        }
        HomeFilterItemView view = (HomeFilterItemView)convertView;
        String record = mListData.get(position);
        view.setName(record);
        view.setNum("123");
        return convertView;
    }
}