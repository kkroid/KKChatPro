package com.kk.chatlist.mvp.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.kk.chatlist.R;
import com.kk.chatlist.mvp.model.entity.ChatItem;
import com.kk.chatlist.mvp.ui.adapter.holder.ChatHolder;

import java.util.List;

public class ChatListAdapter extends DefaultAdapter<ChatItem> {

    public ChatListAdapter(List<ChatItem> chatItemList) {
        super(chatItemList);
    }

    @NonNull
    @Override
    public BaseHolder<ChatItem> getHolder(@NonNull View v, int viewType) {
        return new ChatHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.cl_item_chat;
    }

    public void updateData(List<ChatItem> data) {
        mInfos = data;
        notifyDataSetChanged();
    }
}
