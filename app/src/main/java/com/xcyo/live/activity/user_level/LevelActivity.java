package com.xcyo.live.activity.user_level;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/6.
 */
public class LevelActivity extends BaseActivity<LevelPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_user_level);
        img = (ImageView) findViewById(R.id.usr_level_img);
        currentIndex = (TextView) findViewById(R.id.usr_level_currentindex);
        progressBar = (ProgressBar) findViewById(R.id.usr_level_progressbar);
        discribe = (TextView) findViewById(R.id.usr_level_discribe);

        level_icon = (ImageView) findViewById(R.id.usr_level_icon);
        level_line = (ImageView) findViewById(R.id.usr_level_line);
        level_17 = (ImageView) findViewById(R.id.usr_level_17);
        level_54 = (ImageView) findViewById(R.id.usr_level_54);

        icon_layer = (RelativeLayout) findViewById(R.id.usr_level_icon_layer);
        line_layer = (RelativeLayout) findViewById(R.id.usr_level_line_layer);
        m17_layer = (RelativeLayout) findViewById(R.id.usr_level_17_layer);
        m54_layer = (RelativeLayout) findViewById(R.id.usr_level_54_layer);

        detail = findViewById(R.id.usr_level_right_detail);
    }

    private ImageView img;
    private TextView currentIndex;
    private ProgressBar progressBar;
    private TextView discribe;

    private ImageView level_icon;
    private ImageView level_line;
    private ImageView level_17;
    private ImageView level_54;

    private RelativeLayout icon_layer;
    private RelativeLayout line_layer;
    private RelativeLayout m17_layer;
    private RelativeLayout m54_layer;

    private View detail;

    @Override
    protected void initEvents() {
        addOnClickListener(detail, "rightDetail");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }
}
