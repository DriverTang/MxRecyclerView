package com.marst.mxrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.marst.mxrecyclerview.footer.BaseLoadingFooter;
import com.marst.mxrecyclerview.footer.LoadingMoreFooter;
import com.marst.mxrecyclerview.header.ArrowRefreshHeader;
import com.marst.mxrecyclerview.header.BaseRefreshHeader;


/**
 * 描述：自定义RecyclerView，
 * 支持下拉刷新，上拉加载，添加header，列表项点击监听，空数据以及加载失败布局的设置。。。
 * 作者：唐志远
 * 日期：2016年08月10日-13时30分
 */
public class MxRecyclerView extends RecyclerView {

    //布局类型
    private static final int TYPE_REFRESH_HEADER = 10000;
    private static final int TYPE_FOOTER = 10001;
    private static final int TYPE_INIT = 10002;
    private static final int TYPE_EMPTY = 10003;
    private static final int TYPE_ERROR = 10004;

    //头布局类型基数，
    private static final int HEADER_INIT_INDEX = 10005;

    private static final float DRAG_RATE = 2;
    //刷新失败
    private static final int DATA_ERROR = -1;
    //刷新返回数据为空
    private static final int DATA_EMPTY = 0;

    //初始化
    private static final int DATA_INIT = 1;

    private Context mContext;
    private BaseRefreshHeader mRefreshHeader;

    private BaseLoadingFooter mFootView;
    private boolean pullRefreshEnabled = true;

    private boolean loadingMoreEnabled = true;
    private boolean isLoadingData = false;

    private boolean isLoadingNoMore = false;

    //默认初始化数据类型，不显示任何布局
    private int dataState = DATA_INIT;
    //空数据布局id
    private int mEmptyViewId;

    //加载失败布局id
    private int mErrorViewId;
    private View mEmptyView;

    private View mErrorView;

    private SparseArray<View> mHeaderViews = new SparseArray<>();

    private WrapAdapter mWrapAdapter;

    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();
    private OnItemClickListener mOnItemClickListener;

    private OnItemLongClickListener mOnItemLongClickListener;
    private OnLoadingListener mOnLoadingListener;
    private float mLastY = -1;

    public MxRecyclerView(Context context) {
        this(context, null);
    }

