package com.kk.chatlist.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.kk.chatlist.mvp.contract.ChatListContract;
import com.kk.chatlist.mvp.model.entity.ChatItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.objectbox.BoxStore;
import io.reactivex.Observable;


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
public class ChatListModel extends BaseModel implements ChatListContract.Model {
    @Inject
    BoxStore mBoxStore;

    private static final boolean USE_DUMMY_DATA = true;

    @Inject
    public ChatListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<List<ChatItem>> getChatList() {
        return Observable.create(emitter -> {
            try {
                List<ChatItem> chatItemList = mBoxStore.boxFor(ChatItem.class).getAll();
                if (USE_DUMMY_DATA) {
                    chatItemList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        ChatItem chatItem = new ChatItem();
                        chatItem.avatar = "";
                        chatItem.lastMsg = "msg " + i;
                        chatItem.lastMsgTime = System.currentTimeMillis();
                        chatItem.name = "Name " + i;
                        chatItem.unreadCount = i;
                        chatItemList.add(chatItem);
                    }
                    mBoxStore.boxFor(ChatItem.class).put(chatItemList);
                }
                emitter.onNext(chatItemList);
                emitter.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mBoxStore = null;
    }
}
