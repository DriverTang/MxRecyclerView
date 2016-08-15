package com.marst.recyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter
 */
public class RecyclerAdapter extends RecyclerView.Adapter {

    public List<String> mDatas;

    public Context mContext;

    public RecyclerAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

     //   GradientDrawable drawable = new GradientDrawable();
      //  drawable.setStroke(2, Color.GRAY);

        TextView textView = new TextView(mContext);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setPadding(0, 100, 0, 100);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
       // textView.setBackground(drawable);

        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.update(mDatas.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void update(String text) {
            ((TextView) itemView).setText(text);
        }
    }
}