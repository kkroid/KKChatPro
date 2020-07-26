package com.kk.chatroom.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kk.chatpro.core.util.TimeUtils;
import com.kk.chatroom.R2;
import com.kk.chatroom.mvp.model.entity.MessageItem;

import butterknife.BindView;

public class TextReceiveHolder extends MsgLayoutHolder {

    @BindView(R2.id.msg_content)
    TextView mMsgContent;
    @BindView(R2.id.display_name)
    TextView mDisplayName;

    public TextReceiveHolder(View itemView) {
        super(itemView, false);
    }

    @Override
    public void setData(@NonNull MessageItem data, int position) {
        mTime.setText(TimeUtils.readableTime(data.time));
        mMsgContent.setText(data.text);
        mDisplayName.setText(data.sender);
        // TODO display avatar
    }
}
