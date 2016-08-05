package com.xcyo.live.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jazz.direct.libs.DensityUtils;
import com.xcyo.baselib.utils.Util;

import java.util.List;
import java.util.Vector;

/**
 * Created by TDJ on 2016/6/15.
 */
public class LiveGiftPagerAdapter extends PagerAdapter {

    private final Vector<View> vector = new Vector<>(10);
    private final Vector<View> indicators = new Vector<>();
    private ViewPager mPager;

    public LiveGiftPagerAdapter(ViewPager pager){
        this.mPager = pager;
    }

    public void setView(List<View> data){
        if(data == null || data.size() == 0){
            return ;
        }
        this.vector.clear();
        this.vector.addAll(data);
        this.notifyDataSetChanged();
    }

    public View getCurrentItemPostion(){
        int position = this.mPager.getCurrentItem();
        if(position >= 0 && position < this.vector.size()){
            return this.vector.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.vector.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View childView = this.vector.get(position);
        container.addView(childView);
        return childView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View childView = this.vector.get(position);
        container.removeView(childView);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(mPager.getVisibility() == View.VISIBLE){
            this.mPager.getViewTreeObserver().addOnGlobalLayoutListener(getOnGlabalLayoutLinstener());
            this.mPager.addOnPageChangeListener(getOnPageChangeListener());
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener getOnGlabalLayoutLinstener(){
        return new ViewTreeObserver.OnGlobalLayoutListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams vgp = mPager.getLayoutParams();
                if(vgp != null && vgp instanceof RelativeLayout.LayoutParams){
                    LinearLayout indicator = new LinearLayout(mPager.getContext());
                    RelativeLayout.LayoutParams vg = new RelativeLayout.LayoutParams(vgp.width, Util.dp2px(18));
                    vg.topMargin = ((ViewGroup.MarginLayoutParams) vgp).topMargin + mPager.getMeasuredHeight() - Util.dp2px(24);
                    indicator.setLayoutParams(vg);
                    indicator.setGravity(Gravity.CENTER);
                    for(int i = 0; i < getCount(); i++){
                        View v = getIndicatorView();
                        indicators.add(v);
                        indicator.addView(v);
                    }
                    if(indicators.size() > 0){
                        setSelectorItem(0);
                    }
                    ((ViewGroup)mPager.getParent()).addView(indicator);
                }
                mPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };
    }

    private View getIndicatorView(){
        View v = new View(mPager.getContext());
        LinearLayout.LayoutParams ii =new LinearLayout.LayoutParams(Util.dp2px(8), Util.dp2px(8));
        ii.leftMargin = ii.width / 2;
        v.setLayoutParams(ii);
        v.setBackgroundDrawable(createButtonDrawable(mPager.getContext()));
        return v;
    }

    private static final Drawable createButtonDrawable(Context context){
        StateListDrawable listdb = new StateListDrawable();

        GradientDrawable bd = new GradientDrawable();
        bd.setGradientType(GradientDrawable.RECTANGLE);
        bd.setColor(0xD3D3D3);
        bd.setCornerRadius(DensityUtils.dp2px(context, 45f));
        bd.setStroke(DensityUtils.dp2px(context, 0.5f), Color.BLACK);

        GradientDrawable bds = new GradientDrawable();
        bds.setGradientType(GradientDrawable.RECTANGLE);
        bds.setColor(Color.WHITE);
        bds.setCornerRadius(DensityUtils.dp2px(context, 45f));

        listdb.addState(new int[]{android.R.attr.state_selected}, bds);
        listdb.addState(new int[]{}, bd);
        return listdb;
    }

    private ViewPager.OnPageChangeListener getOnPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageSelected(int position) {
                setSelectorItem(position);
            }
        };
    }

    private void setSelectorItem(final int position){
        View indicator = indicators.get(position);
        for(View vw : indicators){
            if(vw != null && indicator != null &&vw == indicator){
                vw.setSelected(true);
            }else{
                vw.setSelected(false);
            }
        }
    }

}
