package com.kk.chatpro.core.view.swiperefresh;


/**
 * 下拉刷新回调
 */
public interface OnPullRefreshListener {
    void onRefresh();

    /**
     * 下拉回调
     * @param percent 下拉百分比
     */
    default void onPull(float percent) {
    }

    /**
     *
     * @param enable
     */
    default void onPullEnable(boolean enable) {
    }
}
