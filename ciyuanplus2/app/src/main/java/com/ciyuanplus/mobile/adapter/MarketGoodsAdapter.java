package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.widget.RoundImageView;
import com.ciyuanplus.mobile.widget.ThreeGrideView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 */

class MarketGoodsAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<FreshNewItem> mGoodItems;

    public MarketGoodsAdapter(Context mContext, ArrayList<FreshNewItem> mGoodItems) {
        this.mContext = mContext;
        this.mGoodItems = mGoodItems;
    }

    @Override
    public int getCount() {
        return mGoodItems == null ? 0 : mGoodItems.size();
    }

    @Override
    public FreshNewItem getItem(int i) {
        return mGoodItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_market_goods, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_mine_like_news_time_text)
        TextView time;
        @BindView(R.id.m_list_item_mine_like_news_name_text)
        TextView name;
        @BindView(R.id.m_list_item_mine_like_news_head_image)
        RoundImageView head;
        @BindView(R.id.m_list_item_mine_like_news_title_text)
        TextView content;
        @BindView(R.id.m_list_item_mine_like_news_images)
        ThreeGrideView images;
        @BindView(R.id.m_list_item_mine_like_news_location)
        TextView location;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
