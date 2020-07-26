package com.kk.chatlist.mvp.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.di.component.AppComponent;
import com.kk.chatlist.R;
import com.kk.chatlist.di.component.DaggerChatListComponent;
import com.kk.chatlist.mvp.contract.ChatListContract;
import com.kk.chatlist.mvp.model.entity.ChatItem;
import com.kk.chatlist.mvp.presenter.ChatListPresenter;
import com.kk.chatlist.mvp.ui.fragment.ChatListFragment;
import com.kk.chatpro.core.mvp.ui.BaseKKActivity;

import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/22/2020 08:10
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ChatListActivity extends BaseKKActivity<ChatListPresenter> implements ChatListContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChatListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.cl_activity_chat_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        addFragment(R.id.container, ChatListFragment.newInstance());
    }

    @Override
    public void notifyListLoaded(List<ChatItem> data) {

    }

    @Override
    public void notifyItemChanged(long id) {

    }
}
