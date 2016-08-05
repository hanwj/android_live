package com.xcyo.live.dialog.live_share;

import android.view.View;

import com.xcyo.baselib.presenter.BaseDialogFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;

/**
 * Created by TDJ on 2016/6/14.
 */
public class LiveShareDialogPresent extends BaseDialogFragmentPresenter<LiveShareDialogFragment, LiveShareDialogRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String)data;
        if("wechat".equals(action)){
            goWechat();
        }else if("wechatGround".equals(action)){
            goWechatGround();
        }else if("qq".equals(action)){
            goQQ();
        }else if("qqGround".equals(action)){
            goQQGround();
        }else if("cancel".equals(action)){
            goCancel();
        }
    }

    private void goWechat(){

    }

    private void goWechatGround(){

    }

    private void goQQ(){

    }

    private void goQQGround(){

    }

    private void goCancel(){
        fragment().dismiss();
    }
}