    public MxRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MxRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MxRecyclerView);

        mEmptyViewId = array.getResourceId(R.styleable.MxRecyclerView_layout_empty, 0);
        mErrorViewId = array.getResourceId(R.styleable.MxRecyclerView_layout_error, 0);
        array.recycle();
    }

    private void init() {
        //去掉虚影
        setOverScrollMode(View.OVER_SCROLL_NEVER);

        if (pullRefreshEnabled) {
            mRefreshHeader = new ArrowRefreshHeader(mContext);
        }
        mFootView = new LoadingMoreFooter(mContext);
        mFootView.getFooterView().setVisibility(View.GONE);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mEmptyViewId != 0) {
            mEmptyView = LayoutInflater.from(mContext).inflate(mEmptyViewId, null);
            mEmptyView.setLayoutParams(lp);
        }

        if (mErrorViewId != 0) {
            mErrorView = LayoutInflater.from(mContext).inflate(mErrorViewId, null);
            mErrorView.setLayoutParams(lp);
        }
    }

    /**
     * 设置列表项点击事件监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 设置列表项长按事件监听
     *
     * @param listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    /**
     * 设置数据加载监听
     *
     * @param listener
     */
    public void setOnLoadingListener(OnLoadingListener listener) {
        this.mOnLoadingListener = listener;
    }

    /**
     * 设置是否能下拉刷新
     *
     * @param enable true or false
     */
    public void setPullRefreshEnabled(boolean enable) {
        this.pullRefreshEnabled = enable;
    }

    /**
     * 设置是否能上拉加载
     *
     * @param enable true or false
     */
    public void setLoadingMoreEnabled(boolean enable) {
        this.loadingMoreEnabled = enable;
    }

    /**
     * 设置空数据显示的View
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    public void setEmptyView(int mEmptyViewId) {
        if (mEmptyViewId == 0) {
            throw new IllegalArgumentException("This layout ID was error!");
        }
        this.mEmptyView = LayoutInflater.from(mContext).inflate(mEmptyViewId, null);
        this.mEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mDataObserver.onChanged();
    }

    /**
     * 获取空数据显示的View
     *
     * @return
     */
    public View getEmptyView() {
        return this.mEmptyView;
    }

    /**
     * 设置加载数据失败显示的View
     *
     * @param errorView
     */
    public void setErrorView(View errorView) {
        this.mErrorView = errorView;
    }

    public void setErrorView(int mErrorViewId) {
        if (mErrorViewId == 0) {
            throw new IllegalArgumentException("This layout ID was error!");
        }
        this.mErrorView = LayoutInflater.from(mContext).inflate(mErrorViewId, null);
        this.mErrorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mDataObserver.onChanged();
    }

    /**
     * 获取加载错误显示的View
     *
     * @return
     */
    public View getErrorView() {
        return this.mErrorView;
    }

    /**
     * 设置下拉刷新头显示的view，需实现BaseRefreshHeader
     *
     * @param headerView
     */
    public void setRefreshHeader(BaseRefreshHeader headerView) {
        this.mRefreshHeader = headerView;
    }

    /**
     * 设置底部栏View
     *
     * @param footerView
     */
    public void setFooterView(BaseLoadingFooter footerView) {
        this.mFootView = footerView;
    }

    /**
     * 根据参数决定是否刷新数据，会触发刷新数据监听（如果设置了）
     *
     * @param refreshing 是否刷新
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && pullRefreshEnabled && mOnLoadingListener != null) {
            mRefreshHeader.setState(BaseRefreshHeader.STATE_REFRESHING);
            mRefreshHeader.onMove(mRefreshHeader.getHeaderView().getMeasuredHeight());
            mOnLoadingListener.onRefresh();
        }
    }

    /**
     * 刷新数据完成
     */
    public void setRefreshComplete() {
        mRefreshHeader.setState(BaseRefreshHeader.STATE_COMPLETE);
        setLoadingNoMore(false);
    }

    /**
     * 刷新数据 结果为空
     */
    public void setRefreshEmpty() {
        this.isLoadingData = false;
        this.isLoadingNoMore = true;
        this.dataState = DATA_EMPTY;

        if (pullRefreshEnabled) {
            mRefreshHeader.setState(BaseRefreshHeader.STATE_COMPLETE);
        }
        mWrapAdapter.adapter.notifyDataSetChanged();
    }

    /**
     * 刷新数据 加载失败
     */
    public void setRefreshError() {
        this.isLoadingData = false;
        this.dataState = DATA_ERROR;

        if (pullRefreshEnabled) {
            mRefreshHeader.setState(BaseRefreshHeader.STATE_ERROE);
        }
        mWrapAdapter.adapter.notifyDataSetChanged();
    }

    /**
     * 加载更多数据完成
     */
    public void setLoadMoreComplete() {
        this.isLoadingData = false;
        mFootView.setState(BaseLoadingFooter.STATE_COMPLETE);
    }

    /**
     * 加载更多数据失败
     */
    public void setLoadMoreError() {
        this.isLoadingData = false;
        mFootView.setState(BaseLoadingFooter.STATE_ERROR);
    }

    /**
     * 加载数据，是否还有更多数据
     *
     * @param loadingNoMore 是否还有数据
     */
    public void setLoadingNoMore(boolean loadingNoMore) {
        this.isLoadingData = false;
        this.isLoadingNoMore = loadingNoMore;
        mFootView.setState(isLoadingNoMore ? BaseLoadingFooter.STATE_NOMORE : BaseLoadingFooter.STATE_COMPLETE);
    }

    /**
     * 添加头布局
     *
     * @param view
     */
    public void addHeader(View view) {
        int headerIndex = HEADER_INIT_INDEX + getHeaderCount();
        mHeaderViews.append(headerIndex, view);
    }

    /**
     * 获取头布局个数
     *
     * @return
     */
    public int getHeaderCount() {
        return mHeaderViews == null ? 0 : mHeaderViews.size();
    }

    /**
     * 获取刷新头个数
     *
     * @return
     */
    public int getRefreshHeaderCount() {
        return pullRefreshEnabled ? 1 : 0;
    }

    /**
     * 获取脚个数
     *
     * @return
     */
    public int getFooterCount() {
        return loadingMoreEnabled ? 1 : 0;
    }

    /**
     * 根据头类型获取View
     *
     * @param headerType
     * @return
     */
    public View getHeaderViewByType(int headerType) {
        return mHeaderViews.get(headerType);
    }

    /**
     * 判断头类型是否存在
     *
     * @param headerType
     * @return
     */
    public boolean isHeaderType(int headerType) {
        return mHeaderViews.indexOfKey(headerType) >= 0;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPosition) {
        int min = firstPosition[0];
        for (int value : firstPosition) {
            if (value < min) {
                min = value;
            }
        }

        return min;
    }

    /**
     * 判断是滑动是否到顶部
     *
     * @return
     */
    public boolean isOnTop() {
        if (mRefreshHeader.getHeaderView().getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断adapter的数据是否为空
     *
     * @return
     */
    private boolean isDataEmpty() {
        if (mWrapAdapter != null && mWrapAdapter.adapter != null && mWrapAdapter.adapter.getItemCount() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    /**
     * 创建手势监听器，监听点击、长按事件
     */
    GestureDetector mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mOnItemClickListener != null) {
                View view = findChildViewUnder(e.getX(), e.getY());
                int position = getChildAdapterPosition(view);
                //adapter数据不为空
                //position大于等于0
                //childView不为footerView
                int relPostion = position - (getRefreshHeaderCount() + getHeaderCount());
                if (!isDataEmpty()
                        && view != mFootView.getFooterView()
                        && relPostion >= 0) {
                    mOnItemClickListener.onItemClick(view, relPostion);
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mOnItemLongClickListener != null) {
                View view = findChildViewUnder(e.getX(), e.getY());
                int position = getChildAdapterPosition(view);
                //adapter数据不为空
                //position大于等于0
                //childView不为footerView
                int relPostion = position - (getRefreshHeaderCount() + getHeaderCount());
                if (!isDataEmpty()
                        && view != mFootView.getFooterView()
                        && relPostion >= 0) {
                    mOnItemLongClickListener.onItemLongClick(view, relPostion);
                }
            }
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mGestureDetector.onTouchEvent(e)) {
            return true;
        }

        if (mLastY == -1) {
            mLastY = e.getRawY();
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = e.getRawY() - mLastY;
                mLastY = e.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getHeaderView().getMeasuredHeight() > 0 && mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1;
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mOnLoadingListener != null) {
                            mOnLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE
                && mOnLoadingListener != null
                && !isLoadingData
                && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition = getLastVisibleItemPosition();

            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && layoutManager.getItemCount() - 1 > layoutManager.getChildCount()
                    && !isLoadingNoMore
                    && mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING) {
                isLoadingData = true;
                mFootView.setState(BaseLoadingFooter.STATE_LOADING);

                mOnLoadingListener.onLoadMore();
            }
        }
    }

    /**
     * 获取首个可见item的position
     *
     * @return
     */
    public int getFirstVisibleItemPosition() {
        LayoutManager layoutManager = getLayoutManager();
        int firstVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(into);
            firstVisibleItemPosition = findMin(into);
        } else {
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }

        return firstVisibleItemPosition;
    }

    /**
     * 获取最后可见item的position
     *
     * @return
     */
    public int getLastVisibleItemPosition() {
        LayoutManager layoutManager = getLayoutManager();
        int lastVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
            lastVisibleItemPosition = findMax(into);
        } else {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        return lastVisibleItemPosition;
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    public class WrapAdapter extends Adapter {

        private Adapter adapter;

        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        /**
         * 是否为刷新头布局
         *
         * @param position
         * @return
         */
        public boolean isRefreshHeader(int position) {
            return pullRefreshEnabled && position == 0;
        }

        /**
         * 是否为头布局
         *
         * @param position
         * @return
         */
        public boolean isHeader(int position) {
            return position >= getRefreshHeaderCount() && position < getHeaderCount() + getRefreshHeaderCount();
        }

        /**
         * 是否为加载更多布局
         *
         * @param position
         * @return
         */
        public boolean isFooter(int position) {
            if (loadingMoreEnabled && adapter.getItemCount() > 0) {
                return position == getItemCount() - 1;
            } else {
                return false;
            }
        }

        /**
         * 是否为无数据布局类型
         *
         * @param position
         * @return
         */
        public boolean isEmpty(int position) {
            if (adapter.getItemCount() == 0) {
                return position == getRefreshHeaderCount() + getHeaderCount();
            }

            return false;
        }

        @Override
        public int getItemCount() {
            int refreshCount = getRefreshHeaderCount();
            int headerCount = getHeaderCount();

            int adapterCount = adapter.getItemCount();
            int emptyViewCount = adapterCount == 0 ? 1 : 0;
            int footerCount = adapterCount == 0 ? 0 : getFooterCount();

            return refreshCount + headerCount + adapterCount + emptyViewCount + footerCount;
        }

        @Override
        public int getItemViewType(int position) {
            int adapterPosition = position - (getHeaderCount() + getRefreshHeaderCount());
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }

            if (isHeader(position)) {
                int headerPosition = position - getRefreshHeaderCount();
                return HEADER_INIT_INDEX + headerPosition;
            }

            if (isFooter(position)) {
                return TYPE_FOOTER;
            }

            //根据数据状态，返回空数据类型或加载失败类型
            if (isEmpty(position)) {
                if (dataState == DATA_EMPTY) {
                    if (mEmptyView != null) {
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                    return TYPE_EMPTY;
                } else if (dataState == DATA_ERROR) {
                    if (mErrorView != null) {
                        mErrorView.setVisibility(View.VISIBLE);
                    }
                    return TYPE_ERROR;
                } else {
                    return TYPE_INIT;
                }
            }

            if (adapter != null) {
                int adapterCount = adapter.getItemCount();
                if (adapterCount > 0 && adapterPosition < adapterCount) {
                    if (mEmptyView != null && mEmptyView.getVisibility() == View.VISIBLE) {
                        mEmptyView.setVisibility(View.GONE);
                    }

                    if (mErrorView != null && mErrorView.getVisibility() == View.VISIBLE) {
                        mErrorView.setVisibility(View.GONE);
                    }

                    return adapter.getItemViewType(adapterPosition);
                }
            }
            return -1;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeaderCount() + getRefreshHeaderCount()) {
                int adapterPosition = position - (getHeaderCount() + getRefreshHeaderCount());
                if (adapterPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adapterPosition);
                }
            }
            return -1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == TYPE_REFRESH_HEADER) {
                view = mRefreshHeader.getHeaderView();
            } else if (isHeaderType(viewType)) {
                view = getHeaderViewByType(viewType);
            } else if (viewType == TYPE_FOOTER) {
                view = mFootView.getFooterView();
            } else if (viewType == TYPE_EMPTY) {
                view = mEmptyView == null ? new View(mContext) : mEmptyView;
            } else if (viewType == TYPE_ERROR) {
                view = mErrorView == null ? new View(mContext) : mErrorView;
            } else if (viewType == TYPE_INIT) {
                view = new View(mContext);
            }

            if (view != null) {
                return new SimpleViewHolder(view);
            }

            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (isHeader(position) || isRefreshHeader(position) || isEmpty(position)) {
                return;
            }

            int adapterPosition = position - (getHeaderCount() + getRefreshHeaderCount());
            if (adapter != null) {
                int adapterCount = adapter.getItemCount();
                if (adapterPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adapterPosition);
                }
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = (GridLayoutManager) layoutManager;
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position)
                                || isFooter(position)
                                || isRefreshHeader(position)
                                || isEmpty(position)) ? gridManager.getSpanCount() : 1;
                    }
                });
            }

            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition())
                    || isRefreshHeader(holder.getLayoutPosition())
                    || isFooter(holder.getLayoutPosition())
                    || isEmpty(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) lp;
                layoutParams.setFullSpan(true);
            }

            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 刷新和加载事件监听接口
     */
    public interface OnLoadingListener {
        void onRefresh();

        void onLoadMore();
    }

    /**
     * 列表项点击事件监听接口
     */
    public interface OnItemClickListener {
        void onItemClick(View childView, int position);
    }

    /**
     * 列表项点击事件
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(View childView, int position);
    }
}
