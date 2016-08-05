package com.xcyo.live.anim_live;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcyo.live.R;

import java.util.LinkedList;

/**
 * Created by TDJ on 2016/7/7.
 */
public class UioManager {

    private UioManager() {
        mWidth = mWindow.getContext().getResources().getDisplayMetrics().widthPixels;
        prepList = new LinkedList<>();
        mHandler = new Handler();
    }
    private static Window mWindow;
    private static RelativeLayout mContainer;
    private LinkedList<CharSequence> prepList = null;
    private static final int TIME_PARENT_TRANSLATION = 600;
    private int mWidth = 0;
    private CharSequence current = null;
    private Handler mHandler;

    private static final class UioInstance {
        private static final UioManager t = new UioManager();
    }

    public static UioManager get(Window window, int container) {
        mWindow = window;
        mContainer = (RelativeLayout) mWindow.findViewById(container);
        if (mContainer == null)
            return null;
        return UioInstance.t;
    }

    public void recyle() {
        mWindow = null;
        mContainer = null;
        current = null;
        prepList.clear();
    }

    public synchronized void put(CharSequence ch){
        this.prepList.add(ch);
        if(current == null){
            inScreen(prepList.poll());
        }
    }

    private void inScreen(CharSequence seq){
        if(!TextUtils.isEmpty(seq)){
            current = seq;
            View convertView = mWindow.getLayoutInflater().inflate(R.layout.item_uio, null);
            convertView.getViewTreeObserver().addOnGlobalLayoutListener(getLayoutListener(convertView));
            this.mContainer.addView(convertView);
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener getLayoutListener(@NonNull final View convertView){
        return new ViewTreeObserver.OnGlobalLayoutListener(){

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                convertView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Animator anim = ObjectAnimator.ofFloat(convertView, "translationX", new float[]{-convertView.getMeasuredWidth(), 0});
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
                if(view != null){
                    TextView tView = (TextView)view.findViewById(R.id.uio_who);
                    tView.setText(current+"");
                    mHandler.postDelayed(getHandlerRunnable(view), TIME_PARENT_TRANSLATION);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        };
    }

    private Runnable getHandlerRunnable(@NonNull final View convertView){
        return new Runnable() {
            @Override
            public void run() {
                ViewGroup group = (ViewGroup)convertView.getParent();
                if(group != null){
                    group.removeView(convertView);
                }
                current = null;
                if(!prepList.isEmpty()){
                    inScreen(prepList.poll());
                }
            }
        };
    }
}
