package com.xcyo.live.activity.rank;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.record.UserSimpleRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixiaoxiao on 14/6/16.
 */
public class RankActivity extends BaseActivity<RankActPresenter>{
    private List<UserSimpleRecord.User> mListData;
    private TextView mTitleText;
    private TextView mBackText;
    private ListView mListView;

    @Override
    protected void initArgs() {
        mListData = new ArrayList<>();
        for (int i = 0;i < 10;i++){
            UserSimpleRecord.User record = new UserSimpleRecord.User();
            record.alias = "蔡潇潇";
            record.gender = "1";
            record.level = "1";
            record.signature = "这个人很懒，什么都没有留下";
            mListData.add(record);
        }
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_rank);
        mBackText = (TextView)findViewById(R.id.base_title_back);
        mTitleText = (TextView)findViewById(R.id.base_title_name);
        mListView = (ListView) findViewById(R.id.rank_listview);
        mTitleText.setText("贡献榜");
        setTopThreeView();
        mListView.setAdapter(new RankListAdapter(mListData));
    }

    @Override
    protected void initEvents() {
        addOnClickListener(mBackText,"back");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    //设置前三名
    private void setTopThreeView(){
        View topThree = getLayoutInflater().inflate(R.layout.layout_rank_top_three,mListView,false);
        int[] topThreeIDs = {R.id.rank_top1,R.id.rank_top2,R.id.rank_top3};
        for (int i = 0;i < topThreeIDs.length;i ++){
            View item = topThree.findViewById(topThreeIDs[i]);
            if (i < mListData.size()){
                item.setVisibility(View.VISIBLE);
                UserSimpleRecord.User record  = mListData.get(i);
                ((TextView)item.findViewById(R.id.user_name)).setText(record.alias);

            }else {
                item.setVisibility(View.GONE);
            }
        }
        mListView.addHeaderView(topThree);
    }

    private class RankListAdapter extends BaseAdapter{
        private List<UserSimpleRecord.User> mListData;
        public RankListAdapter(List<UserSimpleRecord.User> listData){
            if (listData.size() > 3){
                this.mListData = listData.subList(3,listData.size());
            }else {
                this.mListData = new ArrayList<>();
            }
        }
        @Override
        public int getCount() {
            return this.mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return this.mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null){
                View v = getLayoutInflater().inflate(R.layout.item_cost_rank,null);
                holder = new ViewHolder();
                holder.rank = (TextView)v.findViewById(R.id.item_rank_num);
                holder.icon = (ImageView)v.findViewById(R.id.item_rank_icon);
                holder.name = (TextView)v.findViewById(R.id.user_name);
                holder.sex = (ImageView)v.findViewById(R.id.user_sex);
                holder.lvl = (TextView)v.findViewById(R.id.user_lvl);
                holder.content = (TextView)v.findViewById(R.id.item_cost_str);
                holder.name.setTextColor(getResources().getColor(R.color.tipGrayColor));
                convertView = v;
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            int rank = position + 4;
            if (rank < 10){
                holder.rank.setText("0" + rank + ".");
            }else {
                holder.rank.setText(rank + ".");
            }
            return convertView;
        }

        class ViewHolder{
            public TextView rank;
            public ImageView icon;
            public TextView name;
            public ImageView sex;
            public TextView lvl;
            public TextView content;
        }
    }
}
