package com.xcyo.live.fragment.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.baselib.utils.LogUtil;
import com.xcyo.live.R;
import com.xcyo.live.adapter.FaceViewPagerAdapter;
import com.xcyo.live.adapter.MessageListViewAdapter;
import com.xcyo.live.utils.Constants;
import com.xcyo.live.view.IndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caixiaoxiao on 27/6/16.
 */
public class MessageFragment extends BaseFragment<MessageFragPresenter>{
    public static int MSG_PHOTO = 101,MSG_CAMERA = 102;
    public static int FACE_VIEW = 1, MORE_VIEW = 2;
    public static int TYPE_KEYBOARD = 1,TYPE_VOICE = 2;
    private List<Map<String,Object>> mActionList;
    private List<IMMessage> mMsgListData;
    private List<String> mFacesList;
    private ListView mListView;
    private ImageView mVoiceImg,mkeyboardImg;
    private EditText mEditText;
    private Button mVoiceBtn;
    private ImageView mFaceImg,mMoreImg;
    private View mContainer,mFaceContainer;
    private ViewPager mFaceViewPager;
    private IndicatorView mFaceIndicator;
    private GridView mMoreGridView;
    private View mAudioLayout,mAudioIngLayout;
    private ImageView mAudioCancelImg;
    private TextView mAudioTipText;
    private Button mSendBtn;
    private String toUid,toAlias;
    private MessageListViewAdapter mAdapter;
    private Observer<List<IMMessage>> incomingMsgObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            addMessages(imMessages,false);
        }
    };

    @Override
    protected void initArgs() {
        mActionList = new ArrayList<>();
        String[] names = {"图片","照相","礼物"};
        int[] ids = {R.mipmap.img_btn_icon,R.mipmap.photo_btn_icon,R.mipmap.gift_btn_icon};
        for (int i = 0;i < names.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("name",names[i]);
            map.put("id",ids[i]);
            mActionList.add(map);
        }

        mFacesList = new ArrayList<>();
        for (int i = 0;i < 62;i++){
            mFacesList.add("face");
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.frag_message, null);
        mListView = (ListView)v.findViewById(R.id.message_listview);
        mVoiceImg = (ImageView)v.findViewById(R.id.message_voice);
        mkeyboardImg = (ImageView)v.findViewById(R.id.message_keyboard);
        mEditText = (EditText)v.findViewById(R.id.message_edittext);
        mVoiceBtn = (Button)v.findViewById(R.id.message_voice_btn);
        mFaceImg = (ImageView)v.findViewById(R.id.message_face);
        mMoreImg = (ImageView)v.findViewById(R.id.message_more);
        mContainer = v.findViewById(R.id.message_container);
        mFaceContainer = v.findViewById(R.id.message_face_container);
        mFaceViewPager = (ViewPager)v.findViewById(R.id.message_face_viewpager);
        mFaceIndicator = (IndicatorView)v.findViewById(R.id.message_face_indicator);
        mMoreGridView = (GridView)v.findViewById(R.id.message_more_container);
        mAudioLayout = v.findViewById(R.id.message_play_audio);
        mAudioIngLayout = v.findViewById(R.id.message_audio_ing);
        mAudioCancelImg = (ImageView)v.findViewById(R.id.message_audio_cancel);
        mAudioTipText = (TextView)v.findViewById(R.id.message_audio_tip);
        mSendBtn = (Button)v.findViewById(R.id.message_send);

        mFaceViewPager.setAdapter(new FaceViewPagerAdapter(getActivity(),mFacesList));
        mFaceIndicator.setViewPager(mFaceViewPager);
        mMoreGridView.setAdapter(new SimpleAdapter(getActivity(), mActionList, R.layout.item_message_one_action,
                new String[]{"name", "id"}, new int[]{R.id.item_name, R.id.item_img}));

        return v;
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mVoiceImg,"voiceView");
        addOnClickListener(mkeyboardImg,"keyboardView");
        addOnClickListener(mEditText,"keyboardView");
        addOnClickListener(mFaceImg,"faceView");
        addOnClickListener(mMoreImg, "moreView");
        addOnClickListener(mSendBtn,"send");
        mVoiceBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    startAudio();
                } else if (MotionEvent.ACTION_UP == event.getAction() ||
                        MotionEvent.ACTION_CANCEL == event.getAction()) {
                    endAudio(isCanceled(v, event));
                } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
                    cancelAudio(isCanceled(v, event));
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mSendBtn.setVisibility(View.INVISIBLE);
                    mMoreImg.setVisibility(View.VISIBLE);
                } else {
                    mSendBtn.setVisibility(View.VISIBLE);
                    mMoreImg.setVisibility(View.INVISIBLE);
                }
            }
        });
        mMoreGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    openPhoto();
                } else if (position == 1){
                    openCamera();
                }
            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    public void onResume() {
        super.onResume();
        NIMClient.getService(MsgService.class).setChattingAccount(toUid, SessionTypeEnum.P2P);
    }

    @Override
    public void onPause() {
        super.onPause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }

    @Override
    public void onDestroy() {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMsgObserver, false);
        super.onDestroy();
    }

    protected void initMsgList(){
        mMsgListData = new ArrayList<>();
        mAdapter = new MessageListViewAdapter(getActivity(),mMsgListData);
        mListView.setAdapter(mAdapter);
        RequestCallbackWrapper<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {
                addMessages(imMessages,true);
            }
        };
        NIMClient.getService(MsgService.class)
                .queryMessageListEx(getAnchor(), QueryDirectionEnum.QUERY_OLD, 20, true)
                .setCallback(callback);

        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMsgObserver, true);
    }

    protected void addMessages(List<IMMessage> messages,boolean forceScrollToBottom){
        mMsgListData.addAll(messages);
        mAdapter.notifyDataSetChanged();
        setScrollToBottom(forceScrollToBottom);
    }

    protected IMMessage getAnchor(){
        if (mMsgListData.size() == 0){
            return MessageBuilder.createEmptyMessage(toUid,SessionTypeEnum.P2P,0);
        }
        return mMsgListData.get(0);
    }

    private void openPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, MSG_PHOTO);
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File((Constants.SD_CACHE + "temp.jpg"))));
        startActivityForResult(intent, MSG_CAMERA);
    }

    public void setTo(String toUid,String toAlias){
        this.toUid = toUid;
        this.toAlias = toAlias;
        initMsgList();
    }

    protected void startAudio(){
        mAudioLayout.setVisibility(View.VISIBLE);
        mAudioIngLayout.setVisibility(View.VISIBLE);
        mAudioCancelImg.setVisibility(View.GONE);
        mVoiceBtn.setText("松开结束");
        mAudioTipText.setText("手指上滑，取消发送");
        mAudioTipText.setBackgroundColor(Color.TRANSPARENT);
    }

    protected void endAudio(boolean cancel){
        mAudioLayout.setVisibility(View.GONE);
        mVoiceBtn.setText("按住说话");
    }


    protected void cancelAudio(boolean cancel){
        if (!cancel){
            mAudioIngLayout.setVisibility(View.VISIBLE);
            mAudioCancelImg.setVisibility(View.GONE);
            mAudioTipText.setText("手指上滑，取消发送");
            mAudioTipText.setBackgroundColor(Color.TRANSPARENT);
            return;
        }
        mAudioIngLayout.setVisibility(View.GONE);
        mAudioCancelImg.setVisibility(View.VISIBLE);
        mAudioTipText.setText("松开手指，取消发送");
        mAudioTipText.setBackgroundResource(R.drawable.shape_audio_cancel_bg);
    }

    protected boolean isCanceled(View v,MotionEvent event){
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        LogUtil.e("audio", location[0] + "," + location[1] + " ? " + event.getRawX() + "," + event.getRawY());
        if (event.getRawX() < location[0] || event.getRawX() > location[0] + v.getWidth()
                || event.getRawY() < location[1] - 40){
            return true;
        }
        return false;
    }

    //发送语音和发送消息切换
    public void setMessageType(int type){
        mVoiceImg.setVisibility(View.GONE);
        mVoiceBtn.setVisibility(View.GONE);
        mkeyboardImg.setVisibility(View.GONE);
        mEditText.setVisibility(View.GONE);
        setBottomContainer(false, -1);
        if (TYPE_KEYBOARD == type){
            mVoiceImg.setVisibility(View.VISIBLE);
            mEditText.setVisibility(View.VISIBLE);
            showKeyboard(true);
        }else if (TYPE_VOICE == type){
            mkeyboardImg.setVisibility(View.VISIBLE);
            mVoiceBtn.setVisibility(View.VISIBLE);
            showKeyboard(false);
        }
    }

    //表情布局和更多布局切换
    public void setBottomContainer(boolean visibile,int view){
        if (visibile){
            mContainer.setVisibility(View.VISIBLE);
            mFaceContainer.setVisibility(View.GONE);
            mMoreGridView.setVisibility(View.GONE);
            showKeyboard(false);
            if (FACE_VIEW == view){
                mFaceContainer.setVisibility(View.VISIBLE);
            }else if (MORE_VIEW == view){
                mMoreGridView.setVisibility(View.VISIBLE);
            }else{
                mContainer.setVisibility(View.GONE);
            }
        }else{
            mContainer.setVisibility(View.GONE);
        }
    }

    //键盘显示隐藏
    private void showKeyboard(boolean isShow){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow){
            mEditText.requestFocus();
            imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
        }else{
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }

    //获取键盘输入
    protected String getInputString(){
        return mEditText.getText().toString();
    }
    //清空键盘输入
    protected void clearInputString(){
        mEditText.setText("");
    }

    protected String getToUid(){
        return toUid;
    }

    //是否滑动到底部
    private void setScrollToBottom(boolean force){
        boolean isScrollToBottom = force ? force : isLastMessageVisible();
        if (isScrollToBottom){
            mListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelectionFromTop(mListView.getAdapter().getCount() - 1,0);
                }
            },200);
        }
    }

    private boolean isLastMessageVisible(){
        if (mListView.getLastVisiblePosition() >= mListView.getAdapter().getCount() - 1){
            return true;
        }
        return false;
    }
}
