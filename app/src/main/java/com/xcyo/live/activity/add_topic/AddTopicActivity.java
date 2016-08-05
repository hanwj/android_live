package com.xcyo.live.activity.add_topic;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.activity.live_preview.LivePreviewActivity;
import com.xcyo.live.adapter.TopicListViewAdapter;
import com.xcyo.live.record.TopicListServerRecord;
import com.xcyo.live.record.TopicRecord;
import com.xcyo.live.utils.ServerEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 6/6/16.
 */
public class AddTopicActivity extends BaseActivity<AddTopicActPresenter> {
    private List<TopicRecord> mTopicList;
    private ListView mListView;
    private TextView mCancelText;
    private EditText mContentEditText;
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
        setContentView(R.layout.activity_add_topic);
        mCancelText = (TextView)findViewById(R.id.add_topic_cancel);
        mContentEditText = (EditText)findViewById(R.id.add_topic_content);
        mListView = (ListView)findViewById(R.id.add_topic_listview);
//        mListView.setAdapter(new TopicListViewAdapter(this,mTopicList));
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mCancelText,"back");
        mContentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                    finishActivity("#" + mContentEditText.getText().toString() + "#");
                    return true;
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TopicRecord record = (TopicRecord)mListView.getItemAtPosition(position);
                finishActivity(record.topic);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_empty,R.anim.anim_top_to_bottom_exit);
    }

    public void finishActivity(String topic){
        Intent intent = new Intent(this,LivePreviewActivity.class);
        intent.putExtra("topic",topic);
        setResult(RESULT_OK,intent);
        finish();
    }
}
