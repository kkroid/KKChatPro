package com.kk.chatlist.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.kk.chatlist.mvp.contract.ChatListContract;
import com.kk.chatlist.mvp.model.entity.ChatItem;
import com.kk.chatpro.core.app.RxRequestWrapper;
import com.kk.chatpro.core.mvp.model.Api;
import com.kk.chatpro.core.mvp.presenter.BaseKKPresenter;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/22/2020 08:10
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class ChatListPresenter extends BaseKKPresenter<ChatListContract.Model, ChatListContract.View> {

    @Inject
    public ChatListPresenter(ChatListContract.Model model, ChatListContract.View rootView) {
        super(model, rootView);
    }

    public void loadChatListAsync() {
        RxRequestWrapper.with(mModel.getChatList())
                .subscribeOnIoThread()
                .observeOnMainThread()
                .bindToLifecycle(mRootView)
                .callback(new Api.Callback<List<ChatItem>>() {
                    @Override
                    public void onSuccess(List<ChatItem> response) {
                        mRootView.notifyListLoaded(response);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Timber.e(throwable);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
