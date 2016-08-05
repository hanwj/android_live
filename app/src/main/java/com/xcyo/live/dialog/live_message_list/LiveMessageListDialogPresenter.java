package com.xcyo.live.dialog.live_message_list;

import android.view.View;

import com.xcyo.baselib.presenter.BaseDialogFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by caixiaoxiao on 27/6/16.
 */
public class LiveMessageListDialogPresenter extends BaseDialogFragmentPresenter<LiveMessageListDialogFragment,LiveMessageListDialogRecord>{
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        if (data instanceof String){
            String str = (String)data;
            if ("back".equals(str)){
                mFragment.dismiss();
            }
        }
    }
}
