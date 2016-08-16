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
        //设置列表项点击事件监听
        recyclerView.setOnItemClickListener(new MxRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                Toast.makeText(ItemClickActivity.this, adapter.mDatas.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        //设置列表项长按事件监听
        recyclerView.setOnItemLongClickListener(new MxRecyclerView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View childView, int position) {
                Toast.makeText(ItemClickActivity.this, adapter.mDatas.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new RecyclerAdapter(this, dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            dataList.add("这是第" + dataList.size() + "条数据");
        }
    }
}
