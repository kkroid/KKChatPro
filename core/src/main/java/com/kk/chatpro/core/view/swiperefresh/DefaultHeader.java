package com.kk.chatpro.core.view.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.utils.ThirdViewUtil;
import com.kk.chatpro.core.R;

public class DefaultHeader extends LinearLayout implements SwipeRefreshView {

    TextView mMsg;

    public DefaultHeader(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(
                R.layout.view_footer, this, true);
        ThirdViewUtil.bindTarget(this, this);
        mMsg = findViewById(R.id.footer_msg);
    }

    @Override
    public void setState(LoadingState loadingState) {
        switch (loadingState) {
            default:
            case PreLoad:
                mMsg.setText(R.string.header_preload);
                break;
            case Prepared:
                mMsg.setText(R.string.header_prepared);
                break;
            case Loading:
                mMsg.setText(R.string.header_footer_loading);
                break;
                
        }
    }

    @Override
    public View getView() {
        return this;
    }
}
