package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.LiveHoodItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 周边页面   百科列表  列表适配器
 */

public class LiveHoodAdapter extends RecyclerView.Adapter<LiveHoodAdapter.ViewHolder> {
    private final ArrayList<LiveHoodItem> liveHoodItems;
    private View.OnClickListener mItemClickListener;

    public LiveHoodAdapter(Activity mContext, ArrayList<LiveHoodItem> liveHoodItems, View.OnClickListener itemClickListener) {
        Activity context = mContext;
        this.liveHoodItems = liveHoodItems;
        this.mItemClickListener = itemClickListener;

    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public LiveHoodItem getItem(int i) {
        return liveHoodItems.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_live_hood, parent, false);
        LiveHoodAdapter.ViewHolder holder = new LiveHoodAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LiveHoodItem item = liveHoodItems.get(position);
        if (!Utils.isStringEmpty(item.name))
            holder.name.setText(item.name);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate();
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.img).apply(options).into(holder.img);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return liveHoodItems == null ? 0 : liveHoodItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_live_hood_name)
        TextView name;
        @BindView(R.id.m_list_item_live_hood_img)
        ImageView img;

        ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
        }
    }
}
