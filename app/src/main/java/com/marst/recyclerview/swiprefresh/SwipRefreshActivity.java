package com.marst.recyclerview.swiprefresh;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.marst.mxrecyclerview.MxRecyclerView;
import com.marst.mxrecyclerview.SwipRefreshRecyclerView;
import com.marst.mxrecyclerview.divider.DividerItemDecoration;
import com.marst.recyclerview.R;
import com.marst.recyclerview.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwipRefreshActivity extends AppCompatActivity {

    private SwipRefreshRecyclerView mSwipRefreshRecyclerView;

    private RecyclerAdapter adapter;

    private List<String> dataList = new ArrayList<>();

    private Handler handler = new Handler();

    private int refreshTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swip_refresh);

        mSwipRefreshRecyclerView = (SwipRefreshRecyclerView) findViewById(R.id.recycler_view);

        mSwipRefreshRecyclerView.setProgressViewOffset(true, 50, 150);
        mSwipRefreshRecyclerView.setColorSchemeColors(Color.GREEN, Color.YELLOW, Color.RED);

        //和MxrecyclerViewde使用方法一致
        mSwipRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipRefreshRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mSwipRefreshRecyclerView.setOnItemClickListener(new MxRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                Toast.makeText(SwipRefreshActivity.this, "click:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mSwipRefreshRecyclerView.setOnLoadingListener(new MxRecyclerView.OnLoadingListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshTime++;
                        dataList.clear();

                        if (refreshTime == 1) {
                            mSwipRefreshRecyclerView.setRefreshError();
                        } else if (refreshTime == 2) {
                            mSwipRefreshRecyclerView.setRefreshEmpty();
                        } else {
                            for (int i = 0; i < 20; i++) {
                                dataList.add("刷新第" + refreshTime + "次，" + "第" + i + "条数据");
                            }
                            mSwipRefreshRecyclerView.setRefreshComplete();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            dataList.add("这是第" + dataList.size() + "条数据");
                        }
                        mSwipRefreshRecyclerView.setLoadMoreComplete();
                        adapter.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });

        adapter = new RecyclerAdapter(this, dataList);
        mSwipRefreshRecyclerView.setAdapter(adapter);

        //setRefreshing(true) 会执行OnLoadingListener.onRefresh()方法
        mSwipRefreshRecyclerView.setRefreshing(true);
    }
}
