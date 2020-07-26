package com.kk.chatpro.core.mvp.presenter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.jess.arms.mvp.BasePresenter;
import com.kk.chatpro.core.mvp.model.BaseKKModel;
import com.kk.chatpro.core.mvp.ui.BaseKKView;

public abstract class BaseKKPresenter<M extends BaseKKModel, V extends BaseKKView>
        extends BasePresenter<M, V> {

    public BaseKKPresenter(M m, V v) {
        super(m, v);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onViewResumed() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStarted() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStopped() {

    }
}
