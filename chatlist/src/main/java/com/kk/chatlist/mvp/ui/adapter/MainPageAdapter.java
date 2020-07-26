package com.kk.chatlist.mvp.ui.adapter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kk.chatpro.core.mvp.ui.BaseKKFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainPageAdapter extends FragmentPagerAdapter {

    private final List<BaseKKFragment> mTabs;

    public MainPageAdapter(FragmentManager fm, List<BaseKKFragment> tabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mTabs = tabs;
    }

    @NotNull
    @Override
    public BaseKKFragment getItem(int position) {
        try {
            return mTabs.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Invalid Tab");
        }
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }
}
