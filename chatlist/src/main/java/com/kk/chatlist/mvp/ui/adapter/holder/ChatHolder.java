package com.kk.chatlist.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jess.arms.base.BaseHolder;
import com.kk.chatlist.R2;
import com.kk.chatlist.mvp.model.entity.ChatItem;
import com.kk.chatpro.core.util.TimeUtils;

import butterknife.BindView;

public class ChatHolder extends BaseHolder<ChatItem> {

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.time)
    TextView mTime;
    @BindView(R2.id.last_msg)
    TextView mLastMsg;
    @BindView(R2.id.unread_count)
    TextView mUnreadCount;

    public ChatHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NonNull ChatItem chatItem, int position) {
        mTitle.setText(chatItem.name);
        mTime.setText(TimeUtils.readableTime(chatItem.lastMsgTime));
        mLastMsg.setText(chatItem.lastMsg);
        int unreadCount = chatItem.unreadCount;
        if (unreadCount < 1) {
            mUnreadCount.setVisibility(View.GONE);
        } else {
            mUnreadCount.setText(gerReadableUnreadCount(unreadCount));
        }
    }

    private String gerReadableUnreadCount(int unreadCount) {
        if (unreadCount < 0) {
            return "0";
        } else if (unreadCount > 99) {
            return "99+";
        } else {
            return String.valueOf(unreadCount);
        }
    }
}
