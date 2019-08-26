package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.SquareTagItem;
import com.ciyuanplus.mobile.utils.Constants;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 广场页面  快速进入入口  列表适配器
 */

public class QuickEnterAdapter extends RecyclerView.Adapter<QuickEnterAdapter.ViewHolder> {
    private final ArrayList<SquareTagItem> squareTagItems;
    private View.OnClickListener mItemClickListener;

    public QuickEnterAdapter(Activity mContext, ArrayList<SquareTagItem> squareTagItems, View.OnClickListener itemClickListener) {
        Activity context = mContext;
        this.squareTagItems = squareTagItems;
        this.mItemClickListener = itemClickListener;
    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public SquareTagItem getItem(int i) {
        return squareTagItems.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_quick_enter, parent, false);
        QuickEnterAdapter.ViewHolder holder = new QuickEnterAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        RequestOptions ops = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail).dontAnimate();

        SquareTagItem item = squareTagItems.get(position);
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.img).apply(ops).into(holder.img);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return squareTagItems == null ? 0 : squareTagItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_quick_enter_img)
        ImageView img;

        ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
        }
    }
}
