package com.xcyo.live.activity.message;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.utils.ToastUtil;
import com.xcyo.live.activity.user_info.UserInfoActivity;

/**
 * Created by caixiaoxiao on 8/6/16.
 */
public class MessageActPresenter extends BaseActivityPresenter<MessageActivity,MessageActRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("back".equals(str)){
                mActivity.finish();
            }else if ("userInfo".equals(str)){
                ToastUtil.toastDebug(mActivity,"信息");
                Intent intent = new Intent(mActivity, UserInfoActivity.class);
                intent.putExtra("uid",mActivity.getToUid());
                intent.putExtra("alias",mActivity.getToAlias());
                mActivity.startActivity(intent);
            }
        }
    }
}
