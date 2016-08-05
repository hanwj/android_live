package com.xcyo.baselib.server.paramhandler;

import com.xcyo.baselib.utils.Constants;
import com.xutils.http.RequestParams;

import java.io.File;

/**
 * Created by wanghongyu on 30/12/15.
 */
public class UploadParamHandler extends BaseServerParamHandler {

    private String contentType = "text/plain";

    protected UploadParamHandler(){}

    private UploadParamHandler(String method, String... params){
    }

    public UploadParamHandler(String upfilePath, String contentType){
//        initArgs(null);
//        this.addOnePairParams(Constants.UPLOAD_FILE_PATH_PARAMS_KEY, upfilePath);
        super(Constants.UPLOAD_FILE_PATH_PARAMS_KEY, upfilePath);
        if(contentType != null)
            this.contentType = contentType;
    }

    @Override
    public void addOneParam(RequestParams params, String key, String value) {
        if(value != null){
            final File file = new File(value);
            if(file.exists())
                params.addBodyParameter(key, file, contentType);
        }
    }

    @Override
    public RequestParams toRequestParams(String url) {
        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        params.addBodyParameter(Constants.UPLOAD_FILE_PATH_PARAMS_KEY, getValue(Constants.UPLOAD_FILE_PATH_PARAMS_KEY));
        return params;
    }
}
