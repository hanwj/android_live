package com.xcyo.live.dialog.live_gift;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcyo.baselib.server.ServerBinderData;
import com.xcyo.baselib.ui.BaseDialogFragment;
import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;
import com.xcyo.live.adapter.LiveGiftPagerAdapter;
import com.xcyo.live.view.RecipNumberProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/15.
 */
public class LiveGiftDialogFragment extends BaseDialogFragment<LiveGiftDialogPresent> {

    public LiveGiftDialogFragment setGiftCallBack(LiveGiftDialogPresent.OnGiftCallBack giftCallBack){
        this.giftCallBack = giftCallBack;
        return this;
    }

    @Override
    protected void initArgs() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams ll = getDialog().getWindow().getAttributes();
        ll.alpha = 1.0f;
        ll.dimAmount = 0.1f;
        ll.gravity = Gravity.BOTTOM;
        ll.width = getResources().getDisplayMetrics().widthPixels;
        getDialog().getWindow().setAttributes(ll);
        setCancelable(true);
        super.onResume();

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container) {
        View convertView = inflater.inflate(R.layout.dialog_live_gift, null);

        pager = (ViewPager)convertView.findViewById(R.id.live_gift_pager);
        send = (TextView) convertView.findViewById(R.id.live_gift_recharge_send);
        coin = (TextView) convertView.findViewById(R.id.live_gift_recharge_coin);
        img = (ImageView) convertView.findViewById(R.id.live_gift_recharge_img);

        recip = (com.xcyo.live.view.RecipNumberProgressBar) convertView.findViewById(R.id.live_gift_recharge_recip);
        recip.setEcho(getEchoTimer());
        recip.setVisibility(View.GONE);
        LiveGiftPagerAdapter adapter = new LiveGiftPagerAdapter(this.pager);
        this.pager.setAdapter(adapter);

        List<String> data = new ArrayList<>();
        for(int i = 0; i<27; i++){
            data.add(i + "00");
        }
        adapter.setView(getPagerChild(data));
        return convertView;
    }

    private ViewPager pager;

    private TextView send;
    private TextView coin;
    private ImageView img;

    private com.xcyo.live.view.RecipNumberProgressBar recip;

    private static final int MAX_GIFT_ITEM = 8;

    private LiveGiftDialogPresent.OnGiftCallBack giftCallBack = null;

    @Override
    protected void initEvents() {
        addOnClickListener(send, "send");
        addOnClickListener(recip, "dbhit");
    }

    @Override
    public void onServerCallback(String evt, ServerBinderData data) {

    }

    protected  void recipStart(){
        recip.start();
    }

    protected void resetTimer(){
        if(giftCallBack != null){
            giftCallBack.gift();
        }
    }

    private RecipNumberProgressBar.OnEchoTimerTask getEchoTimer(){
        return new RecipNumberProgressBar.OnEchoTimerTask() {
            @Override
            public void onStart() {
                recip.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRunning(RecipNumberProgressBar.STATE stat, int max, int current) {}

            @Override
            public void onStop() {
                recip.setVisibility(View.GONE);
            }
        };
    }

    protected Object getItemViewStream(){
        GridView gv = (GridView)((LiveGiftPagerAdapter)pager.getAdapter()).getCurrentItemPostion();
        if(gv != null){
            return ((ChildAdapter)gv.getAdapter()).getSelectItem();
        }
        return null;
    }


    private List<View> getPagerChild(final List<String> data){
        if(data == null){
            return null;
        }
        int max_size = 0; List<View> av = new ArrayList<>();
        if(data.size() % MAX_GIFT_ITEM == 0){
            max_size = data.size() / MAX_GIFT_ITEM;
        }else {
            max_size = 1 + data.size() / MAX_GIFT_ITEM;
        }
        for(int i = 0; i < max_size; i++){
            GridView g = new GridView(getActivity());
            ViewGroup.LayoutParams vgp =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            vgp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            vgp.height = Util.dp2px(160);
            g.setLayoutParams(vgp);
            g.setNumColumns(MAX_GIFT_ITEM / 2);
            g.setHorizontalSpacing(1);
            g.setVerticalSpacing(1);
            g.setSelector(new ColorDrawable(Color.TRANSPARENT));
            g.setAdapter(new ChildAdapter(data.subList(MAX_GIFT_ITEM * i, i == max_size - 1 ? data.size() : MAX_GIFT_ITEM * (i + 1))));
            av.add(g);
        }
        return av;
    }

    private class ChildAdapter extends BaseAdapter{

        private List<String> d;

        private final int[] selectPosition = new int[]{-1};

        public ChildAdapter(List<String> list){
            this.d = list;
        }

        @Override
        public int getCount() {
            return this.d.size();
        }

        @Override
        public Object getItem(int position) {
            return this.d.get(position);
        }

        public Object getSelectItem(){
            if(selectPosition[0] == -1){
                return null;
            }
            return d.get(this.selectPosition[0]);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_gift, null);

            final ImageView icon = (ImageView)convertView.findViewById(R.id.item_gift_icon);
            final ImageView tip = (ImageView)convertView.findViewById(R.id.item_gift_tip);
            final TextView coin = (TextView)convertView.findViewById(R.id.item_gift_coin);
            final ImageView coin_img = (ImageView)convertView.findViewById(R.id.item_gift_coin_img);
            final TextView disc = (TextView)convertView.findViewById(R.id.item_gift_disc);
            disc.setText(d.get(position));
            convertView.setOnClickListener(getOnClickListener(position));
            if(selectPosition[0] == position){
                convertView.setSelected(true);
            }else{
                convertView.setSelected(false);
            }
            return convertView;
        }

        private View.OnClickListener getOnClickListener(final int position){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPosition[0] = position;
                    ChildAdapter.this.notifyDataSetChanged();
                }
            };
        }
    }

}
