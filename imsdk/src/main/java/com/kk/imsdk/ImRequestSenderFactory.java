package com.kk.imsdk;

import android.content.Context;

import androidx.annotation.NonNull;

import com.kk.imsdk.base.IImRequestSender;
import com.kk.imsdk.tim.TimRequestSender;

import timber.log.Timber;


public class ImRequestSenderFactory {

    private ImRequestSenderFactory() {
    }

    private static ImRequestSenderFactory sInstance;
    private IImRequestSender mRequestSender;

    public static void create(Context appContext, SdkType sdkType) {
        sInstance = new ImRequestSenderFactory();
        ConfigValidator.init(sdkType);
        switch (sdkType) {
            default:
            case Tim:
                sInstance.mRequestSender = new TimRequestSender(appContext);
                break;
            case Custom:
                sInstance.mRequestSender = new TimRequestSender(appContext);
                Timber.w("Custom sdk not dev yet, use TIM Api instead");
                break;
        }
    }

    @NonNull
    public static IImRequestSender getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("!!!! ImRequestSenderFactory Not Init Yet !!!!");
        }
        return sInstance.mRequestSender;
    }
}
