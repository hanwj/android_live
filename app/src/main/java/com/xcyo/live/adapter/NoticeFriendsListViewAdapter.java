package com.xcyo.live.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xcyo.live.R;
import com.xcyo.live.record.UserSimpleRecord;

import java.util.List;

/**
 * Created by caixiaoxiao on 22/6/16.
 */
public class NoticeFriendsListViewAdapter extends BaseAdapter{
    private Context mCtx;
    private List<UserSimpleRecord.User> mListData;

    public NoticeFriendsListViewAdapter(Context ctx,List<UserSimpleRecord.User> listData){
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            View v = new FriendItemView(mCtx);
            convertView = v;
        }
        FriendItemView view = (FriendItemView)convertView;
        UserSimpleRecord.User record = mListData.get(position);
        view.setName(record.alias);
        view.setLevel(record.level);
        view.setSignature(record.signature);
        return convertView;
    }

    class FriendItemView extends RelativeLayout implements Checkable{
        private ImageView mSelectedImg;
        private ImageView mHeadIcon;
        private TextView mName;
        private ImageView mSex;
        private TextView mLvl;
        private TextView mSignatureText;
        public FriendItemView(Context context) {
            super(context);
            View.inflate(context, R.layout.item_notice_friend, this);
            mSelectedImg = (ImageView)findViewById(R.id.item_selected);
            mHeadIcon = (ImageView)findViewById(R.id.item_head);
            mName = (TextView)findViewById(R.id.user_name);
            mSex = (ImageView)findViewById(R.id.user_sex);
            mLvl = (TextView)findViewById(R.id.user_lvl);
            mSignatureText = (TextView)findViewById(R.id.item_signature);
        }

        public FriendItemView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public FriendItemView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void setLevel(String level){
            mLvl.setText(level);
        }

        public void setName(String name){
            mName.setText(name);
        }

        public void setSignature(String signature){
            mSignatureText.setText(signature);
        }

        public void setSex(String sex){

        }

        @Override
        public void setChecked(boolean checked) {
            mSelectedImg.setVisibility(checked ? View.VISIBLE : View.GONE);
        }

        @Override
        public boolean isChecked() {
            return mSelectedImg.getVisibility() == View.VISIBLE;
        }

        @Override
        public void toggle() {
            if (mSelectedImg.getVisibility() == View.VISIBLE){
                mSelectedImg.setVisibility(View.GONE);
            }else{
                mSelectedImg.setVisibility(View.VISIBLE);
            }
        }
    }
}
