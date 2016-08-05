package com.xcyo.live.activity.user_right;

import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/7.
 */
public class RightActivity extends BaseActivity<RightPresent>{
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_right);
        mIcon = (ImageView)findViewById(R.id.usr_level_right_icon);
        mLine = (ImageView)findViewById(R.id.usr_level_right_line);
        m17 = (ImageView)findViewById(R.id.usr_level_right_17);
        m54 = (ImageView)findViewById(R.id.usr_level_right_54);

        cIcon = (TextView)findViewById(R.id.usr_level_right_icon_content);
        cLine = (TextView)findViewById(R.id.usr_level_right_line_content);
        c17 = (TextView)findViewById(R.id.usr_level_right_17_content);
        c54 = (TextView)findViewById(R.id.usr_level_right_54_content);

        back = (ImageView)findViewById(R.id.usr_level_right_back);
        finish = (TextView)findViewById(R.id.usr_level_right_finish);
    }

    private ImageView mIcon, mLine, m17, m54;

    private TextView cIcon, cLine, c17, c54;

    private TextView finish;
    private ImageView back;

    @Override
    protected void initEvents() {
        addOnClickListener(finish, "finish");
        addOnClickListener(back, "finish");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }
}
