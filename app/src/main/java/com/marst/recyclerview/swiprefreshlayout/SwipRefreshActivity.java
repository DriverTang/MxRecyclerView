package com.marst.recyclerview.swiprefreshlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.marst.mxrecyclerview.MxRecyclerView;
import com.marst.recyclerview.R;
import com.marst.recyclerview.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwipRefreshActivity extends AppCompatActivity {
    private MxRecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;


    private List<String> dataList = new ArrayList<>();
    private RecyclerAdapter adapter;
    private int refreshTime;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swip_refresh);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setProgressViewOffset(true, 20, 120);

        recyclerView = (MxRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //如果不使用自身的下拉刷新请设置false
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setOnLoadingListener(new MxRecyclerView.OnLoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
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

        //使用SwipRefreshLayout的下拉刷新监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                dataList.clear();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            dataList.add("刷新第" + refreshTime + "次，" + "第" + i + "条数据");
                        }
                        refreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        initData();
        adapter = new RecyclerAdapter(this, dataList);

        recyclerView.setAdapter(adapter);

        //注意
        refreshLayout.setRefreshing(false);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            dataList.add("这是第" + dataList.size() + "条数据");
        }
    }
}
