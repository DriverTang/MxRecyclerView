package com.marst.mxrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.marst.mxrecyclerview.footer.BaseLoadingFooter;

/**
 * 描述：使用SwipRefreshLayout效果的RecyclerView。
 * 作者：唐志远
 * 日期：2016年08月17日-08时55分
 */
public class SwipRefreshRecyclerView extends SwipeRefreshLayout {

    private MxRecyclerView mRecyclerView;

    private MxRecyclerView.OnLoadingListener mOnLoadingListener;
    private int mEmptyViewId;
    private int mErrorViewId;

    public SwipRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public SwipRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttrs(attrs);
        init(context);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MxRecyclerView);

        mEmptyViewId = array.getResourceId(R.styleable.MxRecyclerView_layout_empty, 0);
        mErrorViewId = array.getResourceId(R.styleable.MxRecyclerView_layout_error, 0);
        array.recycle();
    }

    private void init(Context context) {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRecyclerView = new MxRecyclerView(context);
        mRecyclerView.setLayoutParams(lp);
        mRecyclerView.setPullRefreshEnabled(false);

        if (mEmptyViewId != 0) {
            View mEmptyView = LayoutInflater.from(context).inflate(mEmptyViewId, null);
            mEmptyView.setLayoutParams(lp);
            mRecyclerView.setEmptyView(mEmptyView);
        }

        if (mErrorViewId != 0) {
            View mErrorView = LayoutInflater.from(context).inflate(mErrorViewId, null);
            mErrorView.setLayoutParams(lp);
            mRecyclerView.setErrorView(mErrorView);
        }

        addView(mRecyclerView);
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        mRecyclerView.setLoadingMoreEnabled(enabled);
    }

    public void setEmptyView(View emptyView) {
        mRecyclerView.setEmptyView(emptyView);
    }

    public void setEmptyView(int mEmptyViewId) {
        mRecyclerView.setEmptyView(mEmptyViewId);
    }

    public void setErrorView(View errorView) {
        mRecyclerView.setErrorView(errorView);
    }

    public void setErrorView(int mEmptyViewId) {
        mRecyclerView.setErrorView(mEmptyViewId);
    }

    public void setFooterView(BaseLoadingFooter footerView) {
        mRecyclerView.setFooterView(footerView);
    }

    public void addHeader(View view) {
        mRecyclerView.addHeader(view);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setOnLoadingListener(final MxRecyclerView.OnLoadingListener listener) {
        mOnLoadingListener = listener;

        this.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (listener != null) {
                    listener.onRefresh();
                }
            }
        });
        mRecyclerView.setOnLoadingListener(listener);
    }

    public void setOnItemClickListener(MxRecyclerView.OnItemClickListener listener) {
        mRecyclerView.setOnItemClickListener(listener);
    }

    public void setOnItemLongClickListener(MxRecyclerView.OnItemLongClickListener listener) {
        mRecyclerView.setOnItemLongClickListener(listener);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRecyclerView.addOnScrollListener(listener);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        if (mOnLoadingListener != null && refreshing) {
            mOnLoadingListener.onRefresh();
        }
    }

    public void setRefreshComplete() {
        mRecyclerView.setRefreshComplete();
        setRefreshing(false);
    }

    public void setRefreshEmpty() {
        mRecyclerView.setRefreshEmpty();
        setRefreshing(false);
    }

    public void setRefreshError() {
        mRecyclerView.setRefreshError();
        setRefreshing(false);
    }

    public void setLoadMoreComplete() {
        mRecyclerView.setLoadMoreComplete();
    }

    public void setLoadMoreError() {
        mRecyclerView.setLoadMoreError();
    }

    public void setLoadingNoMore(boolean loadingNoMore) {
        mRecyclerView.setLoadingNoMore(loadingNoMore);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRecyclerView.addItemDecoration(decoration);
    }
}
