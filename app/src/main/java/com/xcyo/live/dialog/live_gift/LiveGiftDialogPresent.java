package com.xcyo.live.dialog.live_gift;

import android.view.View;

import com.xcyo.baselib.presenter.BaseDialogFragmentPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.utils.LogUtil;

/**
 * Created by TDJ on 2016/6/15.
 */
public class LiveGiftDialogPresent extends BaseDialogFragmentPresenter<LiveGiftDialogFragment, LiveGiftDialogRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String)data;
        if("send".equals(action)){
             String item = (String)fragment().getItemViewStream();
            if(item != null){
                fragment().resetTimer();
                fragment().recipStart();
            }
            LogUtil.e("TAG", item+"");
        }else if("dbhit".equals(action)){
            fragment().resetTimer();
        }
    }

    public interface OnGiftCallBack{
        public void gift();
    }
}
