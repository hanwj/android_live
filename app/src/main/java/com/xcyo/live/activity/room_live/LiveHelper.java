package com.xcyo.live.activity.room_live;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jazz.direct.libs.DirectUtils;
import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;

/**
 * Created by TDJ on 2016/7/1.
 */
public class LiveHelper {

    private Activity mActivity;
    private String mDecoder = DirectUtils.CODE_HD;
    private boolean isOpenFight = false;

    public LiveHelper(Context context){
        this.mActivity = (Activity)context;
        mDecoder = mActivity.getIntent().getStringExtra(DirectUtils.direct_code);
        //DirectUtils.CODE_HD.equals(getMainIntent().getStringExtra(DirectUtils.direct_code)
    }

    public void showUpPop(View target){
        View convertView = mActivity.getLayoutInflater().inflate(R.layout.live_pop, null);
        final View share = convertView.findViewById(R.id.pop_share);
        final View flash = convertView.findViewById(R.id.pop_flash);
        final View swh = convertView.findViewById(R.id.pop_switch);
        final View meiyan = convertView.findViewById(R.id.pop_meiyan);
        ((TextView)convertView.findViewById(R.id.flash_content)).setText(isOpenFight?"关闪光":"开闪光");

        if(DirectUtils.CODE_RD.equals(mDecoder)){
            meiyan.setVisibility(View.GONE);
        }

        PopupWindow window = new PopupWindow(mActivity);
        window.setWidth(Util.dp2px(148));
        window.setHeight(Util.dp2px(238));
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setContentView(convertView);
        window.setOnDismissListener(getViewDismissListener(target));
        window.showAsDropDown(target, (target.getWidth() - window.getWidth()) / 2, -window.getHeight() - target.getHeight());
        target.setSelected(true);

        share.setOnClickListener(getViewClickListener(window));
        flash.setOnClickListener(getViewClickListener(window));
        swh.setOnClickListener(getViewClickListener(window));
        meiyan.setOnClickListener(getViewClickListener(window));
    }

    private View.OnClickListener getViewClickListener(@NonNull final PopupWindow window){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resId = v.getId();
                switch (resId){
                    case R.id.pop_share:
                        break;
                    case R.id.pop_flash:
                        openFight();
                        ((TextView)v.findViewById(R.id.flash_content)).setText(isOpenFight ? "关闪光" : "开闪光");
                        break;
                    case R.id.pop_switch:
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(DirectUtils.Action.ACTION_SWITCH_CAMERA));
                        break;
                    case R.id.pop_meiyan:
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(DirectUtils.Action.ACTION_FILTER));
                        break;
                }
                window.dismiss();
            }
        };
    }

    protected  boolean getFightState(){
        return isOpenFight;
    }

    protected void openFight(){
        Intent intent = new Intent(DirectUtils.Action.ACTION_OPENFIGHT);
        intent.putExtra("openfight", isOpenFight = (isOpenFight ? false : true));
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
    }

    private PopupWindow.OnDismissListener getViewDismissListener(final View target){
        return new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                target.setSelected(false);
            }
        };
    }

    public void destoryResource(){
        this.mActivity = null;
        if(isOpenFight){
            openFight();
        }
    }
}
