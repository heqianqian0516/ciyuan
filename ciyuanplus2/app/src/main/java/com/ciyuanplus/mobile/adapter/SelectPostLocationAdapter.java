package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.ciyuanplus.mobile.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2018/1/4.
 */

public class SelectPostLocationAdapter extends RecyclerView.Adapter<SelectPostLocationAdapter.ViewHolder> {
    private final View.OnClickListener mItemClickListener;

    private final ArrayList<PoiItem> mPoiList;

    public SelectPostLocationAdapter(Context mContext, ArrayList<PoiItem> mPoiList, View.OnClickListener itemClickListener) {
        Context context = mContext;
        this.mPoiList = mPoiList;
        this.mItemClickListener = itemClickListener;

    }

    public PoiItem getItem(int i) {
        return mPoiList.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_select_post_location, parent, false);
        SelectPostLocationAdapter.ViewHolder holder = new SelectPostLocationAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoiItem item = mPoiList.get(position);
        holder.name.setText(item.getTitle());
        holder.address.setText(item.getSnippet());
    }

    @Override
    public int getItemCount() {
        return mPoiList == null ? 0 : mPoiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_select_post_location_name)
        TextView name;
        @BindView(R.id.m_list_item_select_post_location_address)
        TextView address;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
