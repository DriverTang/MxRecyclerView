package com.marst.mxrecyclerview.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marst.mxrecyclerview.R;


public class LoadingMoreFooter extends LinearLayout implements BaseLoadingFooter {

    private ProgressBar progressBar;
    private TextView mText;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(R.layout.listview_footer, this, true);
        progressBar = (ProgressBar) findViewById(R.id.listview_foot_progress);
        mText = (TextView) findViewById(R.id.listview_foot_more);
    }

    @Override
    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                progressBar.setVisibility(View.VISIBLE);
                mText.setText(getContext().getText(R.string.loading));
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(getContext().getText(R.string.loading));
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(getContext().getText(R.string.loading_nomore));
                progressBar.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR:
                mText.setText(getContext().getText(R.string.loading_error));
                progressBar.setVisibility(View.GONE);
                this.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public View getFooterView() {
        return this;
    }
}
