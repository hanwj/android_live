package com.xcyo.baselib.server.callback;

import com.xcyo.baselib.event.Event;
import com.xcyo.baselib.server.BaseServerCallback;
import com.xcyo.baselib.server.ServerBinder;
import com.xcyo.baselib.server.ServerBinderData;

import java.io.File;

/**
 * Created by wanghongyu on 4/1/16.
 */
public class DownloadCallback extends BaseServerCallback<File> {
    public DownloadCallback(ServerBinder binder, ServerBinderData data, Event event) {
        super(binder, data, event);
    }
}
