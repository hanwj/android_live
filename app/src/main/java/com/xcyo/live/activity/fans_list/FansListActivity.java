package com.xcyo.live.activity.fans_list;

import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.adapter.SearchUserListViewAdapter;
import com.xcyo.live.record.UserSimpleRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 12/6/16.
 */
public class FansListActivity extends BaseActivity<FansListActPresenter> {
    private List<UserSimpleRecord.User> mListDatas;
    private TextView mBackText;
    private TextView mTitleText;
    private ListView mListView;
    @Override
    protected void initArgs() {
        mListDatas = new ArrayList<>();
        for (int i = 0;i < 10;i++){
            UserSimpleRecord.User record = new UserSimpleRecord.User();
            record.alias = "蔡潇潇";
            record.gender = "1";
            record.level = "1";
            record.signature = "这个人很懒，什么都没有留下";
            mListDatas.add(record);
        }
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_fans_list);
        mBackText = (TextView)findViewById(R.id.base_title_back);
        mTitleText = (TextView)findViewById(R.id.base_title_name);
        mListView = (ListView)findViewById(R.id.act_listview);
        mListView.setAdapter(new SearchUserListViewAdapter(this,mListDatas));

        mTitleText.setText("粉丝");
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mBackText,"back");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }
}
