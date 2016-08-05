package com.xcyo.live.activity.setting_black;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseActivity;
import com.xcyo.live.R;
import com.xcyo.live.view.swipe.SwipeLayout;
import com.xcyo.live.view.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/18.
 */
public class BlackActivity extends BaseActivity<BlackPresent> {
    @Override
    protected void initArgs() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_black);
        actionbar_back = (TextView) findViewById(R.id.base_title_back);
        actionbar_title = (TextView) findViewById(R.id.base_title_name);
        actionbar_title.setText("黑名单");

        fill = findViewById(R.id.black_fill);
        empty = findViewById(R.id.black_empty);

        list = (ListView)findViewById(R.id.black_list);
        list.setAdapter(new BlackAdapter());
    }

    private TextView actionbar_back, actionbar_title;

    private ListView list;

    private View fill, empty;

    @Override
    protected void initEvents() {
        addOnClickListener(actionbar_back, "finish");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    private void switchViewOnWindow(boolean isShowList){
        if(isShowList){
            fill.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }else{
            fill.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    private class BlackAdapter extends BaseSwipeAdapter{

        private final List<String> data = new ArrayList<>();

        public BlackAdapter(){
            data.add("1000000");
            data.add("2000000");
            data.add("3000000");
            data.add("4000000");
            data.add("5000000");
        }

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
            int count = this.data.size();
            switchViewOnWindow(count > 0);
            return count;
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
        public int getSwipeLayoutResourceId(int position) {
            return R.id.usr_black_swipe;
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            return getLayoutInflater().inflate(R.layout.item_black, null);
        }

        @Override
        public void fillValues(int position, View convertView) {
            final SwipeLayout swipe = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
            final ImageView icon = (ImageView) convertView.findViewById(R.id.usr_black_icon);
            final View icon_tip = convertView.findViewById(R.id.usr_black_tip);
            final TextView name = (TextView) convertView.findViewById(R.id.usr_black_name);
            final TextView level_tip = (TextView)convertView.findViewById(R.id.usr_black_levle_tip);
            final ImageView sex = (ImageView) convertView.findViewById(R.id.usr_black_sex);
            final TextView dis = (TextView) convertView.findViewById(R.id.usr_black_dis);
            final RelativeLayout relieve = (RelativeLayout) convertView.findViewById(R.id.usr_black_del);
            if(swipe.getOpenStatus() == SwipeLayout.Status.Open) swipe.close(false);

            relieve.setOnClickListener(getOnclick(position, swipe));
            name.setText(data.get(position));

        }

        private View.OnClickListener getOnclick(final int position, final SwipeLayout swipe){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    BlackAdapter.this.notifyDataSetChanged();
                }
            };
        }
    }
}
