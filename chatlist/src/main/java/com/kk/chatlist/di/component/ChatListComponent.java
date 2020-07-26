package com.kk.chatlist.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.kk.chatlist.di.module.ChatListModule;
import com.kk.chatlist.mvp.contract.ChatListContract;
import com.kk.chatlist.mvp.ui.activity.ChatListActivity;
import com.kk.chatlist.mvp.ui.fragment.ChatListFragment;

import dagger.BindsInstance;
import dagger.Component;


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
@Component(modules = ChatListModule.class, dependencies = AppComponent.class)
public interface ChatListComponent {
    void inject(ChatListActivity activity);

    void inject(ChatListFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChatListComponent.Builder view(ChatListContract.View view);

        ChatListComponent.Builder appComponent(AppComponent appComponent);

        ChatListComponent build();
    }
}
