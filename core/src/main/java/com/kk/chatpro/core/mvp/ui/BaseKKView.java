package com.kk.chatpro.core.mvp.ui;

import androidx.annotation.StringRes;

import com.jess.arms.mvp.IView;

public interface BaseKKView extends IView {

    void showMessage(@StringRes int stringId);
}
