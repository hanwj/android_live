package com.xcyo.baselib.server.paramhandler;

import com.xutils.http.RequestParams;

/**
 * Created by wanghongyu on 30/12/15.
 */
public class GetParamHandler extends BaseServerParamHandler {
    private GetParamHandler(){}

    public GetParamHandler(String ...params){
        super(params);
    }
    @Override
    public void addOneParam(RequestParams params, String key, String value){
        params.addQueryStringParameter(key, value);
    }
}
