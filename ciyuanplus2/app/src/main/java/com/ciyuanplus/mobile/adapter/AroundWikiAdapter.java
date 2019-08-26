package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.WikiItem;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 周边页面   百科列表  列表适配器
 */

public class AroundWikiAdapter extends RecyclerView.Adapter<AroundWikiAdapter.ViewHolder> {
    private final ArrayList<WikiItem> wikiItems;
    private View.OnClickListener mItemClickListener;

    public AroundWikiAdapter(Activity mContext, ArrayList<WikiItem> wikiItems, View.OnClickListener itemClickListener) {
        Activity context = mContext;
        this.wikiItems = wikiItems;
        this.mItemClickListener = itemClickListener;
    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public WikiItem getItem(int i) {
        return wikiItems.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_around_wiki, parent, false);
        AroundWikiAdapter.ViewHolder holder = new AroundWikiAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WikiItem item = wikiItems.get(position);
        if (!Utils.isStringEmpty(item.name))
            holder.name.setText(item.name);
        if (!Utils.isStringEmpty(item.telephone))
            holder.phone.setText(item.telephone.replace(",", " "));
        if (!Utils.isStringEmpty(item.address))
            holder.address.setText(item.address);

        if (position == 0)
            holder.lp.setBackgroundResource(R.mipmap.find_baike_bg_1);
        else if (position == 1)
            holder.lp.setBackgroundResource(R.mipmap.find_baike_bg_2);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return wikiItems == null ? 0 : wikiItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_around_baike_name)
        TextView name;
        @BindView(R.id.m_list_item_around_baike_phone)
        TextView phone;
        @BindView(R.id.m_list_item_around_baike_address)
        TextView address;
        @BindView(R.id.m_list_item_around_baike_lp)
        RelativeLayout lp;

        ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
        }
    }
}
