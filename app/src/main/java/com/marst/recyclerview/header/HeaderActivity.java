package com.marst.recyclerview.header;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.marst.mxrecyclerview.MxRecyclerView;
import com.marst.recyclerview.R;
import com.marst.recyclerview.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HeaderActivity extends AppCompatActivity {
    private MxRecyclerView recyclerView;
    private RecyclerAdapter adapter;

    int refreshTime = 0;
    private List<String> dataList = new ArrayList<>();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_click);

        initData();
        recyclerView = (MxRecyclerView) findViewById(R.id.recycler_view);

        //添加Header
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(Color.RED);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 360));
        recyclerView.addHeader(imageView);

        recyclerView.setOnLoadingListener(new MxRecyclerView.OnLoadingListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshTime++;
                        dataList.clear();

                        for (int i = 0; i < 15; i++) {
                            dataList.add("刷新第" + refreshTime + "次，" + "第" + i + "条数据");
                        }
                        recyclerView.setRefreshComplete();
                        adapter.notifyDataSetChanged();

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
                        recyclerView.setRefreshComplete();
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        adapter = new RecyclerAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshing(true);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            dataList.add("这是第" + dataList.size() + "条数据");
        }
    }
}
