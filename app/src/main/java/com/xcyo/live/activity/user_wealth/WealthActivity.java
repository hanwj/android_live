package com.xcyo.live.activity.user_wealth;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/8.
 */
public class WealthActivity extends BaseActivity<WealthPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_usr_wealth);

        img = (ImageView) findViewById(R.id.usr_wealth_icon);
        number = (TextView)findViewById(R.id.usr_wealth_number);

        list = (ListView) findViewById(R.id.usr_wealth_list);
        this.list.setAdapter(new WealthAdapter());
        list.addFooterView(createFooterView());
//        list.addHeaderView(createFooterView());
    }

    private ImageView img;
    private TextView number;

    private ListView list;

    private TextView createFooterView(){
        TextView footer = new TextView(this);
        AbsListView.LayoutParams abs = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Util.dp2px(60));
        abs.width = AbsListView.LayoutParams.MATCH_PARENT;
        abs.height = Util.dp2px(60);
        footer.setLayoutParams(abs);
        footer.setText("充值问题,点击联系客服");
        footer.setGravity(Gravity.CENTER);
        footer.setTextColor(0xFFA89CFF);
        footer.setBackgroundColor(Color.WHITE);
        footer.setTextSize(Util.dp2sp(12));
        addOnClickListener(footer, "contact");
        return footer;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    private class WealthAdapter extends BaseAdapter{

        private WealthAdapter(){
            data.add("10");
            data.add("20");
            data.add("30");
            data.add("40");
            data.add("50");
        }

        private List<String> data = new ArrayList<>();

        public void setData(List<String> list){
            if(list == null){
                return ;
            }
            this.data.clear();
            this.data.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.data.size();
        }

        @Override
        public Object getItem(int position) {
            return this.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_wealth, null);
            }
            final ImageView icon = (ImageView)convertView.findViewById(R.id.item_wealth_icon);
            final TextView content = (TextView)convertView.findViewById(R.id.item_wealth_content);
            final TextView tag = (TextView)convertView.findViewById(R.id.item_wealth_tag);
            final TextView submit = (TextView)convertView.findViewById(R.id.item_wealth_submit);
            content.setText(data.get(position));
            return convertView;
        }
    }
}
