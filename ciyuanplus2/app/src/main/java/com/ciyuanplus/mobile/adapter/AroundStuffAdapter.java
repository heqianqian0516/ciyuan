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
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.RoundCornersTransformation;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 周边页面  闲市  列表适配器
 */

public class AroundStuffAdapter extends RecyclerView.Adapter<AroundStuffAdapter.ViewHolder> {
    private final Activity mContext;
    private final ArrayList<FreshNewItem> FreshNewItems;
    private View.OnClickListener mItemClickListener;

    public AroundStuffAdapter(Activity mContext, ArrayList<FreshNewItem> FreshNewItems, View.OnClickListener itemClickListener) {
        this.mContext = mContext;
        this.FreshNewItems = FreshNewItems;
        this.mItemClickListener = itemClickListener;

    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public FreshNewItem getItem(int i) {
        return FreshNewItems.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_around_stuff, parent, false);
        AroundStuffAdapter.ViewHolder holder = new AroundStuffAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RequestOptions options = RequestOptions.bitmapTransform(new RoundCornersTransformation(mContext, 18,
                RoundCornersTransformation.CornerType.ALL)).placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate();
        FreshNewItem item = FreshNewItems.get(position);
        String[] images = item.imgs.split(",");
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + images[0])
                .apply(options).into(holder.imageView);
        holder.price.setText(item.price == 0.0f ? "免费" : "￥ " + item.price);
        holder.name.setText(item.title);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return FreshNewItems == null ? 0 : FreshNewItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_around_stuff_name)
        TextView name;
        @BindView(R.id.m_list_item_around_stuff_price)
        TextView price;
        @BindView(R.id.m_list_item_around_stuff_image)
        ImageView imageView;

        ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
            convertView.setOnClickListener(mItemClickListener);
        }
    }
}
