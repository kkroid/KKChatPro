package com.kk.chatroom.mvp.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.di.component.AppComponent;
import com.kk.chatpro.core.app.RouterHub;
import com.kk.chatpro.core.mvp.ui.BaseKKActivity;
import com.kk.chatroom.R;
import com.kk.chatroom.R2;
import com.kk.chatroom.di.component.DaggerChatRoomComponent;
import com.kk.chatroom.mvp.contract.ChatRoomContract;
import com.kk.chatroom.mvp.model.entity.MessageItem;
import com.kk.chatroom.mvp.presenter.ChatRoomPresenter;
import com.kk.chatroom.mvp.ui.fragment.ChatRoomFragment;
import com.kk.chatroom.mvp.ui.view.ComposeView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.menu.Menu;
import cn.jiguang.imui.chatinput.menu.MenuManager;
import cn.jiguang.imui.chatinput.model.FileItem;


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

@Route(path = RouterHub.CHAT_ROOM_MAIN)
public class ChatRoomActivity extends BaseKKActivity<ChatRoomPresenter> implements ChatRoomContract.View {

    @BindView(R2.id.chat_input)
    ComposeView mChatInputView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChatRoomComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.cr_activity_chat_room; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        addFragment(R.id.container, ChatRoomFragment.newInstance());
        ChatListener chatListener = new ChatListener();
        mChatInputView.setMenuContainerHeight(819);
        MenuManager menuManager = mChatInputView.getMenuManager();
        menuManager.setMenu(Menu.newBuilder()
                .customize(true)
                .setRight(Menu.TAG_SEND)
                .setBottom(Menu.TAG_VOICE, Menu.TAG_EMOJI, Menu.TAG_GALLERY, Menu.TAG_CAMERA)
                .build());
        mChatInputView.setMenuClickListener(chatListener);
        mChatInputView.setRecordVoiceListener(chatListener);
        mChatInputView.setOnCameraCallbackListener(chatListener);
    }

    @Override
    protected void onDestroy() {
        mChatInputView.setMenuClickListener(null);
        mChatInputView.setRecordVoiceListener(null);
        mChatInputView.setOnCameraCallbackListener(null);
        super.onDestroy();
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

    private class ChatListener implements OnMenuClickListener, RecordVoiceListener, OnCameraCallbackListener {

        @Override
        public void onTakePictureCompleted(String photoPath) {

        }

        @Override
        public void onStartVideoRecord() {

        }

        @Override
        public void onFinishVideoRecord(String videoPath) {

        }

        @Override
        public void onCancelVideoRecord() {

        }

        @Override
        public boolean onSendTextMessage(CharSequence input) {
            return false;
        }

        @Override
        public void onSendFiles(List<FileItem> list) {

        }

        @Override
        public boolean switchToMicrophoneMode() {
            return true;
        }

        @Override
        public boolean switchToGalleryMode() {
            return true;
        }

        @Override
        public boolean switchToCameraMode() {
            return true;
        }

        @Override
        public boolean switchToEmojiMode() {
            return true;
        }

        @Override
        public void onStartRecord() {

        }

        @Override
        public void onFinishRecord(File voiceFile, int duration) {

        }

        @Override
        public void onCancelRecord() {

        }

        @Override
        public void onPreviewCancel() {

        }

        @Override
        public void onPreviewSend() {

        }
    }
}
