package com.kk.chatpro.core.view.swiperefresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.jess.arms.utils.ArmsUtils;
import com.kk.chatpro.core.widget.SimpleAnimationListener;

import timber.log.Timber;

import static com.kk.chatpro.core.view.swiperefresh.LoadingState.PreLoad;
import static com.kk.chatpro.core.view.swiperefresh.LoadingState.Prepared;


/**
 * 无侵入式的下拉刷新+上拉加载更多方案，用法类似于Google原生SwipeRefreshLayout
 * 来源自： https://github.com/wangyeming/SuperSwipeRefreshLayout
 * 处理了原来代码中的所有警告，做了代码格式化和一些UI调整
 * 优化了检测滑动到底部的逻辑
 * 支持RecyclerView, ListView, ScrollView, GridView
 */
@SuppressWarnings("unused")
public class SuperSwipeRefreshLayout extends ViewGroup {

    /**
     * unit:dp
     */
    private static final int DEFAULT_HEADER_VIEW_HEIGHT = 48;
    private static final int DEFAULT_FOOTER_VIEW_HEIGHT = 48;

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;

    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int ANIMATE_TO_START_DURATION = 200;

    /**
     * SuperSwipeRefreshLayout内的目标View，比如RecyclerView,ListView,ScrollView,GridView etc.
     */
    private View mTarget;

    /**
     * 下拉刷新listener
     */
    private OnPullRefreshListener mOnPullRefreshListener;
    /**
     * 上拉加载更多
     */
    private OnPushLoadMoreListener mOnPushLoadMoreListener;
    /**
     * 实际使用的header
     */
    private SwipeRefreshView mHeader;
    /**
     * 实际使用的footer
     */
    private SwipeRefreshView mFooter;

    /**
     * 当前是否在刷新
     */
    private boolean mRefreshing = false;
    /**
     * 当前是否在加载
     */
    private boolean mLoadMore = false;
    /**
     * 最小滑动距离，当手指移动距离大于该值时，才开始处理滑动
     */
    private int mTouchSlop;
    /**
     * 触发下拉刷新的滑动临界值
     */
    private float mTotalPushDragDistance = -1;
    /**
     * 当前child距离顶部的偏离值
     */
    private int mCurrentTargetOffsetTop;
    /**
     * Whether or not the starting offset has been determined.
     * 用于标示top距离是否初始化
     */
    private boolean mOriginalOffsetCalculated = false;

    /**
     * 下拉刷新 progress 出现的位置
     */
    protected int mOriginalOffsetTop;
    /**
     * 初始触摸点的标示，用于判断滑动的合法性及计算移动距离
     */
    private int mActivePointerId = INVALID_POINTER;
    /**
     * Target is returning to its start offset because it was cancelled or a
     * refresh was triggered.
     * target是否返回到初始偏移位置, true的话不拦截不处理事件，ACTION_DOWN时重置为false
     */
    private boolean mReturningToStart;
    private final DecelerateInterpolator mDecelerateInterpolator;

    /**
     * 下拉刷新的头
     */
    private RelativeLayout mHeadViewContainer;
    /**
     * 上拉加载的头
     */
    private RelativeLayout mFooterViewContainer;

    /**
     * header的绘制index，数字越高，绘制优先级更高
     */
    private int mHeaderViewIndex = -1;
    /**
     * footer的绘制index，数字越高，绘制优先级更高
     */
    private int mFooterViewIndex = -1;

    /**
     * 下拉刷新（移动到开始位置和移动到触发位置）动画的初始值
     */
    protected int mFrom;
    /**
     * progress 回弹开始的位置
     */
    private float mSpinnerFinalOffset;
    /**
     * 是否需要通知外部下拉刷新开始
     */
    private boolean mNotify;

    private int mHeaderViewWidth;
    private int mFooterViewWidth;
    private int mHeaderViewHeight;
    private int mFooterViewHeight;

    private int mPushDistance = 0;
    /**
     * 触发上拉加载的滑动临界值
     */
    private float mTotalPullDragDistance = -1;

    /**
     * 记录下拉刷新的起始y
     */
    private float mInitialMotionY = -1;
    /**
     * 记录上拉加载的起始y
     */
    private float mInitialMotionYWithPush = -1;

    /**
     * 当前是否在上拉
     */
    private boolean mIsBeingDragged;

    /**
     * 允许上拉加载
     */
    protected boolean mPushEnable = true;

