package com.xcyo.live.activity.live_end;

import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by caixiaoxiao on 6/6/16.
 */
public class LiveEndActivity extends BaseActivity<LiveEndActPresenter> {
    private Button mCloseBtn;
    private TextView mNumText,mCoinText;
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_live_end);
        mCloseBtn = (Button)findViewById(R.id.live_end_finish);
        mNumText = (TextView)findViewById(R.id.live_end_num);
        mCoinText = (TextView)findViewById(R.id.live_end_gain);

        String numDes = "<font color='#b98eff'>123</font><font color='white'>人看过</font>";
        mNumText.setText(Html.fromHtml(numDes));

        String coinDes = "<font color='white'>收获</font><font color='#ff7895'>3000</font><font color='white'>币</font>";
        mCoinText.setText(Html.fromHtml(coinDes));
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mCloseBtn,"back");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }
}
