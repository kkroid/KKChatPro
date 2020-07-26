package com.kk.chatroom.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.kk.chatpro.core.app.RxRequestWrapper;
import com.kk.chatpro.core.mvp.model.Api;
import com.kk.chatpro.core.mvp.presenter.BaseKKPresenter;
import com.kk.chatroom.mvp.contract.ChatRoomContract;
import com.kk.chatroom.mvp.model.entity.MessageItem;

import java.util.List;

import javax.inject.Inject;


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
@ActivityScope
public class ChatRoomPresenter extends BaseKKPresenter<ChatRoomContract.Model, ChatRoomContract.View> {

    @Inject
    public ChatRoomPresenter(ChatRoomContract.Model model, ChatRoomContract.View rootView) {
        super(model, rootView);
    }

    public void loadMessage() {
        RxRequestWrapper.with(mModel.getMessageList())
                .bindToLifecycle(mRootView)
                .observeOnMainThread()
                .subscribeOnIoThread()
                .callback(new Api.Callback<List<MessageItem>>() {
                    @Override
                    public void onSuccess(List<MessageItem> response) {
                        mRootView.onMessageLoaded(response);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
