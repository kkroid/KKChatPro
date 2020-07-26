package com.kk.chatpro.core.view.swiperefresh;

/**
 * 上拉加载更多
 */
public interface OnPushLoadMoreListener {
    void onLoadMore();

    /**
     * 上拉回调
     * @param percent 上拉百分比
     */
    default void onPushDistance(float percent) {
    }

    default void onPushEnable(boolean enable) {
    }
}
