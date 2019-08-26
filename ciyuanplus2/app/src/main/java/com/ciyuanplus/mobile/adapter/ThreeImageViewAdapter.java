package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2018/1/5.
 */

public class ThreeImageViewAdapter extends RecyclerView.Adapter<ThreeImageViewAdapter.ViewHolder> {
    private final View.OnClickListener mItemClickListener;
    private final String[] images;

    // 最多显示9张， 如果超过9张，需要显示 More的样式

    public ThreeImageViewAdapter(Context mContext, String[] images, View.OnClickListener itemClickListener) {
        Context context = mContext;
        this.images = images;
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_nine_gride, parent, false);
        ThreeImageViewAdapter.ViewHolder holder = new ThreeImageViewAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + images[position]).apply(new RequestOptions()
                .dontAnimate().centerCrop()).into(holder.imageView);

        if (position == 8 && images.length > 9) {// 需要显示 More的样式
            holder.moreView.setVisibility(View.VISIBLE);
        } else holder.moreView.setVisibility(View.GONE);
        if (position == 0) holder.rootView.setPadding(Utils.dip2px(10), 0, 0, 0);
        else if (position == images.length - 1)
            holder.rootView.setPadding(0, 0, Utils.dip2px(10), 0);
        else holder.rootView.setPadding(0, 0, 0, 0);
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.length > 9 ? 9 : images.length;// 最多显示9张
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_view_image)
        ImageView imageView;
        @BindView(R.id.m_list_item_view_more_alert)
        TextView moreView;
        @BindView(R.id.m_list_item_view)
        View rootView;

        ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
        }
    }
}
