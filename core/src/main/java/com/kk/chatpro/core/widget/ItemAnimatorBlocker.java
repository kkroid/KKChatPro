package com.kk.chatpro.core.widget;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public final class ItemAnimatorBlocker {
    private ItemAnimatorBlocker() {
    }

    public static void disableItemChangeAnimation(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            itemAnimator.setChangeDuration(0);
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
    }
}
