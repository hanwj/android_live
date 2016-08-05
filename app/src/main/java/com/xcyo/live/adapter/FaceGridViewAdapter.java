package com.xcyo.live.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;

import java.util.List;

/**
 * Created by caixiaoxiao on 12/6/16.
 */
public class FaceGridViewAdapter extends BaseAdapter{
    private Context mCtx;
    private List<String> mListData;
    public FaceGridViewAdapter(Context ctx,List<String> listData){
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
        ImageView view;
        if (convertView == null){
            view = new ImageView(mCtx);
            view.setLayoutParams(new AbsListView.LayoutParams(Util.dp(30),Util.dp(30)));
            convertView = view;
        }else {
            view = (ImageView)convertView;
        }
        if (!mListData.get(position).equals("delete")){
            view.setImageResource(R.mipmap.message_face_icon);
        }else {
            view.setImageResource(R.mipmap.face_delete_icon);
        }
        return view;
    }
}
