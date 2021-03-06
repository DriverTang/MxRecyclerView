package com.marst.recyclerview.itemclick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.marst.mxrecyclerview.MxRecyclerView;
import com.marst.recyclerview.R;
import com.marst.recyclerview.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ItemClickActivity extends AppCompatActivity {
    private MxRecyclerView mRecyclerView;
    private RecyclerAdapter adapter;


    private List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_click);

        initData();
        mRecyclerView = (MxRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        //设置列表项点击事件监听
        mRecyclerView.setOnItemClickListener(new MxRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                Toast.makeText(ItemClickActivity.this, adapter.mDatas.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        //设置列表项长按事件监听
        mRecyclerView.setOnItemLongClickListener(new MxRecyclerView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View childView, int position) {
                Toast.makeText(ItemClickActivity.this, adapter.mDatas.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new RecyclerAdapter(this, dataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            dataList.add("这是第" + dataList.size() + "条数据");
        }
    }
}
