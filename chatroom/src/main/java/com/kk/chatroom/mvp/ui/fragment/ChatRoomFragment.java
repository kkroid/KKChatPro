package com.kk.chatroom.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.di.component.AppComponent;
import com.kk.chatpro.core.mvp.ui.BaseKKFragment;
import com.kk.chatpro.core.widget.ItemAnimatorBlocker;
import com.kk.chatroom.R;
import com.kk.chatroom.R2;
import com.kk.chatroom.di.component.DaggerChatRoomComponent;
import com.kk.chatroom.mvp.contract.ChatRoomContract;
import com.kk.chatroom.mvp.model.entity.MessageItem;
import com.kk.chatroom.mvp.presenter.ChatRoomPresenter;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/23/2020 20:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ChatRoomFragment extends BaseKKFragment<ChatRoomPresenter> implements ChatRoomContract.View {

    @BindView(R2.id.message_list)
    RecyclerView mMessageListView;

    public static ChatRoomFragment newInstance() {
        ChatRoomFragment fragment = new ChatRoomFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerChatRoomComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cr_fragment_chat_room, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ItemAnimatorBlocker.disableItemChangeAnimation(mMessageListView);
        // 使消息从下往上顶，即当只有一条消息时，显示在最下方
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mMessageListView.getLayoutManager();
        Objects.requireNonNull(linearLayoutManager).setStackFromEnd(true);
    }

    @Override
    public void onMessageLoaded(List<MessageItem> data) {

    }

    @Override
    public void onMessageUpdated(MessageItem item) {

    }

    @Override
    public void onMessageAppend(MessageItem item) {

    }
}
