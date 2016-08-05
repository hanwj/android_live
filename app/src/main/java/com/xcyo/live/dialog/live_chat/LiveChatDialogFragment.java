package com.xcyo.live.dialog.live_chat;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseDialogFragment;
import com.xcyo.live.R;
import com.xcyo.live.activity.room.viewholder.ChatRoomMsgRecord;
import com.xcyo.live.activity.room_live.LiveHelper;
import com.xcyo.live.adapter.RoomChatMsgListViewAdapter;
import com.xcyo.live.anim_live.FightingManager;
import com.xcyo.live.anim_live.SlidGiftManager;
import com.xcyo.live.anim_live.UioManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 6/7/16.
 */
public class LiveChatDialogFragment extends BaseDialogFragment<LiveChatDialogPresenter>{
    private View mRootView;
    private View mSendLayout;
    private EditText mInput;
    private Button mSendBtn;
    private ListView mListView;
    private View mBarrageView;
    private TextView mBarrageText;
    private RelativeLayout mControll;
    private boolean isBarrageOpen = false;
    private String yunxinRoomId;
    private boolean isSinger = false;
    private List<ChatRoomMsgRecord> mListData = new ArrayList<>();
    private RoomChatMsgListViewAdapter mAdapter;
    private LiveHelper mHelper;
    @Override
    protected void initArgs() {
        Bundle bundle = getArguments();
        if (bundle != null){
            yunxinRoomId = bundle.getString("yunxinRoomId");
            isSinger = bundle.getBoolean("isSinger",false);
        }
    }

    private SlidGiftManager manager;
    private FightingManager f_manager;
    private UioManager uio_manager;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_live_chat,null);
        setCancelable(false);
        mRootView = view.findViewById(R.id.dialog_root);
        mSendLayout = view.findViewById(R.id.dialog_send_layout);
        mBarrageView = view.findViewById(R.id.dialog_barrage_layout);
        mBarrageText = (TextView) view.findViewById(R.id.dialog_barrage_text);
        mInput = (EditText)view.findViewById(R.id.dialog_input);
        mSendBtn = (Button)view.findViewById(R.id.dialog_send);
        mListView = (ListView)view.findViewById(R.id.room_listview);
        mControll = (RelativeLayout) view.findViewById(R.id.room_controller);
        initControllView(inflater);

        mAdapter = new RoomChatMsgListViewAdapter(getActivity(),mListData);
        mListView.setAdapter(mAdapter);
        setBarrage(isBarrageOpen);
        return view;
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.0f;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        super.onResume();

        manager = SlidGiftManager.get(this.getDialog().getWindow(), R.id.room_amin_container);
        f_manager = FightingManager.get(this.getDialog().getWindow(), R.id.room_fighting_container);
        uio_manager = UioManager.get(this.getDialog().getWindow(), R.id.room_uio_container);
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mRootView,"root");
        addOnClickListener(mBarrageView, "barrage");
        addOnClickListener(mSendBtn, "send");

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    public SlidGiftManager getGiftManager() {
        return manager;
    }

    private void initControllView(LayoutInflater inflater){
        View controllView = inflater.inflate(getControllView(),null,true);
        mControll.addView(controllView);
        if (isSinger){
            initLiveControllView(controllView);
        }else {
            initVideoControllView(controllView);
        }
    }

    //初始化主播控制面板
    private void initLiveControllView(View controllView){
        ImageView chat = (ImageView) controllView.findViewById(R.id.live_chat);
        ImageView recent = (ImageView) controllView.findViewById(R.id.live_recent);
        ImageView music = (ImageView) controllView.findViewById(R.id.live_music);
        ImageView pop = (ImageView) controllView.findViewById(R.id.live_pop);
        ImageView exit = (ImageView) controllView.findViewById(R.id.live_exit);
        addOnClickListener(chat, "chat");
        addOnClickListener(recent, "recent");
        addOnClickListener(music, "music");
        addOnClickListener(pop, "pop");
        addOnClickListener(exit, "exit");

        mHelper = new LiveHelper(getActivity());
    }
    //初始化用户控制面板
    private void initVideoControllView(View controllView){
        ImageView chat = (ImageView) controllView.findViewById(R.id.player_chat);
        ImageView recent = (ImageView) controllView.findViewById(R.id.player_recent);
        ImageView share = (ImageView) controllView.findViewById(R.id.player_share);
        ImageView gift = (ImageView) controllView.findViewById(R.id.player_gift);
        ImageView exit = (ImageView) controllView.findViewById(R.id.player_exit);
        addOnClickListener(chat, "chat");
        addOnClickListener(recent, "recent");
        addOnClickListener(share, "share");
        addOnClickListener(gift, "gift");
        addOnClickListener(exit, "exit");
    }

    private int getControllView(){
        if (isSinger){
            return R.layout.controller_live;
        }else {
            return R.layout.controller_player;
        }
    }

    public void showSendLayout(boolean visible){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (visible){
            mControll.setVisibility(View.GONE);
            mSendLayout.setVisibility(View.VISIBLE);
            //键盘弹起
            mInput.requestFocus();
            imm.showSoftInput(mInput, InputMethodManager.SHOW_FORCED);

        }else {
            mControll.setVisibility(View.VISIBLE);
            mSendLayout.setVisibility(View.GONE);
            //隐藏键盘
            imm.hideSoftInputFromWindow(mInput.getWindowToken(),0);
        }

    }
    //切换弹幕开关
    protected void switchBarrage(){
        isBarrageOpen = !isBarrageOpen;
        setBarrage(isBarrageOpen);
    }
    //设置弹幕开关
    protected void setBarrage(boolean isOpen){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBarrageText.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,isOpen ? 0 : -1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,isOpen ? -1 : 0);
        mBarrageText.setLayoutParams(layoutParams);
        mBarrageText.setSelected(isOpen);
        mBarrageView.setSelected(isOpen);

    }

    protected String getYunxinRoomId(){
        return yunxinRoomId;
    }

    protected LiveHelper getLiveHelper(){
        return mHelper;
    }

    //获取输入的文字
    protected String getInput(){
        return mInput.getText().toString();
    }
    //清空输入的文字
    protected void clearInput() {
        mInput.setText("");
    }
    //更新listview
    public void addMessage(List<ChatRoomMsgRecord> messages,boolean forceScrollToBottom){
//        for (ChatRoomMsgRecord msg : messages){
//            if (msg.getMsgType() == MsgTypeEnum.text){
//                mListData.add(msg);
//            }
//        }
        mListData.addAll(messages);
        mAdapter.notifyDataSetChanged();
        setScrollToBottom(forceScrollToBottom);
    }
    //是否需要滑到底部
    protected void setScrollToBottom(boolean force){
        boolean isScrollToBottom = force ? force : isLastVisible();
        if (isScrollToBottom){
            mListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelectionFromTop(mListView.getAdapter().getCount()-1,0);
                }
            },200);
        }
    }
    //检测当前可见的是否是最后一个
    private boolean isLastVisible(){
        if (mListView.getLastVisiblePosition() >= mListView.getAdapter().getCount() - 1){
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        manager.recyle();
        f_manager.recyle();
        uio_manager.recyle();
        if (mHelper != null){
            mHelper.destoryResource();
        }
        super.onDestroy();
    }
}
