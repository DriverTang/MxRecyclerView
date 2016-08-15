package com.marst.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.marst.recyclerview.divider.DividerActivity;
import com.marst.recyclerview.gridlayout.GridLayoutActivity;
import com.marst.recyclerview.header.HeaderActivity;
import com.marst.recyclerview.itemclick.ItemClickActivity;
import com.marst.recyclerview.linearlayout.LinearLayoutActivity;
import com.marst.recyclerview.staggered.StaggeredGridActivity;
import com.marst.recyclerview.swiprefreshlayout.SwipRefreshActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView linearLayoutBtn;
    private TextView GridLayoutBtn;
    private TextView StaggeredLayoutBtn;
    private TextView SwipRefreshBtn;
    private TextView ItemClickBtn;
    private TextView headerBtn;
    private TextView dividerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayoutBtn = (TextView) findViewById(R.id.linear_btn);
        GridLayoutBtn = (TextView) findViewById(R.id.grid_btn);
        StaggeredLayoutBtn = (TextView) findViewById(R.id.staggered_btn);
        SwipRefreshBtn = (TextView) findViewById(R.id.swip_refresh_btn);
        ItemClickBtn = (TextView)findViewById(R.id.item_click_btn);
        headerBtn = (TextView)findViewById(R.id.header_btn);
        dividerBtn = (TextView)findViewById(R.id.divider_btn);

        linearLayoutBtn.setOnClickListener(this);
        GridLayoutBtn.setOnClickListener(this);
        StaggeredLayoutBtn.setOnClickListener(this);
        SwipRefreshBtn.setOnClickListener(this);
        ItemClickBtn.setOnClickListener(this);
        headerBtn.setOnClickListener(this);
        dividerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_btn:
                startActivity(new Intent(this, LinearLayoutActivity.class));
                break;
            case R.id.grid_btn:
                startActivity(new Intent(this, GridLayoutActivity.class));
                break;
            case R.id.staggered_btn:
                startActivity(new Intent(this, StaggeredGridActivity.class));
                break;
            case R.id.swip_refresh_btn:
                startActivity(new Intent(this, SwipRefreshActivity.class));
                break;
            case R.id.item_click_btn:
                startActivity(new Intent(this, ItemClickActivity.class));
                break;
            case R.id.header_btn:
                startActivity(new Intent(this, HeaderActivity.class));
                break;
            case R.id.divider_btn:
                startActivity(new Intent(this, DividerActivity.class));
                break;
            default:
                break;
        }
    }
}
