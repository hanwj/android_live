package com.xcyo.live.activity.user_profit;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/7.
 */
public class ProfitActivity extends BaseActivity<ProfitPresent>{
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_user_profit);

        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_finish = (TextView) findViewById(R.id.base_title_right);
        actionbar_finish.setText("领取记录"); actionbar_finish.setVisibility(View.VISIBLE);
        actionbar_title.setText("我的收益");

        youbi_count = (TextView) findViewById(R.id.usr_profit_youbi_count);
        red = (TextView) findViewById(R.id.usr_profit_red_money);

        exchange = (Button)findViewById(R.id.usr_profit_exchange);
        wx = (Button) findViewById(R.id.usr_profit_wx);
    }


    private TextView youbi_count;
    private TextView red;

    private Button exchange;
    private Button wx;

    private TextView actionbar_back, actionbar_title, actionbar_finish;

    @Override
    protected void initEvents() {
        addOnClickListener(exchange, "exchange");
        addOnClickListener(wx, "wx");
        addOnClickListener(actionbar_back, "finish");
        addOnClickListener(actionbar_finish, "goexchange");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }
}
