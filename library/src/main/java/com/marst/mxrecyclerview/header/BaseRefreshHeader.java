package com.marst.mxrecyclerview.header;

import android.view.View;

/**
 * 描述：下拉刷新View接口，自定义时需要实现该接口的方法。
 * 作者：唐志远
 * 日期：2016年08月11日-16时26分
 */
public interface BaseRefreshHeader {

    int STATE_NORMAL = 0;
    //释放刷新
    int STATE_RELEASE_TO_REFRESH = 1;
    //正在刷新
    int STATE_REFRESHING = 2;
    //刷新完成
    int STATE_COMPLETE = 3;
    //刷新失败
    int STATE_ERROE = 4;

    /**
     * 根据偏移量移动header
     *
     * @param delta 偏移量
     */
    void onMove(float delta);

    boolean releaseAction();

    /**
     * 获取下拉刷新header的View对象
     *
     * @return
     */
    View getHeaderView();

    /**
     * 获取当前的状态
     *
     * @return
     */
    int getState();

    /**
     * 设置refreshHeader状态，需实现对各状态的处理
     *
     * @param stateCode
     */
    void setState(int stateCode);
}