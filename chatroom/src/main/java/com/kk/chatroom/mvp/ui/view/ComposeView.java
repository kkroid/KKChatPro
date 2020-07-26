package com.kk.chatroom.mvp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.kk.chatroom.R;

import cn.jiguang.imui.chatinput.ChatInputView;

public class ComposeView extends ChatInputView {

    public ComposeView(Context context) {
        this(context, null);
    }

    public ComposeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComposeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // ChatInputStyle style = getStyle();
        EditText inputView = getInputView();
        // inputView.setTextCursorDrawable(style.getInputCursorDrawable());
        int minHeight = getResources().getDimensionPixelOffset(R.dimen.dp_36);
        inputView.setMinimumHeight(minHeight);
        inputView.setMinHeight(minHeight);
        setFocusableInTouchMode(true);
    }
}
