package com.xcyo.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by TDJ on 2016/6/24.
 */
public class HeaderHoriZontalScrollBarListView  extends ListView {

    public HeaderHoriZontalScrollBarListView(Context context){
        this(context, null);
    }

    public HeaderHoriZontalScrollBarListView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public HeaderHoriZontalScrollBarListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float scaleX, scaleY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if(MotionEvent.ACTION_DOWN == action){
            scaleX = ev.getX();
            scaleY = ev.getY();
        }
        if(MotionEvent.ACTION_MOVE == action){

            double round = Math.abs(ev.getY() - scaleY) / Math.abs(ev.getX() -scaleX);
            if(round > Math.tan(Math.PI / 6)){
                requestDisallowInterceptTouchEvent(true);
                return false;
            }
            scaleX = ev.getX();
            scaleY = ev.getY();
        }
        return super.onTouchEvent(ev);
    }
}
