package com.kk.imsdk.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.jess.arms.base.delegate.AppLifecycles;
import com.kk.imsdk.ImRequestSenderFactory;
import com.kk.imsdk.SdkType;

public class ImsdkAppLifecycles implements AppLifecycles {
    private SdkType mSdkType = SdkType.Tim;

    @Override
    public void attachBaseContext(@NonNull Context base) {
    }

    @Override
    public void onCreate(@NonNull Application application) {
        // 初始化IMSDK
        ImRequestSenderFactory.create(application, mSdkType);
    }

    @Override
    public void onTerminate(@NonNull Application application) {
    }
}
