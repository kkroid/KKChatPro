package com.kk.chatroom.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.NonNull;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.kk.chatroom.R;
import com.kk.chatroom.mvp.model.entity.MessageItem;
import com.kk.chatroom.mvp.model.entity.MsgType;
import com.kk.chatroom.mvp.ui.adapter.holder.FileReceiveHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.FileSendHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.GroupEventHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.ImageReceiveHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.ImageSendHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.LocationReceiveHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.LocationSendHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.MsgLayoutHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.TextReceiveHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.TextSendHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.VideoReceiveHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.VideoSendHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.VoiceReceiveHolder;
import com.kk.chatroom.mvp.ui.adapter.holder.VoiceSendHolder;

import java.util.List;


public class MessageAdapter extends DefaultAdapter<MessageItem> {

    private static final int TYPE_BASE = 100;

    private String mUserId;

    private LayoutInflater mInflater;

    public MessageAdapter(List<MessageItem> infos, String userId) {
        super(infos);
        mUserId = userId;
    }

    public void append(List<MessageItem> msgList) {
        int index = mInfos.size();
        mInfos.addAll(msgList);
        notifyItemInserted(index);
        notifyItemRangeInserted(index, msgList.size());
    }

    /**
     * viewType基于{@link MsgType} 的id
     */
    @Override
    public int getItemViewType(int position) {
        // 以msg type作为item type
        MessageItem messageItem = mInfos.get(position);
        if (isMyMessage(messageItem)) {
            return messageItem.type.id;
        } else {
            // 别人发的消息从 {@link TYPE_BASE}（100）开始计算
            return TYPE_BASE + messageItem.type.id;
        }
    }

    private boolean isMyMessage(MessageItem messageItem) {
        return mUserId.equals(messageItem.sender);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public BaseHolder<MessageItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        int tmpType = viewType > TYPE_BASE ? viewType - TYPE_BASE : viewType;
        MsgType msgType = MsgType.get(tmpType);
        Context context = parent.getContext();
        int contentId = getLayoutId(viewType);
        if (mInflater == null) {
            mInflater = LayoutInflater.from(context);
        }
        View itemView = mInflater.inflate(R.layout.cr_view_message_base_layout, parent, false);
        ViewStub viewStub = itemView.findViewById(R.id.msg_container);
        viewStub.setLayoutResource(contentId);
        viewStub.inflate();
        MsgLayoutHolder msgLayoutHolder = getHolderInternal(itemView, msgType, viewType < TYPE_BASE);
        msgLayoutHolder.setOnItemClickListener((view, position) -> {
            if (mOnItemClickListener != null && mInfos.size() > 0) {
                mOnItemClickListener.onItemClick(view, viewType, mInfos.get(position), position);
            }
        });
        return msgLayoutHolder;
    }

    private MsgLayoutHolder getHolderInternal(View itemView, MsgType type, boolean myMsg) {
        if (type == MsgType.Event) {
            return new GroupEventHolder(itemView);
        }
        if (myMsg) {
            // 我的消息
            switch (type) {
                default:
                case Text:
                    return new TextSendHolder(itemView);
                case Image:
                    return new ImageSendHolder(itemView);
                case Video:
                    return new VideoSendHolder(itemView);
                case Audio:
                    return new VoiceSendHolder(itemView);
                case File:
                    return new FileSendHolder(itemView);
                case Location:
                    return new LocationSendHolder(itemView);
            }
        } else {
            // 别人发的消息（减去TYPE_BASE后得到MsgType）
            switch (type) {
                default:
                case Text:
                    return new TextReceiveHolder(itemView);
                case Image:
                    return new ImageReceiveHolder(itemView);
                case Video:
                    return new VideoReceiveHolder(itemView);
                case Audio:
                    return new VoiceReceiveHolder(itemView);
                case File:
                    return new FileReceiveHolder(itemView);
                case Location:
                    return new LocationReceiveHolder(itemView);
            }
        }
    }

    /**
     * @deprecated onCreateViewHolder方法已被重写，此方法不会被调用
     */
    @NonNull
    @Override
    public MsgLayoutHolder getHolder(@NonNull View itemView, int viewType) {
        return getHolderInternal(itemView, MsgType.Event, false);
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType >= TYPE_BASE) {
            // 别人发的消息
            MsgType type = MsgType.get(viewType - TYPE_BASE);
            switch (type) {
                default:
                case Text:
                    return R.layout.cr_item_msg_receive_text;
                case Event:
                    return R.layout.cr_item_msg_group_event;
                case Image:
                    return R.layout.cr_item_msg_receive_image;
                case Video:
                    return R.layout.cr_item_msg_receive_video;
                case Audio:
                    return R.layout.cr_item_msg_receive_voice;
                case File:
                    return R.layout.cr_item_msg_receive_file;
                case Location:
                    return R.layout.cr_item_msg_receive_location;
            }
        } else {
            // 我的消息
            MsgType type = MsgType.get(viewType);
            switch (type) {
                default:
                case Text:
                    return R.layout.cr_item_msg_send_text;
                case Event:
                    return R.layout.cr_item_msg_group_event;
                case Image:
                    return R.layout.cr_item_msg_send_image;
                case Video:
                    return R.layout.cr_item_msg_send_video;
                case Audio:
                    return R.layout.cr_item_msg_send_voice;
                case File:
                    return R.layout.cr_item_msg_send_file;
                case Location:
                    return R.layout.cr_item_msg_send_location;
            }
        }
    }
}
