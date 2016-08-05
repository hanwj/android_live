package com.xcyo.baselib.event;

/**
 * Created by wanghongyu on 30/12/15.
 */
public abstract class EventMapper {
    private static int sCounter = 0;
    private int mUniqId;
    protected EventMapper(){
        mUniqId = sCounter++;
    }

    protected void onExit(){
        Event.getInstance().unmapEventsWithTarget(this);
    }

    public int getUniqId(){
        return mUniqId;
    }
}
