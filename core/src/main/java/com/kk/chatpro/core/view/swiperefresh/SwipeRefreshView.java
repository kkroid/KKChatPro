package com.kk.chatpro.core.view.swiperefresh;

import android.view.View;

public interface SwipeRefreshView {
    void setState(LoadingState loadingState);

    View getView();
}
