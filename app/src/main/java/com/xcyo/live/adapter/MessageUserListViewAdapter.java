package com.xcyo.live.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xcyo.live.R;
import com.xcyo.live.nim.NIMUserInfoCache;
import com.xcyo.live.view.RoundImageView;
import com.xutils.image.ImageOptions;

import java.util.List;

/**
 * Created by lovingyou on 2016/1/4.
 */
public class MessageUserListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<RecentContact> userRecords;
    private ImageOptions imageOptions;

    public MessageUserListViewAdapter(Context mContext, List<RecentContact> userRecords) {
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
        RecentContact record = userRecords.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_message_user, null);
            holder = new ViewHolder();
            holder.icon = (RoundImageView) convertView.findViewById(R.id.message_user_frag_listview_item_avatar);
            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            holder.sex = (ImageView) convertView.findViewById(R.id.user_sex);
            holder.lvl = (TextView) convertView.findViewById(R.id.user_lvl);
            holder.recentContent = (TextView) convertView.findViewById(R.id.message_user_frag_listview_item_chat_info);
            holder.time = (TextView) convertView.findViewById(R.id.message_user_frag_listview_item_time);
            holder.redTip = (TextView)convertView.findViewById(R.id.item_red_tip);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NimUserInfo userInfo = NIMUserInfoCache.getInstance().getUserInfo(record.getContactId());

        String name = TextUtils.isEmpty(userInfo.getName()) ? record.getContactId() : userInfo.getName();
        holder.name.setText(name);

        String content = "";
        switch (record.getMsgType()) {
            case text:
                content = record.getContent();
                break;
            case image:
                content = "[图片]";
                break;
            case audio:
                content = "[语音消息]";
                break;
            default:
                content = "[其他]";
                break;
        }
        holder.recentContent.setText(content);

        int unreadCount = record.getUnreadCount();
        if (unreadCount > 0){
            holder.redTip.setVisibility(View.VISIBLE);
            holder.redTip.setText(unreadCount+"");
        }else{
            holder.redTip.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        RoundImageView icon;
        TextView name;
        ImageView sex;
        TextView lvl;
        TextView recentContent;
        TextView time;
        TextView redTip;
    }

}
