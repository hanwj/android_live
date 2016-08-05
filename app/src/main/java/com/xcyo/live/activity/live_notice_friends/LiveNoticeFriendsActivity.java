package com.xcyo.live.activity.live_notice_friends;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.adapter.NoticeFriendsListViewAdapter;
import com.xcyo.live.record.UserSimpleRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 22/6/16.
 */
public class LiveNoticeFriendsActivity extends BaseActivity<LiveNoticeFriendsActPresenter> {
    private List<UserSimpleRecord.User> mListData;
    private View mEmptyView;
    private ListView mListView;
    private Button mConfirmBtn;
    @Override
    protected void initArgs() {
        mListData = new ArrayList<>();
        for (int i = 0;i < 10;i++){
            UserSimpleRecord.User record = new UserSimpleRecord.User();
            record.uid = "1000" + i;
            record.alias = "蔡潇潇";
            record.level = i + 1 + "";
            record.signature = "这个人很懒";
            mListData.add(record);
        }
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_live_notice_friends);
        mListView = (ListView)findViewById(R.id.live_notice_friends_listview);
        mEmptyView = findViewById(R.id.empty_layout);
        mConfirmBtn = (Button)findViewById(R.id.live_notice_friends_cancel);
        setListView();

    }

    @Override
    protected void initEvents() {
        addOnClickListener(mConfirmBtn,"back");
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int count = mListView.getCheckedItemCount();
                if (count > 0) {
                    mConfirmBtn.setText("完成(" + count + ")");
                } else {
                    mConfirmBtn.setText("取消");
                }
            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    @Override
    public void finish() {
        long[] ids = mListView.getCheckedItemIds();
        ArrayList<String> uids = new ArrayList<>();
        for (int i = 0;i < ids.length;i++){
            uids.add(((UserSimpleRecord.User)mListView.getItemAtPosition((int)ids[i])).uid);
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra("uid",uids);
        setResult(RESULT_OK, intent);
        super.finish();
        overridePendingTransition(R.anim.anim_empty,R.anim.anim_left_to_right_exit);
    }

    private void setListView(){
        if (mListData.size() > 0){
            mEmptyView.setVisibility(View.GONE);
            mListView.setAdapter(new NoticeFriendsListViewAdapter(this,mListData));
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

}
