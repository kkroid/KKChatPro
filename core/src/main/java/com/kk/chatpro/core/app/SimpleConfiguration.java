package com.kk.chatpro.core.app;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.integration.ConfigModule;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * ================================================
 * 组件的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * CommonSDK 中已有 GlobalConfiguration 配置有所有组件都可公用的配置信息
 * 这里用来配置一些组件自身私有的配置信息
 *
 * @see com.jess.arms.base.delegate.AppDelegate
 * @see com.jess.arms.integration.ManifestParser
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki#3.3">ConfigModule wiki 官方文档</a>
 * Created by ArmsComponentTemplate on 07/23/2020 20:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent">Star me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent-Template">模版请保持更新</a>
 * ================================================
 */
public abstract class SimpleConfiguration implements ConfigModule {

    @Override
    public void injectAppLifecycle(@NotNull Context context,
                                   @NotNull List<AppLifecycles> lifecycles) {
    }

    @Override
    public void injectActivityLifecycle(@NotNull Context context,
                                        @NotNull List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(@NotNull Context context,
                                        @NotNull List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }
}
