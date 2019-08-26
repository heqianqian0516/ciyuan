package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.MarketSortItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 闲市 的筛选弹框
 */

public class PopupMarkteSortAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<MarketSortItem> MarketSortItems;

    public PopupMarkteSortAdapter(Context mContext, ArrayList<MarketSortItem> MarketSortItems) {
        this.mContext = mContext;
        this.MarketSortItems = MarketSortItems;

    }

    @Override
    public int getCount() {
        return MarketSortItems == null ? 0 : MarketSortItems.size();
    }

    @Override
    public MarketSortItem getItem(int i) {
        return MarketSortItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        MarketSortItem item = MarketSortItems.get(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_popup_market_sort, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (item.isSelected) {
            holder.name.setTextColor(0xfff0590e);
        } else {
            holder.name.setTextColor(0xff333333);
        }

        holder.name.setText(item.sortName);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_popup_market_name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
