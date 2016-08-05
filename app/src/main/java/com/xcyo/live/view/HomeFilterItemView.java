package com.xcyo.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcyo.live.R;

/**
 * Created by caixiaoxiao on 7/6/16.
 */
public class HomeFilterItemView extends RelativeLayout implements Checkable {

    private ImageView mSelectedImg;
    private TextView mNameText;
    private TextView mNumText;
    public HomeFilterItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_home_filter_city, this);
        mSelectedImg = (ImageView)findViewById(R.id.city_selected);
        mNameText = (TextView)findViewById(R.id.city_name);
        mNumText = (TextView)findViewById(R.id.city_num);
    }

    public HomeFilterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeFilterItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setName(String text){
        mNameText.setText(text);
    }

    public void setNum(String text){
        mNumText.setText(text);
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
        }else {
            mSelectedImg.setVisibility(View.VISIBLE);
        }
    }
}
