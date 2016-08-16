package com.marst.recyclerview.linearlayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.marst.mxrecyclerview.MxRecyclerView;
import com.marst.mxrecyclerview.divider.DividerItemDecoration;
import com.marst.recyclerview.R;
import com.marst.recyclerview.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LinearLayoutActivity extends AppCompatActivity {

    private MxRecyclerView recyclerView;
    private RecyclerAdapter adapter;


    private List<String> dataList = new ArrayList<>();

    private int refreshTime = 0;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout);

        // initData();

        recyclerView = (MxRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        decoration.setDeviderWidth(10);
        decoration.setDividerColor(Color.RED);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingMoreEnabled(true);

        //如果布局没有设置layout_empty和layout_error两个属性则需要写下面注释的代码
     /*   ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置空布局视图
        TextView textView1 = new TextView(this);
        textView1.setText("什么都没有哦");
        textView1.setGravity(Gravity.CENTER);
        textView1.setLayoutParams(lp);
        recyclerView.setEmptyView(textView1);

        //设置加载错误布局视图
        TextView textView2 = new TextView(this);
        textView2.setText("加载失败，请下拉重试");
        textView2.setGravity(Gravity.CENTER);
        textView2.setLayoutParams(lp);
        recyclerView.setErrorView(textView2);*/

        recyclerView.setOnLoadingListener(new MxRecyclerView.OnLoadingListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshTime++;
                        dataList.clear();

                        if (refreshTime == 1) {
                            recyclerView.setRefreshError();
                        } else if (refreshTime == 2) {
                            recyclerView.setRefreshEmpty();
                        } else {
                            for (int i = 0; i < 20; i++) {
                                dataList.add("刷新第" + refreshTime + "次，" + "第" + i + "条数据");
                            }
                            recyclerView.setRefreshComplete();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            dataList.add("这是第" + dataList.size() + "条数据");
                        }
                        recyclerView.setLoadMoreComplete();
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        adapter = new RecyclerAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshing(true);
    }
}
