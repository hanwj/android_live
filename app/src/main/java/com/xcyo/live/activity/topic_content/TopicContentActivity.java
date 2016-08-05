package com.xcyo.live.activity.topic_content;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.adapter.HomeSingerListViewAdapter;
import com.xcyo.live.record.SingerListServerRecord;
import com.xcyo.live.record.SingerRecord;
import com.xcyo.live.utils.ServerEvents;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by caixiaoxiao on 3/6/16.
 */
public class TopicContentActivity extends BaseActivity<TopicContentActPresenter> {
    private String mTopic;
    private TextView mBackText;
    private TextView mTitleText;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView mListView;
    private ImageView mEnjoyImg;
    private View mEmptyView;
    @Override
    protected void initArgs() {
        Intent intent = getIntent();
        mTopic = intent.getStringExtra("topic");
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_topic_content);
        mBackText = (TextView)findViewById(R.id.base_title_back);
        mTitleText = (TextView)findViewById(R.id.base_title_name);
        mPtrFrame = (PtrClassicFrameLayout)findViewById(R.id.topic_content_ptr);
        mListView = (ListView)findViewById(R.id.topic_content_listview);
        mEmptyView = findViewById(R.id.empty_layout);
        mEnjoyImg = (ImageView)findViewById(R.id.topic_content_enjoy);
        mTitleText.setText(mTopic);
//        setListView();
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !frame.isRefreshing() && PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter().loadSingerList();
            }
        });
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        },200);
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mBackText, "back");
        addOnClickListener(mEnjoyImg, "enjoy");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_TOPIC_ROOM.equals(evt)){
            SingerListServerRecord record = (SingerListServerRecord)data.record;
            setListView(record.list);
            mPtrFrame.refreshComplete();
        }
    }

    @Override
    public void onServerFailedCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_TOPIC_ROOM.equals(evt)){
            mPtrFrame.refreshComplete();
        }
    }

    private void setListView(List<SingerRecord> listData){
        if (listData.size() > 0){
            mEmptyView.setVisibility(View.GONE);
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mListView.setAdapter(new HomeSingerListViewAdapter(this,listData));
    }

    protected String getTopic(){
        return mTopic;
    }
}
