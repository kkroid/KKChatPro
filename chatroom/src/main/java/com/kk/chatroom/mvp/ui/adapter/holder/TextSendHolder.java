package com.kk.chatroom.mvp.ui.adapter.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kk.chatpro.core.util.TimeUtils;
import com.kk.chatroom.R2;
import com.kk.chatroom.mvp.model.entity.MessageItem;
import com.kk.chatroom.mvp.model.entity.MsgStatus;

import butterknife.BindView;

public class TextSendHolder extends MsgLayoutHolder {


    @BindView(R2.id.msg_content)
    TextView mMsgContent;
    @BindView(R2.id.icon_sending)
    ImageView mSending;
    @BindView(R2.id.text_receipt)
    TextView mReceipt;
    @BindView(R2.id.btn_resend)
    ImageButton mResend;

    public TextSendHolder(View itemView) {
        super(itemView, true);
    }

    @Override
    public void setData(@NonNull MessageItem data, int position) {
        mTime.setText(TimeUtils.readableTime(data.time));
        mMsgContent.setText(data.text);
        // TODO display avatar
        showMsgStatus(data);
    }

    private void showMsgStatus(MessageItem data) {
        switch (data.status) {
            case Sending:
                mSending.setVisibility(View.VISIBLE);
                mReceipt.setVisibility(View.GONE);
                mResend.setVisibility(View.GONE);
                break;
            case Read:
                mSending.setVisibility(View.GONE);
                mReceipt.setVisibility(View.VISIBLE);
                mResend.setVisibility(View.GONE);
                break;
            case SendFailed:
                mSending.setVisibility(View.GONE);
                mReceipt.setVisibility(View.GONE);
                mResend.setVisibility(View.VISIBLE);
                break;
            default:
            case Sent:
                mSending.setVisibility(View.GONE);
                mReceipt.setVisibility(View.GONE);
                mResend.setVisibility(View.GONE);
                break;
        }

        if (data.status == MsgStatus.Sending) {
            mSending.setVisibility(View.VISIBLE);
            mReceipt.setVisibility(View.GONE);
        } else {
            mSending.setVisibility(View.GONE);
        }
    }
}
