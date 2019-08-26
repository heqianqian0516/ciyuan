package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.WelfareItem;
import com.ciyuanplus.mobile.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 */

public class WelfareAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<WelfareItem> welfareList;

    public WelfareAdapter(Context mContext, ArrayList<WelfareItem> welfareList) {
        this.mContext = mContext;
        this.welfareList = welfareList;
    }


    @Override
    public int getCount() {
        return welfareList == null ? 0 : welfareList.size();
    }

    @Override
    public WelfareItem getItem(int i) {
        return welfareList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        WelfareItem item = welfareList.get(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_mine_welfare, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(item.title);
        holder.area.setText(item.giftArea);
        holder.time.setText(item.startTime);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop();
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.indexImage).apply(options).into(holder.image);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_mine_welfare_title)
        TextView title;
        @BindView(R.id.m_list_item_mine_welfare_image)
        ImageView image;
        @BindView(R.id.m_list_item_mine_welfare_area)
        TextView area;
        @BindView(R.id.m_list_item_mine_welfare_time)
        TextView time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}