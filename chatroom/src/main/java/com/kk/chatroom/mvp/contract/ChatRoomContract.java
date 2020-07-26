package com.kk.chatroom.mvp.contract;

import com.kk.chatpro.core.mvp.model.BaseKKModel;
import com.kk.chatpro.core.mvp.ui.BaseKKView;
import com.kk.chatroom.mvp.model.entity.MessageItem;

import java.util.List;

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
public interface ChatRoomContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseKKView {
        void onMessageLoaded(List<MessageItem> data);

        void onMessageUpdated(MessageItem item);

        void onMessageAppend(MessageItem item);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends BaseKKModel {
        Observable<List<MessageItem>> getMessageList();
    }
}
