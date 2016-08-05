package com.xcyo.live.dialog.live_share;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseDialogFragment;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/6/14.
 */
public class LiveShareDialogFragment extends BaseDialogFragment<LiveShareDialogPresent> {
    @Override
    protected void initArgs() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View convertView = inflater.inflate(R.layout.dialog_liveshare, null);
        wechat = (LinearLayout) convertView.findViewById(R.id.live_share_wechat);
        wechatGround = (LinearLayout) convertView.findViewById(R.id.live_share_wechatground);
        qq = (LinearLayout) convertView.findViewById(R.id.live_share_qq);
        qqGround = (LinearLayout) convertView.findViewById(R.id.live_share_qqground);
        cancel = (TextView) convertView.findViewById(R.id.live_share_cancel);
        return convertView;
    }

    private LinearLayout wechat, wechatGround, qq, qqGround;
    private TextView cancel;

    @Override
    protected void initEvents() {
        addOnClickListener(wechat, "wechat");
        addOnClickListener(wechatGround, "wechatGround");
        addOnClickListener(qq, "qq");
        addOnClickListener(qqGround, "qqGround");
        addOnClickListener(cancel, "cancel");
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams ll = getDialog().getWindow().getAttributes();
        ll.alpha = 1.0f;
        ll.dimAmount = 0.1f;
        ll.gravity = Gravity.BOTTOM;
        ll.width = getResources().getDisplayMetrics().widthPixels;
        getDialog().getWindow().setAttributes(ll);
        setCancelable(false);
        super.onResume();
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }
}
