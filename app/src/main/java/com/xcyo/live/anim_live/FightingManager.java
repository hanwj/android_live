package com.xcyo.live.anim_live;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.RelativeLayout;

import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/7/7.
 */
public class FightingManager {

    public static class FightRecord{

    }

    private static final int TIME_PARENT_TRANSLATION = 6000;
    private static Window mWindow;
    private static RelativeLayout mContainer;
    private int mWidth = 0;
    private int mPreRule = -1;

    private FightingManager() {
        mWidth = mWindow.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    private static final class FightInstance {
        private static final FightingManager t = new FightingManager();
    }

    public static FightingManager get(Window window, int container) {
        mWindow = window;
        mContainer = (RelativeLayout) mWindow.findViewById(container);
        if (mContainer == null)
            return null;
        return FightInstance.t;
    }

    public void recyle() {
        mWindow = null;
        mContainer = null;
    }

    public void put(){
        View childView = mWindow.getLayoutInflater().inflate(R.layout.item_fighting, null);
        childView.getViewTreeObserver().addOnGlobalLayoutListener(getLayoutListener(childView));
        this.mContainer.addView(childView, getLayoutParams());
    }

    private RelativeLayout.LayoutParams getLayoutParams(){
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT
                , RelativeLayout.LayoutParams.WRAP_CONTENT);
        if(mPreRule == RelativeLayout.ALIGN_PARENT_TOP){
            mPreRule = RelativeLayout.ALIGN_PARENT_BOTTOM;
        }else{
            mPreRule = RelativeLayout.ALIGN_PARENT_TOP;
        }
        rp.addRule(mPreRule);
        return rp;
    }

    private ViewTreeObserver.OnGlobalLayoutListener getLayoutListener(@NonNull final View convertView){
        return new ViewTreeObserver.OnGlobalLayoutListener(){

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                convertView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Animator anim = ObjectAnimator.ofFloat(convertView, "translationX", new float[]{convertView.getMeasuredWidth()+mWidth, -convertView.getMeasuredWidth()});
                anim.setDuration(TIME_PARENT_TRANSLATION);
                anim.addListener(getAnimListener(convertView));
                anim.start();
            }
        };
    }

    private Animator.AnimatorListener getAnimListener(@NonNull final View view){
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                ViewGroup group = (ViewGroup)view.getParent();
                if(group != null){
                    group.removeView(view);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        };
    }
}
