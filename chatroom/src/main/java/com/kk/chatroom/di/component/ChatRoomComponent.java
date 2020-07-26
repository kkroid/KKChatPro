package com.kk.chatroom.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.kk.chatroom.di.module.ChatRoomModule;
import com.kk.chatroom.mvp.contract.ChatRoomContract;
import com.kk.chatroom.mvp.ui.activity.ChatRoomActivity;
import com.kk.chatroom.mvp.ui.fragment.ChatRoomFragment;

import dagger.BindsInstance;
import dagger.Component;


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
@Component(modules = ChatRoomModule.class, dependencies = AppComponent.class)
public interface ChatRoomComponent {
    void inject(ChatRoomActivity activity);

    void inject(ChatRoomFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChatRoomComponent.Builder view(ChatRoomContract.View view);

        ChatRoomComponent.Builder appComponent(AppComponent appComponent);

        ChatRoomComponent build();
    }
}
