package com.xcyo.baselib.server.callback;

import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.server.BaseServerCallback;
import com.xcyo.baselib.server.ServerBinder;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by wanghongyu on 4/1/16.
 */
public class CommonCallback extends BaseServerCallback<String>{
    public CommonCallback(ServerBinder binder, ServerBinderData data, Event event) {
        super(binder, data, event);
    }
}
