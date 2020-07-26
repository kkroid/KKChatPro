package com.kk.chatlist.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.kk.chatlist.R;
import com.kk.chatlist.R2;
import com.kk.chatlist.di.component.DaggerChatListComponent;
import com.kk.chatlist.mvp.contract.ChatListContract;
import com.kk.chatlist.mvp.model.entity.ChatItem;
import com.kk.chatlist.mvp.presenter.ChatListPresenter;
import com.kk.chatlist.mvp.ui.adapter.ChatListAdapter;
import com.kk.chatpro.core.app.RouterHub;
import com.kk.chatpro.core.mvp.ui.BaseKKFragment;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;


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
public class ChatListFragment extends BaseKKFragment<ChatListPresenter> implements ChatListContract.View {

    public static ChatListFragment newInstance() {
        return new ChatListFragment();
    }

    @BindView(R2.id.chatlist)
    RecyclerView mChatListView;

    private ChatListAdapter mChatListAdapter;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerChatListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater,
                         @Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cl_fragment_chat_list, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        assert mPresenter != null;
        mPresenter.loadChatListAsync();
    }

    @Override
    public void notifyListLoaded(List<ChatItem> data) {
        if (mChatListAdapter == null) {
            mChatListAdapter = new ChatListAdapter(data);
            mChatListView.setAdapter(mChatListAdapter);
            mChatListAdapter.setOnItemClickListener((DefaultAdapter
                    .OnRecyclerViewItemClickListener<ChatItem>) (view, viewType, chatItem, position) -> {
                Timber.i("select item:%s", chatItem.name);
                // TODO jump to ComposeActivity
                ARouter.getInstance().build(RouterHub.CHAT_ROOM_MAIN).navigation(mContext);
            });
        } else {
            mChatListAdapter.updateData(data);
        }
    }

    @Override
    public void notifyItemChanged(long id) {

    }
}
