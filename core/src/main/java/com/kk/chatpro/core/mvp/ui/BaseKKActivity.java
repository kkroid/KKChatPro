package com.kk.chatpro.core.mvp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;
import com.kk.chatpro.core.R;
import com.kk.chatpro.core.mvp.presenter.BaseKKPresenter;
import com.kk.chatpro.core.view.LoadingDialog;

import javax.inject.Inject;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public abstract class BaseKKActivity<P extends BaseKKPresenter> extends BaseActivity<P>
        implements BaseKKView {

    // BaseKKFragment 需要一个unused inject
    // 详情：https://github.com/google/dagger/issues/955
    @Inject
    Application mApplication;

    protected LoadingDialog mLoadingDialog;
    protected FragmentManager mFragmentManager;
    private boolean mActive;

    /**
     * 默认使用event bus，框架会自动注册和注销
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void showLoading() {
        showLoading(R.string.txt_loading);
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    /**
     * 给{@link #mLoadingDialog}设置相关参数
     */
    protected void initLoadingDialog() {
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setCanceledOnTouchOutside(false);
    }

    protected void showLoading(String msg) {
        msg = msg == null ? getString(R.string.txt_loading) : msg;
        mLoadingDialog = new LoadingDialog(this, msg);
        initLoadingDialog();
        mLoadingDialog.show();
    }

    protected void showLoading(@StringRes int stringId) {
        showLoading(getString(stringId));
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void showMessage(int stringId) {
        showMessage(getString(stringId));
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActive = false;
    }

    public boolean isActive() {
        return mActive;
    }

    protected void initFragmentManagerIfNeeded() {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
    }

    protected void addFragment(BaseKKFragment fragment, String tag) {
        if (!isActive()) {
            return;
        }
        initFragmentManagerIfNeeded();
        mFragmentManager.beginTransaction().add(fragment, tag).addToBackStack(tag).commit();
    }

    protected void addFragment(int containerId, BaseKKFragment fragment) {
        if (!isActive()) {
            return;
        }
        initFragmentManagerIfNeeded();
        mFragmentManager.beginTransaction().add(containerId, fragment).commit();
    }

    protected void replaceFragment(int containerId, BaseKKFragment fragment, String tag) {
        if (!isActive()) {
            return;
        }
        initFragmentManagerIfNeeded();
        mFragmentManager.beginTransaction().replace(containerId, fragment, tag);
    }
}
