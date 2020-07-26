package com.kk.imsdk.base;

import java.util.ArrayList;
import java.util.List;

import static com.kk.imsdk.base.IResponseListenerContract.CommonEventListener;
import static com.kk.imsdk.base.IResponseListenerContract.ContactEventListener;
import static com.kk.imsdk.base.IResponseListenerContract.GroupEventListener;
import static com.kk.imsdk.base.IResponseListenerContract.MessageListener;

public abstract class BaseImRequestSender implements IImRequestSender {

    protected List<MessageListener> mMessageListeners = new ArrayList<>();
    protected List<GroupEventListener> mGroupEventListeners = new ArrayList<>();
    protected List<ContactEventListener> mContactEventListeners = new ArrayList<>();
    protected List<CommonEventListener> mCommonEventListeners = new ArrayList<>();

    @Override
    public void registerMessageListener(MessageListener listener) {
        mMessageListeners.add(listener);
    }

    @Override
    public void registerGroupEventListener(GroupEventListener listener) {
        mGroupEventListeners.add(listener);
    }

    @Override
    public void registerContactEventListener(ContactEventListener listener) {
        mContactEventListeners.add(listener);
    }

    @Override
    public void registerCommonEventListener(CommonEventListener listener) {
        mCommonEventListeners.add(listener);
    }

    @Override
    public void unregisterMessageListener(MessageListener listener) {
        mMessageListeners.remove(listener);
    }

    @Override
    public void unregisterGroupEventListener(GroupEventListener listener) {
        mGroupEventListeners.remove(listener);
    }

    @Override
    public void unregisterContactEventListener(ContactEventListener listener) {
        mContactEventListeners.remove(listener);
    }

    @Override
    public void unregisterCommonEventListener(CommonEventListener listener) {
        mCommonEventListeners.remove(listener);
    }
}
