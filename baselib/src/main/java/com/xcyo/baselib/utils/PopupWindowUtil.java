package com.xcyo.baselib.utils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.xcyo.baselib.R;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghongyu on 7/1/16.
 *
 * 用法
     PopupWindowUtil.PopupWindowConfig cfg = new PopupWindowUtil.PopupWindowConfig(PopupWindowUtil.PopupWindowConfig.MaskType.VIEW, mAttentionSingerList);
     cfg.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
     PopupWindowUtil.showPopupWindow(R.layout.common_loading_status_pop, cfg, new PopupWindowUtil.PopupWindowCallback() {
        @Override
        public void initView(View v) {
            ImageView imageView = (ImageView)v.findViewById(R.id.common_loading_status_image);
            imageView.setImageResource(R.mipmap.loading_error);
        }
    });
 */
public class PopupWindowUtil {
    private final static String TAG = "PopupWindowUtil";
    private static HashMap<View, PopupWindowConfig> mShowingPopupWindow;
    public static class PopupWindowConfig{
        public enum MaskType{
            SCREEN,//全屏模式
            VIEW,//铺满target view，大小位置同targetview相同
            RECT,//铺满rect
            NONE// wrap content
        }

        public enum ClickMode{
            ALWAYS_SHOW,//不阻挡点击，
            // 也不会因点击而消失，相当于浮层
            CLOSE_TAP_ROUND, //点击周围关闭，点击周围区域，popwindow消失
            NON_CLOSE_TAP_ROUND, //点击周围不会响应，阻挡点击事件，而且点击popwindow周围也不会响应
        }

        public PopupWindowConfig(MaskType mask, View t){
            target = t;
            maskType = mask;
        }

        public View target;//popupwindow所必须的参照View
        public Rect rect;//popupwindow显示在此rect内
        public MaskType maskType = MaskType.VIEW;//popupwindow的大小和位置用什么参照物来确定
        public ClickMode clickMode = ClickMode.ALWAYS_SHOW;//点击页面不同的位置，popup如何响应
        public boolean cancelable = false;//是否可以按back按键退出
        public int animStyle = 0;//动画
        public int gravity = Gravity.CENTER;
        public int windowGravity = Gravity.CENTER;
        private PopupWindow popupWindow;//保存当前popupwindow
        public int backgroundDrableId = -1;//背景图resourceid
        public boolean isAddTargetToThreadCheck = false;
        public boolean dismissWhenTargetInvalid = false;//当targetview不再显示／被移除等的时候，是否关闭popup
        public Context context;
        public boolean isValid(){
            return target != null &&
                    ((maskType != MaskType.RECT) || (rect.height() > 0 && rect.width() > 0));

        }
    }

    public interface PopupWindowCallback{
        void initView(View v);
    }

    private static HashMap<View, PopupWindowConfig> getShowingPopupWindowMap() {
        if (mShowingPopupWindow == null) {
            mShowingPopupWindow = new HashMap<>();
        }
        return mShowingPopupWindow;
    }

    private static PopupWindowConfig getShowingPopupWindow(View target){
        if (mShowingPopupWindow == null){
            return null;
        }
        return mShowingPopupWindow.get(target);
    }

    public static void dismissPopupWindow(View target){
        PopupWindowConfig cfg = getShowingPopupWindow(target);
        if(cfg != null){
            cfg.popupWindow.dismiss();
        }
        if(mShowingPopupWindow != null) {
            mShowingPopupWindow.remove(target);
        }

    }

    public static boolean isShowingPopupWindow(View target){
        return getShowingPopupWindow(target) != null;
    }

    public static void showPopupWindow(int rid, final PopupWindowConfig cfg, PopupWindowCallback cb){
        if(cfg == null || !cfg.isValid()){
            LogUtil.i(TAG, "[ERROR]showPopWindow cfg is not valid!");
            return;
        }
        Context ctx = cfg.context == null ? Util.context: cfg.context;
        final View popView = LayoutInflater.from(ctx).inflate(rid, null, false);
        if(popView == null){
            LogUtil.i(TAG, "[ERROR]showPopWindow popView is null!");
            return;
        }

        if (isShowingPopupWindow(popView)){
            LogUtil.i(TAG, "[ERROR]showPopWindow popView is showing!");
            return;
        }

        if (null != cb){
            cb.initView(popView);
        }
        ViewGroup viewGroup = (ViewGroup)popView;
        if (viewGroup == null){
            LogUtil.i(TAG, "[ERROR]showPopWindow rid root is not ViewGroup!");
            return;
        }
        viewGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if(viewGroup instanceof LinearLayout){
            LinearLayout linearLayout = (LinearLayout)viewGroup;
            linearLayout.setGravity(cfg.gravity);
        }else if(viewGroup instanceof RelativeLayout){
            RelativeLayout relativeLayout = (RelativeLayout) viewGroup;
            relativeLayout.setGravity(cfg.gravity);
        }else{
            LogUtil.i(TAG, "[ERROR]showPopWindow rid root is not LinearLayout/RelativeLayout!");
            return;
        }

        int pos[] = new int[2];
        int x = 0, y = 0;
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        switch (cfg.maskType){
            case VIEW:
                cfg.target.getLocationOnScreen(pos);
                final int viewx = pos[0];
                final int viewy = pos[1];
                //final ViewTreeObserver observer = cfg.target.getViewTreeObserver();
//                observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                    @Override
//                    public boolean onPreDraw() {
//                        if (!isShowingPopupWindow(cfg.target)) {
//                        }else{
//                            observer.removeOnPreDrawListener(this);
//                        }
//                        return true;
//                    }
//                });
                showPopupInner(popView, cfg, viewx, viewy, cfg.target.getMeasuredWidth(), cfg.target.getMeasuredHeight());
                break;
            case RECT:
                x = cfg.rect.left;
                y = cfg.rect.top;
                width = cfg.rect.width();
                height = cfg.rect.height();
                break;
            case NONE:
                width = ViewGroup.LayoutParams.WRAP_CONTENT;
                height = ViewGroup.LayoutParams.WRAP_CONTENT;
                break;
            case SCREEN:
                width = Util.getScreenWidth();
                height = Util.getScreenHeight();
                break;
        }

        if(cfg.maskType != PopupWindowConfig.MaskType.VIEW){
            showPopupInner(popView, cfg, x, y, width, height);
        }
    }

