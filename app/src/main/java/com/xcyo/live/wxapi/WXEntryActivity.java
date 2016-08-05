package com.xcyo.live.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xcyo.live.utils.LuncherUtils;
import com.xcyo.live.utils.WeChatUtils;

/**
 * Created by TDJ on 2016/6/18.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, null, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        android.util.Log.d("WXEntry", "errCode = " + baseResp.errCode + "Code = " + baseResp.errStr + "Type=" + baseResp.getType() + "transaction=" + baseResp.transaction);
        if(baseResp.errCode == BaseResp.ErrCode.ERR_OK){
            //处理登录成功
            Intent handIntent = new Intent();
            handIntent.setAction(WeChatUtils.LUNCHER_ACTION);
            handIntent.putExtra(LuncherUtils.LUNCHER_PARAMS_TAG, baseResp.errStr);
            LocalBroadcastManager.getInstance(this).sendBroadcast(handIntent);
            this.finish();
        }
    }
}
