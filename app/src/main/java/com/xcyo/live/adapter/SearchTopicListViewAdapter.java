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
import com.xutils.image.ImageOptions;

import java.util.List;

/**
 * Created by lovingyou on 2016/1/4.
 */
public class SearchTopicListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<TopicRecord> mListData;
    private ImageOptions imageOptions;

    public SearchTopicListViewAdapter(Context mContext, List<TopicRecord> listData) {
        this.mContext = mContext;
        this.mListData = listData;
    }

    @Override
    public int getCount() {
        int count = mListData.isEmpty() ? 0 : mListData.size();
        return count;
    }

    @Override
    public Object getItem(int position) {
        return mListData == null ? null : mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.default_user_icon)
                .setFailureDrawableId(R.mipmap.default_user_icon)
                .build();
        TopicRecord record = mListData.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_search_topic, null);
            holder = new ViewHolder();
            holder.textUserTopic = (TextView) convertView.findViewById(R.id.search_topic_frag_listview_item_topic);
            holder.textUserCount = (TextView) convertView.findViewById(R.id.search_topic_frag_listview_item_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textUserTopic.setText("#"+record.topic+"#");
        holder.textUserCount.setText(record.num+"直播");
        return convertView;
    }

    class ViewHolder {
        TextView textUserTopic;
        TextView textUserCount;
    }

}
