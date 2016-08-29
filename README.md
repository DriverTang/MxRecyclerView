# MxRecyclerView #

一个RecyclerView的类库，支持上拉加载，下拉刷新，列表项点击事件监听，添加header，设置无数据和加载失败布局的显示,

支持SwipRefreshLayout的下拉效果

## 引入 ##

### Gradle： ###
<pre><code>
compile 'com.marst:mxrecyclerview:1.0.3'
</code></pre>

###  Maven： ###
<pre><code>
&lt;dependency>
  &lt;groupId>com.marst&lt;/groupId>
  &lt;artifactId>mxrecyclerview&lt;/artifactId>
  &lt;version>1.0.2&lt;/version>
  &lt;type>pom&lt;/type>
&lt;/dependency>
</code></pre>

## 使用 ##

## 添加列表项点击事件监听 ##
<pre><code>
        //设置列表项点击事件监听
        mRecyclerView.setOnItemClickListener(new MxRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {

            }
        });
        //设置列表项长按事件监听
        mRecyclerView.setOnItemLongClickListener(new MxRecyclerView.OnItemLongClickListener() {
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
        mRecyclerView.setPullRefreshEnable(true);
        //禁止下拉刷新
        mRecyclerView.setPullRefreshEnable(false);
        <br/>
        //开启加载更多
        mRecyclerView.setLoadingMoreEnable(true);
        //禁止加载更多
        mRecyclerView.setLoadingMoreEnable(false);
</code></pre>

你可以对MxRecyclerView的下拉和上拉事件进行监听，代码如下：
<pre><code>
        mRecyclerView.setOnLoadingListener(new MxRecyclerView.OnLoadingListener() {
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
        mRecyclerView.setRefreshComplete();
</code></pre>
刷新失败时：
<pre><code>
        mRecyclerView.setRefreshError();
</code></pre>
刷新成功但是没有数据：
<pre><code>
        mRecyclerView.setRefreshEmpty();
</code></pre>


上拉加载数据也一样，需要调用不同的方法进行通知

加载成功：
<pre><code>
        mRecyclerView.setLoadMoreComplete();
</code></pre>
加载失败：
<pre><code>
        mRecyclerView.setLoadMoreError();
</code></pre>
加载成功，但是没有更多数据了：
<pre><code>
        mRecyclerView.setLoadingNoMore(true);
</code></pre>

## 支持SwipRefreshLayout的下拉效果 ##

SwipRefreshRecyclerView继承自SwipRefreshLayout，用法和MxRecyclerView一致

布局文件如下：
<pre><code>
       &lt;com.marst.mxrecyclerview.SwipRefreshRecyclerView
                   android:id="@+id/recycler_view"
                   android:layout_width="match_parent"
                   android:layout_height="match_parent"
                   app:layout_empty="@layout/layout_empty"
                   app:layout_error="@layout/layout_error" />
</code></pre>

为了保持用法统一，请使用setOnLoadingListener()设置监听，不要使用setOnRefreshListener()
<pre><code>
            //
            mSwipRefreshRecyclerView.setProgressViewOffset(true, 50, 150);
            mSwipRefreshRecyclerView.setColorSchemeColors(Color.GREEN, Color.YELLOW, Color.RED);

            //和MxrecyclerViewde使用方法一致
            mSwipRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mSwipRefreshRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            //设置下拉刷新和上拉加载监听
            mSwipRefreshRecyclerView.setOnLoadingListener(new MxRecyclerView.OnLoadingListener() {
                @Override
                public void onRefresh() {
                    mSwipRefreshRecyclerView.setRefreshComplete();
                }

                @Override
                public void onLoadMore() {
                    mSwipRefreshRecyclerView.setLoadMoreComplete();
                }
            });

            //setRefreshing(true) 会执行OnLoadingListener.onRefresh()方法
            mSwipRefreshRecyclerView.setRefreshing(true);
</code></pre>

## 添加Header ##

MxRecyclerView可以添加复数个header，代码如下
<pre><code>
         mRecyclerView.addHeader(headerView1);
         mRecyclerView.addHeader(headerView2);
         mRecyclerView.addHeader(headerView3);
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
         mRecyclerView.setEmptyView(mEmptyView);
         //设置加载失败View
         mRecyclerView.setErrorView(mErrorView);
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
        mRecyclerView.addItemDecoration(decoration);
</code></pre>

## 自定义refresHeader和Footer ##

如果要完全自定义下拉刷新和上拉加载的View，需要实现对应的接口

下拉刷新需要实现 BaseLoadingFooter 接口，
<pre><code>
        mRecyclerView.setFooterView(BaseLoadingFooter footer);
</code></pre>

上拉加载需要实现 BaseRefreshHeader 接口。
<pre><code>
        mRecyclerView.setRefreshHeader(BaseRefreshHeader refreshHeader);
</code></pre>

<br/>
<br/>
<br/>

## Thanks： ##
### <a href="https://github.com/jianghejie/XRecyclerView">XRecyclerView</a> ###



# License #
 Copyright DriverTang

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 <a href=" http://www.apache.org/licenses/LICENSE-2.0"> http://www.apache.org/licenses/LICENSE-2.0</a>
 <br/>
 <br/>
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.





