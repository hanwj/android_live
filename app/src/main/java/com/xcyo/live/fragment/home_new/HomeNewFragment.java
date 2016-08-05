package com.xcyo.live.fragment.home_new;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;
import com.xcyo.live.activity.topic_content.TopicContentActivity;
import com.xcyo.live.activity.topic_list.TopicListActivity;
import com.xcyo.live.adapter.HomeNewGridViewAdapter;
import com.xcyo.live.record.HomeNewServerRecord;
import com.xcyo.live.record.TopicRecord;
import com.xcyo.live.utils.ServerEvents;
import com.xcyo.live.view.HeaderGridView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by caixiaoxiao on 2/6/16.
 */
public class HomeNewFragment extends BaseFragment<HomeNewFragPresenter> {
    private List<TopicRecord> mTopicList;
    private PtrClassicFrameLayout mPtrFrame;
    private HeaderGridView mGridView;
    private View mTopicLayout,mTopicSubLayout1,mTopicSplitLine1;
    private TextView topic1,topic2,topic3,topicMore;
    private LayoutInflater mInflater;
    @Override
    protected void initArgs() {
        mTopicList = new ArrayList<>();
//        for (int i=0;i<20;i++){
//            mTopicList.add("话题" + i);
//        }

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        this.mInflater = inflater;
        View v = inflater.inflate(R.layout.frag_home_new,null);
        mPtrFrame = (PtrClassicFrameLayout)v.findViewById(R.id.home_new_ptr);
        mGridView = (HeaderGridView)v.findViewById(R.id.home_new_gridview);

        initTopicView();
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !mPtrFrame.isRefreshing() && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                mPtrFrame.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mGridView.setAdapter(new HomeNewGridViewAdapter(getActivity(), mListData));
//                        mPtrFrame.refreshComplete();
//                    }
//                }, 500);
                presenter().loadSingerList();
            }
        });
        mPtrFrame.autoRefresh();
        return v;
    }

    @Override
    protected void initEvents() {
        addOnClickListener(topic1, "topic1");
        addOnClickListener(topic2, "topic2");
        addOnClickListener(topic3, "topic3");
        addOnClickListener(topicMore, "more_topic");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_LIVE_NEW.equals(evt)){
            mPtrFrame.refreshComplete();
            HomeNewServerRecord record = (HomeNewServerRecord)data.record;
            mTopicList = record.topics;
            setTopicView();
            mGridView.setAdapter(new HomeNewGridViewAdapter(getActivity(),record.list));
        }
    }

    @Override
    public void onServerFailedCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_LIVE_NEW.equals(evt)){
            mPtrFrame.refreshComplete();
        }
    }

    private void initTopicView(){
        View view = mInflater.inflate(R.layout.layout_home_new_header,null);
        mTopicLayout = view.findViewById(R.id.header_topic_layout);
        mTopicSubLayout1 = view.findViewById(R.id.header_topic_12);
        mTopicSplitLine1 = view.findViewById(R.id.header_split_line1);
        topic1 = (TextView) view.findViewById(R.id.header_topic1);
        topic2 = (TextView) view.findViewById(R.id.header_topic2);
        topic3 = (TextView) view.findViewById(R.id.header_topic3);
        topicMore = (TextView) view.findViewById(R.id.header_topic_more);
        mGridView.addHeaderView(view);
    }

    private void setTopicView(){
        mTopicLayout.setVisibility(View.VISIBLE);
        if (mTopicList.size() >= 3){
            mTopicSubLayout1.setVisibility(View.VISIBLE);
            mTopicSplitLine1.setVisibility(View.VISIBLE);
            topic1.setText(mTopicList.get(0).topic);
            topic2.setText(mTopicList.get(1).topic);
            topic3.setText(mTopicList.get(2).topic);
        }else if (mTopicList.size() >= 1){
            mTopicSubLayout1.setVisibility(View.GONE);
            mTopicSplitLine1.setVisibility(View.GONE);
            topic3.setText(mTopicList.get(0).topic);
        }else {
            mTopicLayout.setVisibility(View.GONE);
        }
    }

    public void startTopicContentActivity(int position){
        TopicRecord record;
        if (mTopicList.size() < 3){
            record = mTopicList.get(0);
        }else {
            record = mTopicList.get(position);
        }
        Intent intent = new Intent(getActivity(), TopicContentActivity.class);
        intent.putExtra("topic", record.topic);
        startActivity(intent);
    }

    public void startTopicListActivity(){
        Intent intent = new Intent(getActivity(), TopicListActivity.class);
        startActivity(intent);
    }
}
