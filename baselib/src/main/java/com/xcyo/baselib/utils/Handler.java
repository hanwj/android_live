package com.xcyo.baselib.utils;

import java.lang.ref.WeakReference;

/**
 * Created by wanghongyu on 5/1/16.
 */

public class Handler<T> extends android.os.Handler{
    protected WeakReference<T> mBase;
    protected Handler(T ctx){
        setBase(ctx);
    }

    protected Handler(){
    }

    protected T getBase(){
        return mBase.get();
    }

    public void setBase(T base){
        mBase = new WeakReference<T>(base);
    }
}
