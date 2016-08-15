package com.marst.recyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 描述：瀑布流布局Adapter。
 * 作者：唐志远
 * 日期：2016年08月12日-16时19分
 */
public class StaggeredAdapter extends RecyclerView.Adapter {

    public List<String> mDatas;

    public Context mContext;

    public int height = 200;

    public StaggeredAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(2, Color.GRAY);

        TextView textView = new TextView(mContext);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundDrawable(drawable);

        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.update(mDatas.get(position), height * (position % 3 + 1));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void update(String text, int height) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            itemView.setLayoutParams(lp);
            ((TextView) itemView).setText(text);
        }
    }
}
