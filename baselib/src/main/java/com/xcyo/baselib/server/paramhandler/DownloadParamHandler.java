package com.xcyo.baselib.server.paramhandler;

import com.xcyo.baselib.utils.Constants;
import com.xutils.http.RequestParams;

/**
 * Created by wanghongyu on 30/12/15.
 */
public class DownloadParamHandler extends BaseServerParamHandler {
    private DownloadParamHandler(){}
    private DownloadParamHandler(String method, String... params){
    }

    public DownloadParamHandler(String fileUrl, String savePath){
        initArgs(null);
        addOnePairParams(Constants.DOWNLOAD_FILE_NAME_PARAMS_KEY, fileUrl);
        addOnePairParams(Constants.DOWNLOAD_SAVE_PATH_PARAMS_KEY, savePath);
    }

    @Override
    public void addOneParam(RequestParams params, String key, String value) {
        params.addQueryStringParameter(key, value);
    }

    @Override
    public RequestParams toRequestParams(String uri) {
        RequestParams params = new RequestParams(uri);
        params.setAutoResume(true);
        params.setAutoRename(false);
        params.setSaveFilePath(getValue(Constants.DOWNLOAD_SAVE_PATH_PARAMS_KEY));
        params.setCancelFast(true);
        return params;
    }
}
