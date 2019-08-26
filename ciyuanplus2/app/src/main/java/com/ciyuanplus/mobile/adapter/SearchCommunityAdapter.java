package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.ciyuanplus.mobile.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 * 选择小区 的adapter
 */

public class SearchCommunityAdapter extends BaseAdapter {
    private final Context mContext;
    private List<PoiItem> mList;

    public SearchCommunityAdapter(Context mContext, List<PoiItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setmList(List<PoiItem> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PoiItem getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        PoiItem item = mList.get(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_search_community, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(item.getTitle());
        holder.address.setText(item.getSnippet());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_search_community_address)
        TextView address;
        @BindView(R.id.m_list_item_search_community_name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}