package com.xcyo.live.fragment.home_hot;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jazz.direct.libs.DirectUtils;
import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseFragment;
import com.xcyo.baselib.utils.ToastUtil;
import com.xcyo.live.R;
import com.xcyo.live.activity.room_player.VideoActivity;
import com.xcyo.live.adapter.HomeHotAdPagerAdapter;
import com.xcyo.live.adapter.HomeSingerListViewAdapter;
import com.xcyo.live.record.SingerListServerRecord;
import com.xcyo.live.record.SingerRecord;
import com.xcyo.live.utils.Constants;
import com.xcyo.live.utils.ServerEvents;
import com.xcyo.live.view.IndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by caixiaoxiao on 1/6/16.
 */
public class HomeHotFragment extends BaseFragment<HomeHotFragPresenter> {
    private String sex = Constants.SEX_ALL;
    private String city = "热门";
    private List<SingerRecord> mListData;
    private LayoutInflater inflater;
    private PtrClassicFrameLayout mPtrFrame;
    private ListView mListView;
    private View mEmptyView;
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
        this.inflater = inflater;
        View v = inflater.inflate(R.layout.frag_home_hot,null);
        mPtrFrame = (PtrClassicFrameLayout)v.findViewById(R.id.home_hot_ptr);
        mListView = (ListView)v.findViewById(R.id.home_hot_listview);
        mEmptyView = v.findViewById(R.id.empty_layout);

        initAdView();
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.disableWhenHorizontalMove(true);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !frame.isRefreshing() && PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                mPtrFrame.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        setListView(mListData);
//                        mPtrFrame.refreshComplete();
//                    }
//                }, 2000);
                presenter().loadSingerList();
            }
        });
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 200);
        return v;
    }

    @Override
    protected void initEvents() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingerRecord record = (SingerRecord)mListView.getItemAtPosition(position);
                Map<String,String> params = new HashMap<String, String>();
                params.put("roomId",record.roomId);
                DirectUtils.openVideoStream(getActivity(), VideoActivity.class, null, params);
            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_LIVE_HOT.equals(evt)){
            SingerListServerRecord record = (SingerListServerRecord)data.record;
            setListView(record.list);
            mPtrFrame.refreshComplete();
        }
    }

    @Override
    public void onServerFailedCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_LIVE_HOT.equals(evt)){
            mPtrFrame.refreshComplete();
        }
    }

    private void setListView(List<SingerRecord> listData){
        if (listData.size() > 0){
            mEmptyView.setVisibility(View.GONE);
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mListView.setAdapter(new HomeSingerListViewAdapter(getActivity(),listData));
    }

    private void initAdView(){
        View headerView = inflater.inflate(R.layout.layout_main_home_ad,mListView,false);
        ViewPager viewPager = (ViewPager)headerView.findViewById(R.id.main_home_ad_viewpager);
        IndicatorView indicatorView = (IndicatorView)headerView.findViewById(R.id.main_home_ad_indicator);
        viewPager.setAdapter(new HomeHotAdPagerAdapter(getActivity()));
        indicatorView.setViewPager(viewPager);
        mListView.addHeaderView(headerView);
    }

    public void setSexCategory(String sex){
        this.sex = sex;
    }
    public String getSexCategory(){
        return sex;
    }

    public void setCityCategory(String city){
        this.city = city;
    }
    public String getCityCategory(){
        return city;
    }
}
