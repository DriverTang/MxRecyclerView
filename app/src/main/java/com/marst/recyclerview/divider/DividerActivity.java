package com.marst.recyclerview.divider;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.marst.mxrecyclerview.MxRecyclerView;
import com.marst.mxrecyclerview.divider.DividerItemDecoration;
import com.marst.recyclerview.R;
import com.marst.recyclerview.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DividerActivity extends AppCompatActivity {
    private MxRecyclerView recyclerView;
    private RecyclerAdapter adapter;


    private List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_click);

        initData();
        recyclerView = (MxRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);

        adapter = new RecyclerAdapter(this, dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //创建分割线对象，设置方向
        DividerItemDecoration decoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        //设置分割线颜色
        decoration.setDividerColor(Color.RED);
        //设置分割线粗细，单位是dp
        decoration.setDeviderWidth(10);
        //RecyclerView添加分割线
        recyclerView.addItemDecoration(decoration);

        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            dataList.add("这是第" + dataList.size() + "条数据");
        }
    }
}
