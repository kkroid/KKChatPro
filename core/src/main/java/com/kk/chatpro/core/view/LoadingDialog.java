package com.kk.chatpro.core.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kk.chatpro.core.R;
import com.kk.chatpro.core.R2;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressWarnings("unused")
public class LoadingDialog extends Dialog {

    @BindView(R2.id.loading_msg)
    TextView mLoadingMessage;

    public LoadingDialog(@NonNull Context context) {
        this(context, null);
    }

    public LoadingDialog(@NonNull Context context, String msg) {
        super(context);
        init();
        if (msg != null) {
            mLoadingMessage.setText(msg);
        }
    }

    private void init() {
        setContentView(R.layout.view_progress_dialog);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
