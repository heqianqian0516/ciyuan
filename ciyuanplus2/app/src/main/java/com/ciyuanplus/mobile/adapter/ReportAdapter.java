package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.ReportItem;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 帖子举报的Adapter
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private final ArrayList<ReportItem> mlist;
    private final View.OnClickListener mItemClickListener;

    public ReportAdapter(Context mContext, ArrayList<ReportItem> list, View.OnClickListener mItemClickListener) {
        Context context = mContext;
        mlist = list;
        this.mItemClickListener = mItemClickListener;

    }

    public ReportItem getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post_report, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReportItem item = mlist.get(position);
        holder.checkBox.setChecked(item.isChecked);
        holder.checkBox.setText(item.name);
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> item.isChecked = b);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_post_report_check)
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
