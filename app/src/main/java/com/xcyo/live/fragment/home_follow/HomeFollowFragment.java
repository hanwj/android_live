package com.xcyo.live.fragment.home_follow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.live.R;
import com.xcyo.live.adapter.HomeSingerListViewAdapter;
import com.xcyo.live.record.SingerListServerRecord;
import com.xcyo.live.record.SingerRecord;
import com.xcyo.live.utils.ServerEvents;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by caixiaoxiao on 2/6/16.
 */
public class HomeFollowFragment extends BaseFragment<HomeFollowFragPresenter> {
    private List<SingerRecord> mListData;
    private View mHeaderView;
    private View mEmptyView;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView mListView;
    private Button mJumpBtn;
    @Override
    protected void initArgs() {
        mListData = new ArrayList<>();
        for (int i = 0;i < 10;i++){
            SingerRecord record = new SingerRecord();
            record.alias = "蔡潇潇";
            record.locate = "未知";
            record.memberNum = "1234";
            if (i%2 == 0){
                record.title = "#话题";
            }
            mListData.add(record);
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.frag_home_follow,null);
        mPtrFrame = (PtrClassicFrameLayout)v.findViewById(R.id.home_follow_ptr);
        mListView = (ListView) v.findViewById(R.id.home_follow_listview);
        mHeaderView = inflater.inflate(R.layout.layout_home_follow_header_view,null);
        mJumpBtn = (Button)mHeaderView.findViewById(R.id.home_follow_jump);
        mEmptyView = mHeaderView.findViewById(R.id.home_follow_empty);
        mListView.addHeaderView(mHeaderView);

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !frame.isRefreshing() && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                mPtrFrame.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        setListView(mListData);
//                        mPtrFrame.refreshComplete();
//                    }
//                }, 1000);
                presenter().loadSingerList();
            }
        });
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        },200);
        return v;
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mJumpBtn, "gotoHot");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_LIVE_FOLLOW.equals(evt)){
            SingerListServerRecord record = (SingerListServerRecord)data.record;
            setListView(record.list);
            mPtrFrame.refreshComplete();
        }
    }

    @Override
    public void onServerFailedCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_LIVE_FOLLOW.equals(evt)){
            mPtrFrame.refreshComplete();
        }
    }

    public void setListView(List<SingerRecord> listData){
        if (mListData.size() > 0){
            mEmptyView.setVisibility(View.GONE);
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mListView.setAdapter(new HomeSingerListViewAdapter(getActivity(), listData));
    }
}