    private static boolean isValidTarget(View target){
        return target != null && target.isEnabled() && target.isShown();
    }

    private static Handler sDismissHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            View target = (View) msg.obj;
            PopupWindowConfig cfg = getShowingPopupWindow(target);
            PopupWindow pop = cfg.popupWindow;
            if (pop == null){
                dismissPopupWindow(target);
                return;
            }
            switch (msg.what){
                case 0:
                    if (cfg.dismissWhenTargetInvalid){
                        dismissPopupWindow(target);
                    }else {
                        ((View)pop.getContentView().getParent()).setVisibility(View.GONE);
                    }
                    break;
                case 1:
                    ((View)pop.getContentView().getParent()).setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private static void showPopupInner(View popView, final PopupWindowConfig cfg, int x, int y, int w, int h){
        if (isValidTarget(cfg.target)) {
            PopupWindow popupWindow = new PopupWindow(popView, w, h, true);
            cfg.popupWindow = popupWindow;

            popupWindow.setTouchable(cfg.clickMode != PopupWindowConfig.ClickMode.ALWAYS_SHOW);//不可
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return cfg.clickMode == PopupWindowConfig.ClickMode.NON_CLOSE_TAP_ROUND;
                }
            });
            if(cfg.backgroundDrableId < 0){
                cfg.backgroundDrableId = R.drawable.popupwindow_background_color;
            }
            Context ctx = cfg.context == null? Util.context : cfg.context;
            popupWindow.setBackgroundDrawable(ctx.getResources().getDrawable(cfg.backgroundDrableId));
            popupWindow.setAnimationStyle(cfg.animStyle);
            popupWindow.setFocusable(cfg.cancelable);

            if (cfg.maskType == PopupWindowConfig.MaskType.VIEW) {
                popupWindow.showAsDropDown(cfg.target, 0, -h);
            } else {
                popupWindow.showAtLocation(cfg.target, cfg.windowGravity, x, y);
            }

            getShowingPopupWindowMap().put(cfg.target, cfg);

            if (cfg.isAddTargetToThreadCheck) {
                if (sExecutorRunnable == null) {
                    sExecutorRunnable = new ExecutorRunnable();
                }
                sExecutorRunnable.addTarget(cfg.target);
            }
            startCheckTargetThread();
        }
    }

    private static void startCheckTargetThread(){
        if (executorService == null || executorService.isShutdown()){
            executorService = Executors.newSingleThreadScheduledExecutor();
        }else{
            return;
        }
        if (sExecutorRunnable != null){
            executorService.scheduleAtFixedRate(sExecutorRunnable, 0, EXECUTOR_CHECK_INTERVAL, TimeUnit.MILLISECONDS);
        }
    }

    private static void finishCheckTargetThread(){
        if (executorService != null && !executorService.isShutdown()){
            executorService.shutdown();
        }
    }

    private static ExecutorRunnable sExecutorRunnable = null;
    private static class ExecutorRunnable implements Runnable{
        private LinkedBlockingQueue<View> blockingTargets;
        public void addTarget(View target){
            if (blockingTargets == null){
                blockingTargets = new LinkedBlockingQueue<View>();
            }
            blockingTargets.add(target);
        }
        @Override
        public void run() {
            if (blockingTargets.size() == 0){
                finishCheckTargetThread();
                return;
            }
            for (View target : blockingTargets) {
                Message msg = Message.obtain();
                msg.obj = target;
                if (isValidTarget(target)) {
                    msg.what = 1;
                } else {
                    msg.what = 0;
                }
                sDismissHandler.sendMessage(msg);
            }
        }
    }

    private static ScheduledExecutorService executorService = null;//检测targetview 是否valid的线程
    private static final int EXECUTOR_CHECK_INTERVAL = 50;
}
