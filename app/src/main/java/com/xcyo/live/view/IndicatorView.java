package com.xcyo.live.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class IndicatorView extends LinearLayout implements ViewPager.OnPageChangeListener{
    private ViewPager mViewPager;
    private int mSelectedID = R.drawable.shape_base_color_circle;
    private int mUnSelectedID = R.drawable.shape_black_color_circle;
    private int mDotSize = Util.dp(5);
    private int mMargin = Util.dp(5);
    private int mCount = 0;
    private List<ImageView> mDots = new ArrayList<>();
    public IndicatorView(Context context) {
        super(context);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewPager(ViewPager viewPager){
        clear();
        mViewPager = viewPager;
        mCount = mViewPager.getAdapter().getCount();
        for (int i=0;i<mCount;i++){
            ImageView img = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mDotSize,mDotSize);
            layoutParams.setMargins(mMargin,mMargin,mMargin,mMargin);
            img.setLayoutParams(layoutParams);

            if(i == 0){
                img.setImageResource(mSelectedID);
            }else{
                img.setImageResource(mUnSelectedID);
            }
            addView(img);
            mDots.add(img);
        }

        mViewPager.addOnPageChangeListener(this);
    }

    public void refreshView(int position){
        for (int i = 0;i < mCount;i++){
            ImageView dot = mDots.get(i);
            if (i == position){
                dot.setImageResource(mSelectedID);
            }else {
                dot.setImageResource(mUnSelectedID);
            }
        }
    }

    public void clear(){
        removeAllViews();
        mDots.clear();
        mCount = 0;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        refreshView(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
