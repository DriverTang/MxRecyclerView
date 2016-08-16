# MxRecyclerView #

一个RecyclerView的类库，支持上拉加载，下拉刷新，列表项点击事件监听，添加header，设置无数据和加载失败布局的显示。

## 引入 ##

compile 'com.marst:mxrecyclerview:1.0.0'

## 使用 ##

## 添加列表项点击事件监听 ##
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


## 下拉刷新 和 加载更多 ##

### 开启和禁止 ###

默认都是开启的，如果不需要此功能可以禁止
<pre><code>
        //开启下拉刷新
        recyclerView.setPullRefreshEnable(true);
        //禁止下拉刷新
        recyclerView.setPullRefreshEnable(false);
        <br/>
        //开启加载更多
        recyclerView.setLoadingMoreEnable(true);
        //禁止加载更多
        recyclerView.setLoadingMoreEnable(false);
</code></pre>

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
        recyclerView.setRefreshError();
</code></pre>
刷新成功但是没有数据：
<pre><code>
        recyclerView.setRefreshEmpty();
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

## 添加Header ##

MxRecyclerView可以添加复数个header，代码如下
<pre><code>
         recyclerView.addHeader(headerView1);
         recyclerView.addHeader(headerView2);
         recyclerView.addHeader(headerView3);
</code></pre>

## 设置空数据 和 加载失败 布局 ##
有两种方式进行添加：

### 一、在布局中添加属性，引用布局id ###
<pre><code>
        &lt;com.marst.mxrecyclerview.MxRecyclerView
               android:id="@+id/recycler_view"
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               app:layout_empty="@layout/layout_empty"
               app:layout_error="@layout/layout_error" />
</code></pre>


### 二、在代码中进行设置 ###

<pre><code>
         //设置空数据View
         recyclerView.setEmptyView(mEmptyView);
         //设置加载失败View
         recyclerView.setErrorView(mErrorView);
</code></pre>

## 添加分割线 ##
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

## 自定义refresHeader和Footer ##

如果要完全自定义下拉刷新和上拉加载的View，需要实现对应的接口

下拉刷新需要实现 BaseLoadingFooter 接口，
<pre><code>
        recyclerView.setFooterView(BaseLoadingFooter footer);
</code></pre>

上拉加载需要实现 BaseRefreshHeader 接口。
<pre><code>
        recyclerView.setRefreshHeader(BaseRefreshHeader refreshHeader);
</code></pre>

<br/>
<br/>
<br/>

## Thanks： ##
### <a href="https://github.com/jianghejie/XRecyclerView">XRecyclerView</a> ###





