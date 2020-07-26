package com.kk.chatroom.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.kk.chatroom.R;
import com.kk.chatroom.R2;
import com.kk.chatroom.mvp.model.entity.MessageItem;

import butterknife.BindView;

public abstract class MsgLayoutHolder extends BaseHolder<MessageItem> {

    @BindView(R2.id.send_time)
    TextView mTime;
    @BindView(R2.id.avatar)
    ImageView mAvatar;
    @BindView(R2.id.content)
    View mContent;

    public MsgLayoutHolder(View itemView, boolean send) {
        super(itemView);
        RelativeLayout.LayoutParams avatarParams = (RelativeLayout.LayoutParams) mAvatar.getLayoutParams();
        RelativeLayout.LayoutParams contentParams = (RelativeLayout.LayoutParams) mContent.getLayoutParams();
        avatarParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
        contentParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        contentParams.removeRule(RelativeLayout.END_OF);
        if (send) {
            contentParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            mAvatar.setVisibility(View.GONE);
        } else {
            avatarParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            contentParams.addRule(RelativeLayout.END_OF, R.id.avatar);
            mAvatar.setVisibility(View.VISIBLE);
        }
    }
}
