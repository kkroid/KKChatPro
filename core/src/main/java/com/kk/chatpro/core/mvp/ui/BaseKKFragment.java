package com.kk.chatpro.core.mvp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.utils.ArmsUtils;
import com.kk.chatpro.core.mvp.presenter.BaseKKPresenter;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public abstract class BaseKKFragment<P extends BaseKKPresenter> extends BaseFragment<P> implements BaseKKView {

    // BaseKKFragment 需要一个unused inject
    // 详情：https://github.com/google/dagger/issues/955
    @Inject
    Application mApplication;

    @Override
    public void showMessage(int stringId) {
        showMessage(getString(stringId));
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void setData(@Nullable Object data) {
    }
}
