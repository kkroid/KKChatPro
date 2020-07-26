package com.kk.chatroom.di.module;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.kk.chatroom.BuildConfig;
import com.kk.chatroom.mvp.contract.ChatRoomContract;
import com.kk.chatroom.mvp.model.ChatRoomModel;
import com.kk.chatroom.mvp.model.entity.MyObjectBox;

import java.io.File;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;


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
@Module
public abstract class ChatRoomModule {

    private static BoxStore sBoxStore;

    @Binds
    abstract ChatRoomContract.Model bindChatRoomModel(ChatRoomModel model);

    @ActivityScope
    @Provides
    static BoxStore provideBoxStore(Application application) {
        if (sBoxStore == null) {
            sBoxStore =  MyObjectBox.builder()
                    .androidContext(application)
                    .directory(new File(application.getFilesDir(), BuildConfig.MODULE_NAME))
                    .build();
        }
        return sBoxStore;
    }
}
