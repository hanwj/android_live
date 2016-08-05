package com.xcyo.live.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.xcyo.baselib.utils.Util;
import com.xcyo.live.R;
import com.xcyo.live.dialog.live_files.LiveFilesDialogFragment;
import com.xcyo.live.model.RoomModel;
import com.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDJ on 2016/6/13.
 */
public class AudienceAdapter extends RecyclerView.Adapter<AudienceAdapter.AudienceViewHolder>{

    public AudienceAdapter(final android.content.Context context){
        mContext = context;
    }

    private final List<ChatRoomMember> data = new ArrayList<>();
    private android.content.Context mContext;

    public void setData(List<ChatRoomMember> data) {
        if(data != null){
            this.data.clear();
            this.data.addAll(data);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public AudienceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        com.xcyo.live.view.RoundImageView roundImageView = new com.xcyo.live.view.RoundImageView(mContext);
        android.support.v7.widget.RecyclerView.LayoutParams rp = new android.support.v7.widget.RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT
                , RecyclerView.LayoutParams.WRAP_CONTENT);
        rp.width = Util.dp2px(33);
        rp.height = Util.dp2px(33);
        roundImageView.setLayoutParams(rp);
        return new AudienceViewHolder(roundImageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(AudienceViewHolder holder, int position) {
        final ImageView img = holder.getImageView();
        img.setImageResource(0);
        ChatRoomMember member = data.get(position);
        if(member != null){
            x.image().bind(img, TextUtils.isEmpty(member.getAvatar()) ? "http://file.xcyo.com/default_avatar.jpg" : member.getAvatar());
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveFilesDialogFragment live = new LiveFilesDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("files_uid", "1013387811");
                live.setArguments(bundle);
                live.show(((FragmentActivity) mContext).getSupportFragmentManager(), LiveFilesDialogFragment.class.getName());
            }
        });
    }

    static class AudienceViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder{

        public AudienceViewHolder(View itemView) {
            super(itemView);
        }

        protected  ImageView getImageView(){
            return (ImageView)itemView;
        }
    }
}