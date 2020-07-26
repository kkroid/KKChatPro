package com.kk.chatroom.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.kk.chatroom.mvp.contract.ChatRoomContract;
import com.kk.chatroom.mvp.model.entity.MessageItem;
import com.kk.chatroom.mvp.model.entity.MsgType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.objectbox.BoxStore;
import io.reactivex.Observable;


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
public class ChatRoomModel extends BaseModel implements ChatRoomContract.Model {

    @Inject
    BoxStore mBoxStore;

    private static final boolean USE_DUMMY_DATA = true;

    @Inject
    public ChatRoomModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<List<MessageItem>> getMessageList() {
        return Observable.create(emitter -> {
            List<MessageItem> data = mBoxStore.boxFor(MessageItem.class).getAll();
            if (USE_DUMMY_DATA) {
                if (data == null || data.isEmpty()) {
                    data = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        MessageItem item = new MessageItem();
                        item.sender = "sender 1";
                        item.sendTo = "receiver";
                        item.text = "text " + i;
                        item.type = MsgType.Text;
                        data.add(item);
                    }
                }
            }
            emitter.onNext(data);
            emitter.onComplete();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBoxStore = null;
    }
}