    /**
     * 允许下拉刷新
     */
    protected boolean mPullEnable = true;

    /**
     * 下拉时，超过距离之后，弹回来的动画监听器
     */
    private AnimationListener mRefreshListener = new SimpleAnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mRefreshing) {
                if (mNotify) {
                    if (mHeader != null) {
                        mHeader.setState(LoadingState.Loading);
                    }
                    if (mOnPullRefreshListener != null) {
                        mOnPullRefreshListener.onRefresh();
                    }
                }
            } else {
                mHeadViewContainer.setVisibility(View.GONE);
                setTargetOffsetTopAndBottom(
                        mOriginalOffsetTop - mCurrentTargetOffsetTop);
            }
            mCurrentTargetOffsetTop = mHeadViewContainer.getTop();
        }
    };

    /**
     * 添加头布局
     */
    public void setHeaderView(@NonNull SwipeRefreshView child) {
        mHeader = child;
        doAddSwipeRefreshView(mHeadViewContainer, child);
    }

    /**
     * 添加footer布局
     */
    public void setFooterView(@NonNull SwipeRefreshView child) {
        mFooter = child;
        doAddSwipeRefreshView(mFooterViewContainer, child);
    }

    private void doAddSwipeRefreshView(@NonNull ViewGroup container, @NonNull SwipeRefreshView child) {
        View childView;
        if ((childView = child.getView()) == null) {
            throw new IllegalArgumentException("Target view is null");
        }
        container.removeAllViews();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        container.addView(childView, layoutParams);
    }

    public void enableDefaultHeader() {
        SwipeRefreshView header = new DefaultHeader(getContext());
        setHeaderView(header);
    }

    public void enableDefaultFooter() {
        SwipeRefreshView footer = new DefaultFooter(getContext());
        setFooterView(footer);
    }

    public SuperSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public SuperSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        setWillNotDraw(false);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        int width = ArmsUtils.getScreenWidth(context);
        mHeaderViewWidth = width;
        mFooterViewWidth = width;
        mHeaderViewHeight = ArmsUtils.dip2px(context, DEFAULT_HEADER_VIEW_HEIGHT);
        mFooterViewHeight = ArmsUtils.dip2px(context, DEFAULT_FOOTER_VIEW_HEIGHT);
        createContainers();
        setChildrenDrawingOrderEnabled(true);
    }

    /**
     * 孩子节点绘制的顺序
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        // 将新添加的View,放到最后绘制
        if (mHeaderViewIndex < 0 && mFooterViewIndex < 0) {
            return i;
        }
        if (i == childCount - 2) {
            return mHeaderViewIndex;
        }
        if (i == childCount - 1) {
            return mFooterViewIndex;
        }
        int bigIndex = Math.max(mFooterViewIndex, mHeaderViewIndex);
        int smallIndex = Math.min(mFooterViewIndex, mHeaderViewIndex);
        if (i >= smallIndex && i < bigIndex - 1) {
            return i + 1;
        }
        if (i >= bigIndex || (i == bigIndex - 1)) {
            return i + 2;
        }
        return i;
    }

    private void createContainers() {
        mHeadViewContainer = new RelativeLayout(getContext());
        mHeadViewContainer.setVisibility(View.GONE);
        addView(mHeadViewContainer);

        mFooterViewContainer = new RelativeLayout(getContext());
        mFooterViewContainer.setVisibility(View.GONE);
        addView(mFooterViewContainer);
    }

    /**
     * 设置下拉刷新监听
     */
    public void setOnPullRefreshListener(OnPullRefreshListener listener) {
        mOnPullRefreshListener = listener;
    }

    /**
     * 设置上拉加载更多的接口
     */
    public void setOnPushLoadMoreListener(OnPushLoadMoreListener onPushLoadMoreListener) {
        this.mOnPushLoadMoreListener = onPushLoadMoreListener;
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        setRefreshing(refreshing, false);
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            mRefreshing = refreshing;
            if (mOnPullRefreshListener != null) {
                mOnPullRefreshListener.onPullEnable(refreshing);
            }
            if (mRefreshing) {
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshListener);
            } else {
                animateOffsetToStartPosition(mCurrentTargetOffsetTop);
            }
        }
    }

    public boolean isRefreshing() {
        return mRefreshing;
    }

    private void initTargetView() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!child.equals(mHeadViewContainer) && !child.equals(mFooterViewContainer)) {
                mTarget = child;
                break;
            }
        }
        if (mTarget == null) {
            throw new IllegalArgumentException("Child view not found");
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int moveOffset = mCurrentTargetOffsetTop - mOriginalOffsetTop;
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - moveOffset;
        initTargetView();
        mTarget.measure(
                MeasureSpec.makeMeasureSpec(
                        getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                        MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );

        mHeadViewContainer.measure(
                MeasureSpec.makeMeasureSpec(mHeaderViewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mHeaderViewHeight, MeasureSpec.EXACTLY)
        );

        mFooterViewContainer.measure(
                MeasureSpec.makeMeasureSpec(mFooterViewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mFooterViewHeight, MeasureSpec.EXACTLY)
        );

        mSpinnerFinalOffset = mHeadViewContainer.getMeasuredHeight();
        mTotalPushDragDistance = mSpinnerFinalOffset;
        mTotalPullDragDistance = mFooterViewContainer.getMeasuredHeight();

        if (!mOriginalOffsetCalculated) {
            // 如果没有初始化mOriginalOffsetTop的话
            mOriginalOffsetCalculated = true;
            mCurrentTargetOffsetTop = mOriginalOffsetTop = -mHeadViewContainer.getMeasuredHeight();
        }
        mHeaderViewIndex = -1;
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mHeadViewContainer) {
                mHeaderViewIndex = index;
                break;
            }
        }
        mFooterViewIndex = -1;
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mFooterViewContainer) {
                mFooterViewIndex = index;
                break;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        int distance = mCurrentTargetOffsetTop + mHeadViewContainer.getMeasuredHeight();

        final View child = mTarget;
        final int childLeft = getPaddingLeft();
        // 根据偏移量distance更新
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();

        // 更新目标View的位置
        child.layout(
                childLeft,
                // 如果下拉，child的top下移
                childTop + distance - mPushDistance,
                childLeft + childWidth,
                // 如果上拉，child的bottom上移
                childTop + childHeight - mPushDistance
        );

        // 更新头布局的位置
        int headViewWidth = mHeadViewContainer.getMeasuredWidth();
        int headViewHeight = mHeadViewContainer.getMeasuredHeight();
        mHeadViewContainer.layout(
                (width / 2 - headViewWidth / 2),
                mCurrentTargetOffsetTop,
                (width / 2 + headViewWidth / 2),
                mCurrentTargetOffsetTop + headViewHeight
        );
        int footViewWidth = mFooterViewContainer.getMeasuredWidth();
        int footViewHeight = mFooterViewContainer.getMeasuredHeight();
        mFooterViewContainer.layout(
                (width / 2 - footViewWidth / 2),
                height - mPushDistance,
                (width / 2 + footViewWidth / 2),
                height + footViewHeight - mPushDistance);
    }

    /**
     * 判断目标View是否滑动到顶部-还能否继续滑动
     */
    public boolean isChildScrollToTop() {
        return !mTarget.canScrollVertically(-1);
    }

    /**
     * 是否滑动到底部
     */
    public boolean isChildScrollToBottom() {
        return !mTarget.canScrollVertically(1);
    }

    /**
     * 主要判断是否应该拦截子View的事件<br>
     * 如果拦截，则交给自己的OnTouchEvent处理<br>
     * 否者，交给子View处理<br>
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }
        if (!isEnabled()
                || mReturningToStart
                || mRefreshing
                || mLoadMore
                || (!isChildScrollToTop() && !isChildScrollToBottom())) {
            // 如果子View可以滑动，不拦截事件，交给子View处理-下拉刷新
            // 或者子View没有滑动到底部不拦截事件-上拉加载更多
            return false;
        }

        if (isChildScrollToTop() && !mPullEnable) {
            // 如果用户禁止下拉刷新，则不处理事件
            Timber.i("Pull to refresh is disabled");
            return false;
        }

        if (isChildScrollToBottom() && !mPushEnable) {
            // 如果用户禁止了上拉加载，则同样不处理事件
            Timber.i("Push to load more is disabled");
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTopAndBottom(
                        mOriginalOffsetTop - mHeadViewContainer.getTop());
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                if (isChildScrollToTop()) {
                    mInitialMotionY = initialMotionY;
                }
                if (isChildScrollToBottom()) {
                    mInitialMotionYWithPush = initialMotionY;
                }
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Timber.e("Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                float yDiff;

                // 正在下拉
                if (isChildScrollToBottom()) {
                    if (mInitialMotionYWithPush == -1) {
                        //上拉的初始距离应该从此刻开始更新
                        mInitialMotionYWithPush = y;
                    }
                    // 计算上拉距离
                    yDiff = mInitialMotionYWithPush - y;
                    // 判断是否上拉的距离足够
                } else {
                    if (mInitialMotionY == -1) {
                        mInitialMotionY = y;
                    }
                    // 计算下拉距离
                    yDiff = y - mInitialMotionY;
                    // 判断是否下拉的距离足够
                }
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    // 正在上拉
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                mInitialMotionY = -1;
                mInitialMotionYWithPush = -1;
                break;
        }
        // 如果正在拖动，则拦截子View的事件
        return mIsBeingDragged;
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = ev.findPointerIndex(activePointerId);
        if (index < 0) {
            return -1;
        }
        return ev.getY(index);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean ignored) {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled()
                || mReturningToStart
                || (!isChildScrollToTop() && !isChildScrollToBottom())) {
            // 如果子View可以滑动，不拦截事件，交给子View处理
            return false;
        }

        if (isChildScrollToBottom()) {
            // 上拉加载更多
            return handlerPushTouchEvent(ev, action);
        } else {
            // 下拉刷新
            return handlerPullTouchEvent(ev, action);
        }
    }

    /**
     * 设置是否允许下拉刷新
     */
    public void setPullEnable(boolean enable) {
        mPullEnable = enable;
    }

    /**
     * 设置是否允许上拉加载
     */
    public void setPushEnable(boolean enable) {
        mPushEnable = enable;
    }

    private boolean handlerPullTouchEvent(MotionEvent ev, int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录初始点
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                // 当前下拉距离
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                if (mIsBeingDragged) {
                    // 下拉百分比
                    float originalDragPercent = overScrollTop / mTotalPushDragDistance;
                    if (originalDragPercent < 0) {
                        return false;
                    }
                    // 最小1%
                    float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
                    if (mHeader != null) {
                        mHeader.setState(dragPercent < 1 ? PreLoad : Prepared);
                    }
                    if (mOnPullRefreshListener != null) {
                        mOnPullRefreshListener.onPull(dragPercent);
                    }
                    // 距离触发下拉刷新还有多少距离
                    float extraOS = Math.abs(overScrollTop) - mTotalPushDragDistance;
                    // 回弹开始的位置
                    float slingshotDist = mSpinnerFinalOffset;
                    // 下面两行没看懂，貌似是计算
                    float tensionSlingshotPercent = Math.max(0,
                            Math.min(extraOS, slingshotDist * 2) / slingshotDist);
                    float tensionPercent = (float) ((tensionSlingshotPercent / 4)
                            - Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
                    float extraMove = (slingshotDist) * tensionPercent * 2;
                    int targetY = mOriginalOffsetTop + (int) ((slingshotDist * dragPercent) + extraMove);
                    if (mHeadViewContainer.getVisibility() != View.VISIBLE) {
                        mHeadViewContainer.setVisibility(View.VISIBLE);
                    }
                    setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float y = ev.getY(pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overScrollTop > mTotalPushDragDistance) {
                    setRefreshing(true, true);
                } else {
                    mRefreshing = false;
                    animateOffsetToStartPosition(mCurrentTargetOffsetTop);
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
        }
        return true;
    }

    /**
     * 处理上拉加载更多的Touch事件
     */
    private boolean handlerPushTouchEvent(MotionEvent ev, int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                final float y = ev.getY(pointerIndex);
                // 计算上拉的距离
                final float overScrollBottom = (mInitialMotionYWithPush - y) * DRAG_RATE;
                if (mIsBeingDragged) {
                    // 上拉百分比
                    float originalDragPercent = overScrollBottom / mTotalPullDragDistance;
                    if (originalDragPercent < 0) {
                        return false;
                    }
                    // 最小1%
                    float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
                    mPushDistance = (int) overScrollBottom;
                    updateFooterViewPosition();
                    if (mFooter != null) {
                        mFooter.setState(dragPercent < 1 ? PreLoad : Prepared);
                    }
                    if (mOnPushLoadMoreListener != null) {
                        mOnPushLoadMoreListener.onPushDistance(dragPercent);
                        mOnPushLoadMoreListener.onPushEnable(mPushDistance >= mFooterViewHeight);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float y = ev.getY(pointerIndex);
                // 松手是下拉的距离
                final float overScrollBottom = (mInitialMotionYWithPush - y) * DRAG_RATE;
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if ((overScrollBottom < mFooterViewHeight) || mOnPushLoadMoreListener == null) {
                    // 如果上拉距离不足或没有设置上拉加载监听，动画弹回
                    animatorFooterToBottom((int) overScrollBottom, 0);
                    return false;
                    // 下拉到mFooterViewHeight
                } else {
                    mPushDistance = mFooterViewHeight;
                }
                animatorFooterToBottom((int) overScrollBottom, mPushDistance);
                return false;
            }
        }
        return true;
    }

    /**
     * 松手之后，使用动画将Footer从距离start变化到end
     */
    private void animatorFooterToBottom(int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(150);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            // update
            mPushDistance = (Integer) valueAnimator1.getAnimatedValue();
            updateFooterViewPosition();
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (end > 0 && mOnPushLoadMoreListener != null) {
                    // start loading more
                    mLoadMore = true;
                    if (mFooter != null) {
                        mFooter.setState(LoadingState.Loading);
                    }
                    mOnPushLoadMoreListener.onLoadMore();
                } else {
                    resetTargetLayout();
                    mLoadMore = false;
                }
            }
        });
        valueAnimator.setInterpolator(mDecelerateInterpolator);
        valueAnimator.start();
    }

    /**
     * 设置停止加载
     */
    public void setLoadMore(boolean loadMore) {
        // 停止加载
        if (!loadMore && mLoadMore) {
            animatorFooterToBottom(mFooterViewHeight, 0);
        }
    }

    /**
     * 移动到触发下拉的位置
     */
    private void animateOffsetToCorrectPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mHeadViewContainer.clearAnimation();
        mHeadViewContainer.startAnimation(mAnimateToCorrectPosition);
        mAnimateToCorrectPosition.setAnimationListener(listener);
    }

    /**
     * 移动到开始的位置
     */
    private void animateOffsetToStartPosition(int from) {
        mFrom = from;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(ANIMATE_TO_START_DURATION);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mHeadViewContainer.clearAnimation();
        mHeadViewContainer.startAnimation(mAnimateToStartPosition);
        mAnimateToStartPosition.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                resetTargetLayout();
            }
        });
    }

    /**
     * 重置Target的位置
     */
    private void resetTargetLayout() {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = mTarget.getWidth() - getPaddingLeft() - getPaddingRight();
        final int childHeight = mTarget.getHeight() - getPaddingTop() - getPaddingBottom();
        mTarget.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        int headViewWidth = mHeadViewContainer.getMeasuredWidth();
        int headViewHeight = mHeadViewContainer.getMeasuredHeight();
        // 更新头布局的位置
        mHeadViewContainer.layout((width / 2 - headViewWidth / 2),
                -headViewHeight, (width / 2 + headViewWidth / 2), 0);
        int footViewWidth = mFooterViewContainer.getMeasuredWidth();
        int footViewHeight = mFooterViewContainer.getMeasuredHeight();
        mFooterViewContainer.layout((width / 2 - footViewWidth / 2), height,
                (width / 2 + footViewWidth / 2), height + footViewHeight);
    }

    /**
     * 下拉刷新---移动到正确的位置/
     */
    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop;
            int endTarget;
            endTarget = (int) (mSpinnerFinalOffset - Math.abs(mOriginalOffsetTop));
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mHeadViewContainer.getTop();
            setTargetOffsetTopAndBottom(offset);
        }

        @Override
        public void setAnimationListener(AnimationListener listener) {
            super.setAnimationListener(listener);
        }
    };

    private void moveToStart(float interpolatedTime) {
        int targetTop;
        targetTop = (mFrom + (int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));
        int offset = targetTop - mHeadViewContainer.getTop();
        setTargetOffsetTopAndBottom(offset);
    }

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    /**
     * 移动指示器位置
     */
    private void setTargetOffsetTopAndBottom(int offset) {
        mCurrentTargetOffsetTop = mHeadViewContainer.getTop() + offset;
        // 低版本系统上，页面刷新不及时导致下拉时，布局滞后
        if (!isInLayout()) {
            requestLayout();
        }
    }

    /**
     * 修改底部布局的位置-敏感pushDistance
     */
    private void updateFooterViewPosition() {
        mFooterViewContainer.setVisibility(View.VISIBLE);
        mFooterViewContainer.bringToFront();
        // 针对4.4及之前版本的兼容
        mFooterViewContainer.offsetTopAndBottom(-mPushDistance);
    }


    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }
}
