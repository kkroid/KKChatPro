package com.kk.chatlist.app;

import android.content.Context;

import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.GlobalConfigModule;
import com.kk.chatpro.core.app.SimpleConfiguration;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

/**
 * ================================================
 * 组件的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * CommonSDK 中已有 GlobalConfiguration 配置有所有组件都可公用的配置信息
 * 这里用来配置一些组件自身私有的配置信息
 *
 * @see com.jess.arms.base.delegate.AppDelegate
 * @see com.jess.arms.integration.ManifestParser
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki#3.3">ConfigModule wiki 官方文档</a>
 * Created by ArmsComponentTemplate on 07/09/2020 23:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent">Star me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent-Template">模版请保持更新</a>
 * ================================================
 */
public final class ChatListConfiguration extends SimpleConfiguration {

    @Override
    public void applyOptions(@NotNull Context context, @NotNull GlobalConfigModule.Builder builder) {
        RetrofitUrlManager.getInstance().putDomain("ChatList", "https://www.baidu.com");
    }

    @Override
    public void injectAppLifecycle(@NotNull Context context, List<AppLifecycles> lifecycles) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecyclesImpl());
    }
}
