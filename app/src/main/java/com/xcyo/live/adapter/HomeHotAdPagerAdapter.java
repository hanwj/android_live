package com.xcyo.live.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xcyo.live.R;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class HomeHotAdPagerAdapter extends PagerAdapter {
    private View[] mViews;
    private int mCount = 3;
    private Context mCtx;
    public HomeHotAdPagerAdapter(Context ctx){
        mCtx = ctx;
        mViews = new View[mCount];
        for (int i = 0;i<mCount;i++){
            ImageView img = new ImageView(mCtx);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(layoutParams);
            img.setImageResource(R.mipmap.app_start_pic);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mViews[i] = img;
        }
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = mViews[position];
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews[position]);
    }
}
