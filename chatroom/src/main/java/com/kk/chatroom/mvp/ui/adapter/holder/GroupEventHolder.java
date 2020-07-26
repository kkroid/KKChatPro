package com.kk.chatroom.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kk.chatpro.core.util.TimeUtils;
import com.kk.chatroom.R2;
import com.kk.chatroom.mvp.model.entity.MessageItem;

import butterknife.BindView;

public class GroupEventHolder extends MsgLayoutHolder {

    @BindView(R2.id.event_content)
    TextView mEventContent;

    public GroupEventHolder(View itemView) {
        super(itemView, true);
    }

    @Override
    public void setData(@NonNull MessageItem data, int position) {
        mTime.setText(TimeUtils.readableTime(data.time));
        mEventContent.setText(data.text);
    }
}
