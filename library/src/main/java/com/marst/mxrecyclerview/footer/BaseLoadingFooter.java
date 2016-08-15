package com.marst.mxrecyclerview.footer;

import android.view.View;

/**
 * 描述：上拉加载View接口，自定义时需要实现该接口的方法。
 * 作者：唐志远
 * 日期：2016年08月11日-16时26分
 */
public interface BaseLoadingFooter {
    //正在加载
    int STATE_LOADING = 0;
    //加载完成
    int STATE_COMPLETE = 1;
    //没有更多
    int STATE_NOMORE = 2;
    //加载失败
    int STATE_ERROR = 3;

    /**
     * 获取上拉加载footer的view对象
     *
     * @return
     */
    View getFooterView();

    /**
     * 设置footer的状态，按需求实现对各状态的处理
     *
     * @param state
     */
    void setState(int state);
}
