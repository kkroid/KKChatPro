package com.kk.chatpro.mvp.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jess.arms.di.component.AppComponent;
import com.kk.chatlist.mvp.ui.adapter.MainPageAdapter;
import com.kk.chatlist.mvp.ui.fragment.ChatListFragment;
import com.kk.chatpro.R;
import com.kk.chatpro.core.app.RouterHub;
import com.kk.chatpro.core.mvp.ui.BaseKKActivity;
import com.kk.chatpro.core.mvp.ui.BaseKKFragment;
import com.kk.chatpro.di.component.DaggerMainComponent;
import com.kk.chatpro.mvp.contract.MainContract;
import com.kk.chatpro.mvp.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/19/2020 19:09
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.APP_MAIN)
public class MainActivity extends BaseKKActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // TODO 根据模块加载情况添加menu
        Menu menu = mBottomNavigation.getMenu();
        String[] titles = new String[] {"Chat", "Contact", "Me"};
        List<BaseKKFragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            menu.add(0, i, i, titles[i]);
            MenuItem item = menu.findItem(i);
            item.setIcon(R.mipmap.ic_launcher);
            BaseKKFragment fragment = ChatListFragment.newInstance();
            fragments.add(fragment);
        }
        initFragmentManagerIfNeeded();
        MainPageAdapter mainPageAdapter = new MainPageAdapter(mFragmentManager, fragments);
        mViewPager.setAdapter(mainPageAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Timber.d("Item selected:%s", item.getItemId());
            mViewPager.setCurrentItem(item.getItemId(), true);
            return true;
        });
    }

}
