"# MxRecyclerView"

一个RecyclerView的类库，支持上拉加载，下拉刷新，列表项点击事件监听，添加header，设置无数据和加载失败布局的显示。

"## 引入"
<br />

compile 'com.marst.mxrecyclerview:mxrecyclerview:1.0.0'

"## 使用"
<br/>

"## 添加列表项点击事件"
<pre><code>
        //设置列表项点击事件监听
        recyclerView.setOnItemClickListener(new MxRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {

            }
        });
        //设置列表项长按事件监听
        recyclerView.setOnItemLongClickListener(new MxRecyclerView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View childView, int position) {

            }
        });
</code></pre>


"## 下拉刷新 和 上拉加载"

你可以对MxRecyclerView的下拉和上拉事件进行监听，代码如下：
<pre><code>
        recyclerView.setOnLoadingListener(new MxRecyclerView.OnLoadingListener() {
                @Override
                public void onRefresh() {

                }

                @Override
                public void onLoadMore() {

                }
        });
</code></pre>

对于刷新数据的结果，需要调用方法来进行通知RecyclerView，

刷新成功时：
<pre><code>
        recyclerView.setRefreshComplete();
</code></pre>
刷新失败时：
<pre><code>
        recyclerView.需调用setRefreshError();
</code></pre>
刷新成功但是没有数据：
<pre><code>
        recyclerView.需调用setRefreshEmpty();
</code></pre>


上拉加载数据也一样，需要调用不同的方法进行通知

加载成功：
<pre><code>
        recyclerView.setLoadMoreComplete();
</code></pre>
加载失败：
<pre><code>
        recyclerView.setLoadMoreError();
</code></pre>
加载成功，但是没有更多数据了：
<pre><code>
        recyclerView.setLoadingNoMore(true);
</code></pre>

"## 添加Header"

MxRecyclerView可以添加复数个header，代码如下
<pre><code>
         recyclerView.addHeader(headerView1);
         recyclerView.addHeader(headerView2);
         recyclerView.addHeader(headerView3);
</code></pre>

"## 设置空数据 和 加载失败 布局"

有两种方式进行添加：

"### 一、在布局中添加属性，引用布局id"
    <pre><code>
         <com.marst.mxrecyclerview.MxRecyclerView
               android:id="@+id/recycler_view"
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               app:layout_empty="@layout/layout_empty"
               app:layout_error="@layout/layout_error" />
    </code></pre>


"### 二、在代码中进行设置"

   <pre><code>
         //设置空数据View
         recyclerView.setEmptyView(mEmptyView);
         //设置加载失败View
         recyclerView.setErrorView(mErrorView);
    </code></pre>

"## 添加分割线"
    可以设置分割线颜色，粗细，代码如下
  <pre><code>
        //创建分割线对象，设置方向
        DividerItemDecoration decoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        //设置分割线颜色
        decoration.setDividerColor(Color.RED);
        //设置分割线粗细，单位是dp
        decoration.setDeviderWidth(10);
        //RecyclerView添加分割线
        recyclerView.addItemDecoration(decoration);
  </code></pre>


  "## 自定义refresHeader和Footer"
    如果要自定义下拉刷新和上拉加载的View，需要实现对应的接口

   下拉刷新需要实现 BaseLoadingFooter 接口
  <pre><code>
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

    /**
     * 释放刷新
     *
     * @return
     */
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
  </code></pre>

   上拉加载需要实现 BaseRefreshHeader 接口：

 <pre><code>
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
 </code></pre>


 "##最后声明，本库是在XRecyclerView的基础上完成的，

 XRecyclerView的GitHub地址：<a href="https://github.com/jianghejie/XRecyclerView">https://github.com/jianghejie/XRecyclerView</a>
 "




