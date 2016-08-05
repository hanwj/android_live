package com.xcyo.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.live.R;
import com.xcyo.live.record.UserSimpleRecord;
import com.xcyo.live.view.RoundImageView;
import com.xutils.image.ImageOptions;

import java.util.List;

/**
 * Created by lovingyou on 2016/1/4.
 */
public class SearchUserListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserSimpleRecord.User> userRecords;
    private ImageOptions imageOptions;

    public SearchUserListViewAdapter(Context mContext, List<UserSimpleRecord.User> userRecords) {
        this.mContext = mContext;
        this.userRecords = userRecords;
    }

    @Override
    public int getCount() {
        int count = userRecords.isEmpty() ? 0 : userRecords.size();
        return count;
    }

    @Override
    public Object getItem(int position) {
        return userRecords == null ? null : userRecords.get(position);
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
        UserSimpleRecord.User userRecord = userRecords.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_search_user, null);
            holder = new ViewHolder();
            holder.icon = (RoundImageView) convertView.findViewById(R.id.search_user_frag_listview_item_avatar);
            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            holder.sex = (ImageView) convertView.findViewById(R.id.user_sex);
            holder.lvl = (TextView) convertView.findViewById(R.id.user_lvl);
            holder.signature = (TextView) convertView.findViewById(R.id.search_user_frag_listview_item_info);
            holder.imgAddUser = (ImageView) convertView.findViewById(R.id.search_user_frag_listview_item_add_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(userRecord.alias);
        holder.signature.setText(userRecord.signature);
        return convertView;
    }

    class ViewHolder {
        RoundImageView icon;
        TextView name;
        ImageView sex;
        TextView lvl;
        TextView signature;
        ImageView imgAddUser;
    }

}
