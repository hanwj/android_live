package com.xcyo.live.activity.room;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jazz.direct.libs.Action.DirectMainActivity;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;
import com.xcyo.live.activity.room.viewholder.ChatRoomMsgRecord;
import com.xcyo.live.adapter.AudienceAdapter;
import com.xcyo.live.adapter.RoomChatMsgListViewAdapter;
import com.xcyo.live.anim_live.FightingManager;
import com.xcyo.live.anim_live.SlidGiftManager;
import com.xcyo.live.anim_live.UioManager;
import com.xcyo.live.helper.DividerItemDecoration;
import com.xcyo.live.model.RoomModel;
import com.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by TDJ on 2016/7/6.
 */
public abstract class RoomActivity<P extends RoomPresent> extends DirectMainActivity<P> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = SlidGiftManager.get(getWindow(), R.id.room_amin_container);
        f_manager = FightingManager.get(getWindow(), R.id.room_fighting_container);
        uio_manager = UioManager.get(getWindow(), R.id.room_uio_container);
    }

    @Override
    protected final void initView() {
        setContentView(R.layout.activity_room);

        rootView = findViewById(R.id.room_root_view);
        mControll = (RelativeLayout) findViewById(R.id.room_controller);

        audience = (RecyclerView) findViewById(R.id.room_audience);
        audience.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        RecyclerView.ItemDecoration diver = new DividerItemDecoration(this, LinearLayout.HORIZONTAL);
        ((DividerItemDecoration)diver).setDiver(new ColorDrawable(Color.TRANSPARENT));
        ((DividerItemDecoration)diver).setDiverHeight(Util.dp2px(8));
        audience.addItemDecoration(diver);
        audience.setAdapter(new AudienceAdapter(this));

        icon = (ImageView) findViewById(R.id.room_singler_icon);
        name = (TextView) findViewById(R.id.room_singler_name);
        uid = (TextView) findViewById(R.id.room_singler_uid);
        follow = (TextView) findViewById(R.id.room_singler_follow);
        contribution = (LinearLayout) findViewById(R.id.room_singler_contribution_list);
        coin = (TextView) findViewById(R.id.room_singler_coin);

        mSendLayout = findViewById(R.id.dialog_send_layout);
        mBarrageView = findViewById(R.id.dialog_barrage_layout);
        mBarrageText = (TextView) findViewById(R.id.dialog_barrage_text);
        mInput = (EditText)findViewById(R.id.dialog_input);
        mSendBtn = (Button)findViewById(R.id.dialog_send);

        //初始化listview
        mListView = (ListView)findViewById(R.id.room_listview);
        mAdapter = new RoomChatMsgListViewAdapter(this,mListData);
        mListView.setAdapter(mAdapter);

        //加载底部操作布局
        View controllView = getLayoutInflater().inflate(getControllView(), null, true);
        if(controllView != null){
            this.mControll.addView(controllView);
            initControllView();
        }

        setBarrage(isBarrageOpen);
    }

    @Override
    protected void initEvent() {
        addOnClickListener(rootView,"root");
        addOnClickListener(mBarrageView,"barrage");
        addOnClickListener(mSendBtn,"send");
        addOnClickListener(icon, "show_me");
    }

    protected abstract int getControllView();
    protected abstract void initControllView();
    protected abstract boolean isSinger();

    private SlidGiftManager manager;
    private FightingManager f_manager;
    private UioManager uio_manager;

    private RecyclerView audience;

    private View rootView;
    private ImageView icon;
    private TextView name;
    private TextView uid;
    private TextView follow;
    private LinearLayout contribution;
    private TextView coin;
    private RelativeLayout mControll;



    private View mSendLayout;
    private EditText mInput;
    private Button mSendBtn;
    private ListView mListView;
    private View mBarrageView;
    private TextView mBarrageText;
    private boolean isBarrageOpen = false;
    private List<ChatRoomMsgRecord> mListData = new ArrayList<>();
    private RoomChatMsgListViewAdapter mAdapter;

    public void refreshSingerInfo(){
        RoomModel model = RoomModel.getInstance();
        x.image().bind(icon, "http://file.xcyo.com/"+ model.getSinger().avatar);
        name.setText(handEmpty(model.getSinger().alias));
        uid.setText("悠悠号: "+handEmpty(model.getSinger().uid));
        coin.setText(handEmpty(model.getSinger().bean));
        audience.getAdapter().notifyDataSetChanged();
    }

    public void showAudience(List<ChatRoomMember> members){
        ((AudienceAdapter)this.audience.getAdapter()).setData(members);
    }

    private String handEmpty(String content){
        if(TextUtils.isEmpty(content)){
            return "";
        }
        return content;
    }

    //显示聊天输入布局
    public void showSendLayout(boolean visible){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
    public boolean switchBarrage(){
        isBarrageOpen = !isBarrageOpen;
        setBarrage(isBarrageOpen);
        return isBarrageOpen;
    }
    //设置弹幕开关
    protected void setBarrage(boolean isOpen){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBarrageText.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, isOpen ? 0 : -1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, isOpen ? -1 : 0);
        mBarrageText.setLayoutParams(layoutParams);
        mBarrageText.setSelected(isOpen);
        mBarrageView.setSelected(isOpen);
        if (TextUtils.isEmpty(getInput())){
            String hint = isOpen ? "开启大喇叭，1悠币/条" : "和大家说点什么";
            mInput.setHint(hint);
        }
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

    public SlidGiftManager getGiftManager() {
        return manager;
    }

    public FightingManager getFightManager() {
        return f_manager;
    }

    public UioManager getUiomanager() {
        return uio_manager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.recyle();
        f_manager.recyle();
        uio_manager.recyle();
    }

    @Override
    public void setVideoPathWithStart(String urlPath) {
        super.setVideoPathWithStart(urlPath);
    }
}
