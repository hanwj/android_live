package com.xcyo.live.activity.user_profit;

import android.content.Intent;
import android.view.View;

import com.xcyo.baselib.presenter.BaseActivityPresenter;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.live.activity.user_exchange.ExchangeActivity;
import com.xcyo.live.activity.user_wxbind.BindWXActivity;

/**
 * Created by TDJ on 2016/6/7.
 */
public class ProfitPresent extends BaseActivityPresenter<ProfitActivity, ProfitRecord> {
    @Override
    protected void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    protected void onClick(View v, Object data) {
        String action = (String) data;
        if("finish".equals(action)){
            getActivity().finish();
        }else if("wx".equals(action)){
            bindWx();
        }else if("exchange".equals(action)){

        }else if("goexchange".equals(action)){
            goExchange();
        }
    }

    private void goExchange(){
        getActivity().startActivity(new Intent(getActivity(), ExchangeActivity.class));
    }

    private void bindWx(){
        getActivity().startActivity(new Intent(getActivity(), BindWXActivity.class));
    }

    private void doExchange(){

    }
}
