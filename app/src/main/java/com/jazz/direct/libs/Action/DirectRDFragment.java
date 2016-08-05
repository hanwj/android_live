package com.jazz.direct.libs.Action;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jazz.direct.libs.DensityUtils;
import com.jazz.direct.libs.DirectUtils;
import com.jazz.direct.libs.view.LiveSurfaceView;
import com.jazz.direct.libs.ViewUtils;
import com.netease.LSMediaCapture.lsMediaCapture;
import com.xcyo.live.R;


/**
 * Created by TDJ on 2016/5/31.
 */
public class DirectRDFragment extends DirectMainFragment {

    private LiveSurfaceView surfaceView;

    @Override
    protected View getDirectSurfaceView() {
        return surfaceView = surfaceView != null ? surfaceView : new LiveSurfaceView(activity);
    }

    @Override
    protected void configPreViewSize(int mVideoPreViewWidth, int mVideoPreViewHeight) {
        surfaceView.setPreviewSize(mVideoPreViewWidth, mVideoPreViewHeight);
    }

    @Override
    protected void startVideoPreView(lsMediaCapture mLSMediaCapture, final lsMediaCapture.LSLiveStreamingParaCtx mLSLiveStreamingParaCtx) {
        mLSMediaCapture.startVideoPreview(surfaceView, 0);
    }

    @Override
    protected View configControllerView() {
        int customContoller = getMainIntent().getIntExtra(DirectUtils.direct_controller, -1);
        if(customContoller > 0){
            return LayoutInflater.from(this.activity).inflate(customContoller, null, true);
        }

        LinearLayout iir = ViewUtils.createControllerLinear(activity);

        Button switchButton = ViewUtils.createSwitchButton(activity);
        switchButton.setId(R.id.direct_controller_switch_av);
        switchButton.setText("开始");

        Button stopButton = ViewUtils.createSwitchButton(activity);
        stopButton.setId(R.id.direct_controller_stop_av);
        stopButton.setText("停止");

        Button mutiButton = ViewUtils.createSwitchButton(activity);
        mutiButton.setId(R.id.direct_controller_mute_audio);
        mutiButton.setText("静音");

        Button cameraButton = ViewUtils.createSwitchButton(activity);
        cameraButton.setId(R.id.direct_controller_switch_camera);
        cameraButton.setText("切换");

        Button fpsButton = ViewUtils.createSwitchButton(activity);
        fpsButton.setId(R.id.direct_controller_switch_bitrate);
        fpsButton.setText("fps");

        LinearLayout.LayoutParams vs = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        vs.setMargins(DensityUtils.dp2px(activity, 4), DensityUtils.dp2px(activity, 2), DensityUtils.dp2px(activity, 4), DensityUtils.dp2px(activity, 2));
        switchButton.setLayoutParams(vs);

        iir.addView(switchButton, vs);
        iir.addView(stopButton, vs);
        iir.addView(mutiButton, vs);
        iir.addView(cameraButton, vs);
        iir.addView(fpsButton, vs);
        return iir;
    }

    @Override
    protected void addClickEvent() {
        dispatchClickEvent(mFrame.findViewById(R.id.direct_controller_switch_av));
        dispatchClickEvent(mFrame.findViewById(R.id.direct_controller_stop_av));
        dispatchClickEvent(mFrame.findViewById(R.id.direct_controller_mute_audio));
        dispatchClickEvent(mFrame.findViewById(R.id.direct_controller_switch_camera));
        dispatchClickEvent(mFrame.findViewById(R.id.direct_controller_switch_bitrate));
        if(mFrame.findViewById(R.id.direct_controller_switch_bitrate) != null){
            mFrame.findViewById(R.id.direct_controller_switch_bitrate).setVisibility(View.GONE);
        }
    }


    private void dispatchClickEvent(final View view){
        if(view != null){
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.direct_controller_switch_av:
                if(v instanceof TextView){
                    String text = ((TextView) v).getText().toString().trim();
                    if("开始".equals(text)){
                        ((TextView) v).setText("暂停");
                        mHelper.startAV();
                        return ;
                    }else if("暂停".equals(text)){
                        ((TextView) v).setText("继续");
                        mHelper.pouseAV();
                        return ;
                    }else if("继续".equals(text)){
                        ((TextView) v).setText("暂停");
                        mHelper.resumeAV();
                        return ;
                    }
                }
                mHelper.startAV();
                break;
            case R.id.direct_controller_stop_av:
                activity.finish();
                break;
            case R.id.direct_controller_mute_audio:

                break;
            case R.id.direct_controller_switch_camera:
                mHelper.switchCamera();
                break;
            case R.id.direct_controller_switch_bitrate:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter mFilter = new IntentFilter();
        mFilter.setPriority(100);
        mFilter.addAction(DirectUtils.Action.ACTION_START);
        mFilter.addAction(DirectUtils.Action.ACTION_STOP);
        mFilter.addAction(DirectUtils.Action.ACTION_RESTART);
        mFilter.addAction(DirectUtils.Action.ACTION_RELEASE);
        mFilter.addAction(DirectUtils.Action.ACTION_MUTE);
        mFilter.addAction(DirectUtils.Action.ACTION_SWITCH_CAMERA);
        mFilter.addAction(DirectUtils.Action.ACTION_SWITCH_BITRATE);
        mFilter.addAction(DirectUtils.Action.ACTION_OPENFIGHT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(DirectUtils.Action.ACTION_START.equals(action)){
                startVideo();
            }else if(DirectUtils.Action.ACTION_STOP.equals(action)){
                destoryVideo();
            }else if(DirectUtils.Action.ACTION_RESTART.equals(action)){
                reStartVideo();
            }else if(DirectUtils.Action.ACTION_RELEASE.equals(action)){
                destoryVideo();
            }else if(DirectUtils.Action.ACTION_MUTE.equals(action)){
                mHelper.switchVudio(true);
            }else if(DirectUtils.Action.ACTION_SWITCH_CAMERA.equals(action)){
                mHelper.switchCamera();
            }else if(DirectUtils.Action.ACTION_SWITCH_BITRATE.equals(action)){

            }else if(DirectUtils.Action.ACTION_OPENFIGHT.equals(action)){
                mHelper.openFight(intent.getBooleanExtra("openfight", false));
            }
        }
    };
}
