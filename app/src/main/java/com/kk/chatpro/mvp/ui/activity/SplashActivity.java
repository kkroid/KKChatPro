package com.kk.chatpro.mvp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jess.arms.di.component.AppComponent;
import com.kk.chatpro.R;
import com.kk.chatpro.core.app.RouterHub;
import com.kk.chatpro.core.app.RxRequestWrapper;
import com.kk.chatpro.core.mvp.ui.BaseKKActivity;
import com.kk.chatpro.di.component.DaggerSplashComponent;
import com.kk.chatpro.mvp.contract.SplashContract;
import com.kk.chatpro.mvp.presenter.SplashPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import timber.log.Timber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/19/2020 16:29
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SplashActivity extends BaseKKActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.loading)
    TextView mTextViewLoading;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSplashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_splash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        showLoading();
        RxRequestWrapper.with(Observable.timer(2, TimeUnit.SECONDS))
                .observeOnMainThread()
                .subscribeOnIoThread()
                .bindToLifecycle(this)
                .callback(response -> {
                    hideLoading();
                    Timber.d("Jump to chatlist page");
                    ARouter.getInstance().build(RouterHub.APP_MAIN).navigation(this);
                    finish();
                });
    }

    @Override
    public void showLoading() {
        mTextViewLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mTextViewLoading.setVisibility(View.INVISIBLE);
    }
}
