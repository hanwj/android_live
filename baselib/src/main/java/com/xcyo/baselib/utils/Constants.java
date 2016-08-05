package com.xcyo.baselib.utils;

/**
 * Created by wanghongyu on 30/12/15.
 */
public class Constants {
    public static final String SERVER_POST = "post";
    public static final String SERVER_UPLOAD = "upload";
    public static final String SERVER_DOWNLOAD = "download";
    public static final String SERVER_GET = "get";

    public static final String CALL_SERVER_METHOD_ERROR = "call_server_method_error";
    public static final int CALL_SERVER_METHOD_SUCC = 1;
    public static final int CALL_SERVER_METHOD_ERROR_SERVER = 0;
    public static final int CALL_SERVER_METHOD_ERROR_NETWORK = -1;
    public static final int CALL_SERVER_METHOD_ERROR_USERCANCEL = -2;
    public static final String CALL_SERVER_METHOD_ON_PROGRESS = "call_server_method_on_progress";

    public static final String DOWNLOAD_FILE_NAME_PARAMS_KEY = "download_file_name_params_key";
    public static final String DOWNLOAD_SAVE_PATH_PARAMS_KEY = "download_save_path_params_key";
    public static final String UPLOAD_FILE_PATH_PARAMS_KEY = "usefile";
    public static boolean LOG_OPEN = true;
}
