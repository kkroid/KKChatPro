package com.kk.chatpro.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.kk.chatpro.core.R;


public class OneToolBar extends Toolbar {

    private TextView mTitle;

    public OneToolBar(Context context) {
        this(context, null);
    }

    public OneToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.toolbarStyle);
    }

    public OneToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_toolbar,
                this, true);
        mTitle = findViewById(R.id.title);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    @Override
    public void setTitle(int resId) {
        mTitle.setText(resId);
    }

    @Override
    public void setTitleTextColor(int color) {
        mTitle.setTextColor(color);
    }

    @Override
    public void setTitleTextAppearance(Context context, int resId) {
        mTitle.setTextAppearance(resId);
    }
}
