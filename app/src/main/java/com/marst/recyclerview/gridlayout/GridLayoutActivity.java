package com.marst.recyclerview.gridlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.marst.mxrecyclerview.MxRecyclerView;
import com.marst.recyclerview.R;
import com.marst.recyclerview.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GridLayoutActivity extends AppCompatActivity {

    private MxRecyclerView mRecyclerView;

    private RecyclerAdapter adapter;

    private List<String> dataList = new ArrayList<>();

    private Handler handler = new Handler();
    private int refreshTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_layout);

        mRecyclerView = (MxRecyclerView) findViewById(R.id.recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        //刷新和加载更多默认为true
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);

        mRecyclerView.setOnLoadingListener(new MxRecyclerView.OnLoadingListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshTime++;
                        dataList.clear();

                        if (refreshTime == 1) {
                            mRecyclerView.setRefreshError();
                        } else if (refreshTime == 2) {
                            mRecyclerView.setRefreshEmpty();
                        } else {
                            for (int i = 0; i < 20; i++) {
                                dataList.add("刷新第" + refreshTime + "次，" + "第" + i + "条数据");
                            }
                            mRecyclerView.setRefreshComplete();
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
                        mRecyclerView.setLoadMoreComplete();
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        adapter = new RecyclerAdapter(this, dataList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRefreshing(true);
    }
}
