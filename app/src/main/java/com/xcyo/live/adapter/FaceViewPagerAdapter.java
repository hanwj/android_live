package com.xcyo.live.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.xcyo.baselib.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caixiaoxiao on 12/6/16.
 */
public class FaceViewPagerAdapter extends PagerAdapter {
    private int maxNumPerPager = 20; //每个页面最多20个表情，最后一个是删除键
    private Context mCtx;
    private List<String> mListData;
    private GridView[] mGridViews;
    private int mCount;
    public FaceViewPagerAdapter(Context ctx,List<String> listData){
        this.mCtx = ctx;
        this.mListData = listData;

        mCount = (int)Math.ceil(mListData.size()/maxNumPerPager);
        mGridViews = new GridView[mCount];
        for (int i = 0;i < mCount; i++){
            GridView gridView = new GridView(mCtx);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            gridView.setLayoutParams(layoutParams);
            gridView.setPadding(0, Util.dp(20),0,0);
            gridView.setVerticalSpacing(Util.dp(10));
            gridView.setGravity(Gravity.CENTER);
            gridView.setNumColumns(7);
            int num = 0;
            if (mListData.size() - maxNumPerPager * (i + 1) > 0){
                num = maxNumPerPager;
            }else {
                num = mListData.size() - maxNumPerPager * i;
            }
            List<String> subData = new ArrayList<>(mListData.subList(maxNumPerPager*i,maxNumPerPager*i+num));
            subData.add("delete");
            gridView.setAdapter(new FaceGridViewAdapter(mCtx, subData));
            mGridViews[i] = gridView;
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
        View v = mGridViews[position];
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mGridViews[position]);
    }
}
