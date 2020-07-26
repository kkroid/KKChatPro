package com.kk.chatlist.di.module;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.kk.chatlist.BuildConfig;
import com.kk.chatlist.mvp.contract.ChatListContract;
import com.kk.chatlist.mvp.model.ChatListModel;
import com.kk.chatlist.mvp.model.entity.MyObjectBox;

import java.io.File;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;


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
@Module
public abstract class ChatListModule {

    private static BoxStore sBoxStore;

    @Binds
    abstract ChatListContract.Model bindChatListModel(ChatListModel model);

    @ActivityScope
    @Provides
    static BoxStore provideBoxStore(Application application) {
        if (sBoxStore == null) {
            sBoxStore =  MyObjectBox.builder()
                    .androidContext(application)
                    // 将不同组件的数据库放在不同的文件夹中
                    .directory(new File(application.getFilesDir(), BuildConfig.MODULE_NAME))
                    .build();
        }
        return sBoxStore;
    }
}
