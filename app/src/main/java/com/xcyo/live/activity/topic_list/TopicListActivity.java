package com.xcyo.live.activity.topic_list;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.activity.topic_content.TopicContentActivity;
import com.xcyo.live.adapter.TopicListViewAdapter;
import com.xcyo.live.record.TopicListServerRecord;
import com.xcyo.live.record.TopicRecord;
import com.xcyo.live.utils.ServerEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 3/6/16.
 */
public class TopicListActivity extends BaseActivity<TopicListActPresenter> {
    private List<TopicRecord> mTopicList;
    private TextView mBackText;
    private TextView mTitleText;
    private ListView mListView;
    @Override
    protected void initArgs() {
        mTopicList = new ArrayList<>();
        for (int i=0;i<20;i++){
            TopicRecord record = new TopicRecord();
            record.topic = "话题 " + i;
            record.num = i * 23 - i;
            mTopicList.add(record);
        }
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_topic_list);
        mBackText = (TextView)findViewById(R.id.base_title_back);
        mTitleText = (TextView)findViewById(R.id.base_title_name);
        mListView = (ListView)findViewById(R.id.topic_list_listview);

        mTitleText.setText("热门话题");
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mBackText,"back");
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicRecord record = (TopicRecord)mListView.getItemAtPosition(position);
                Intent intent = new Intent(TopicListActivity.this, TopicContentActivity.class);
                intent.putExtra("topic",record.topic);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {
        if (ServerEvents.CALL_SERVER_METHOD_TOPIC_LIST.equals(evt)){
            List<TopicRecord> listData = ((TopicListServerRecord)data.record).list;
            mListView.setAdapter(new TopicListViewAdapter(this,listData));
        }
    }
}
