package com.kk.imsdk.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.integration.ConfigModule;

import java.util.List;

public class ImsdkConfiguration implements ConfigModule {

    @Override
    public void applyOptions(@NonNull Context context,
                             @NonNull GlobalConfigModule.Builder builder) {
    }

    @Override
    public void injectAppLifecycle(@NonNull Context context,
                                   @NonNull List<AppLifecycles> lifecycles) {
        lifecycles.add(new ImsdkAppLifecycles());
    }

    @Override
    public void injectActivityLifecycle(@NonNull Context context,
                                        @NonNull List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new ImsdkActivityLifecycleCallback());
    }

    @Override
    public void injectFragmentLifecycle(@NonNull Context context,
                                        @NonNull List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }
}
