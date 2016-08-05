package com.xcyo.live.utils;

/**
 * Created by caixiaoxiao on 31/5/16.
 */
public class ServerEvents {
    public final static String CALL_SERVER_METHOD_SYSTEM_START = "call_server_method_system_start";//第一个接口
    public final static String CALL_SERVER_METHOD_LOGIN = "call_server_method_login";//登录
    public final static String CALL_SERVER_METHOD_LIVE_HOT = "call_server_method_live_hot";  //热门直播列表
    public final static String CALL_SERVER_METHOD_LIVE_FOLLOW = "call_server_method_live_follow"; //关注列表
    public final static String CALL_SERVER_METHOD_LIVE_NEW = "call_server_method_live_new";  //最新直播列表
    public final static String CALL_SERVER_METHOD_USER_SEARCH = "call_server_method_user_search";  //用户搜索

    public final static String CALL_SERVER_METHOD_TOPIC_LIST = "call_server_method_topic_list"; //获取话题列表
    public final static String CALL_SERVER_METHOD_TOPIC_ROOM = "call_server_method_topic_room"; //获取某个话题的房间列表
    public final static String CALL_SERVER_METHOD_USER_START = "call_server_method_usr_start";//用户自己个人资料
    public final static String CALL_SERVER_METHOD_LOGIN_SMS = "call_server_method_usr_login_sms";//获取登录验证码
    public final static String CALL_SERVER_METHOD_LOGIN_MOBILE = "call_server_method_usr_login_mobile";//短信验证码登录
    public final static String CALL_SERVER_METHOD_LOGIN_LUNCHER_OTHER = "call_server_method_usr_login_luncher_other";//第三方登录

    public final static String CALL_SERVER_METHOD_USR_UPLOAD_NAME = "call_server_method_usr_upload_name";//更新用户昵称
    public final static String CALL_SERVER_METHOD_IMAGE_QN_TOKEN = "call_server_method_image_qn_token";//获取七牛的token
    public final static String CALL_SERVER_METHOD_USR_UPLOAD_ICON = "call_server_method_usr_upload_icon";//上传用户的头像
    public final static String CALL_SERVER_METHOD_OTHER_DETAIL_INFO = "call_server_method_other_detail_info";  //获取他人详细信息
    public final static String CALL_SERVER_METHOD_OTHER_BASE_INFO = "call_server_method_other_base_info";  //获取他人基本信息

    //房间相关
    public final static String CALL_SERVER_METHOD_ROOM_CREATE = "call_server_method_room_create";//创建房间
    public final static String CALL_SERVER_METHOD_ROOM_LIVE = "call_server_method_room_live";//进入房间
    public final static String CALL_SERVER_METHOD_ROOM_SEND_END = "call_server_method_room_send_dan";//发送弹幕

    //聊天
    public final static String RECENTCONTACT_LIST_UPDATE = "recentcontact_list_update";  //最近会话变更通知

    //资料卡
    public final static String CALL_SERVER_METHOD_FILE_FOLLOW = "call_server_method_file_follow";//关注

}
