package com.xcyo.baselib.server;

/**
 * Created by wanghongyu on 30/12/15.
 */
public abstract class BaseServerRegister {
    protected abstract void doRegister(ServerBinder binder);
    protected abstract String getHostUrl();
}
