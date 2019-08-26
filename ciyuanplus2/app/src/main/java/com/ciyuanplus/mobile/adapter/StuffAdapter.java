package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.base.irecyclerview.IViewHolder;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.module.found.market_search.MarketSearchActivity;
import com.ciyuanplus.mobile.module.mine.my_publish.MinePublishActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 物品adapter
 */

public class StuffAdapter extends RecyclerView.Adapter<StuffAdapter.ViewHolder> {
    private final Activity mContext;
    private ArrayList<FreshNewItem> mStuffList;
    private final int mImageWidth = Utils.px2dip(Utils.getScreenWidth() / 2);
    private final View.OnClickListener mItemClickListener;

    public StuffAdapter(Activity mContext, ArrayList<FreshNewItem> mStuffList, View.OnClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mStuffList = mStuffList;
        this.mItemClickListener = mItemClickListener;
    }

    public FreshNewItem getItem(int i) {
        return mStuffList.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_stuff, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FreshNewItem item = mStuffList.get(position);

        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop();
        if (!Utils.isStringEquals((String) holder.head.getTag(R.id.glide_item_tag), item.photo)) {
            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo + Constants.IMAGE_LOAD_THUMB_END + ",w_" + mImageWidth + ",h_" + mImageWidth)
                    .apply(options).into(holder.head);
            //ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + item.photo, holder.head);
            holder.head.setTag(R.id.glide_item_tag, item.photo);
        }
        if (!Utils.isStringEmpty(item.imgs)) {
            String[] images = item.imgs.split(",");
            if (!Utils.isStringEquals((String) holder.image.getTag(R.id.glide_item_tag), images[0])) {
                Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + images[0]).apply(options).into(holder.image);
                //ImageLoaderManger.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + images[0], holder.image);
                holder.image.setTag(R.id.glide_item_tag, images[0]);
            }
        }
        if (mContext instanceof MarketSearchActivity) {
            holder.title.setText(Utils.matcherSearchTitle(0xff2279AB, item.title, ((MarketSearchActivity) mContext).mPresenter.mSearch));
        } else {
            holder.title.setText(item.title);
        }
        if (mContext instanceof OthersActivity || mContext instanceof MinePublishActivity) {
            holder.infoLayout.setVisibility(View.GONE);
        } else {
            holder.infoLayout.setVisibility(View.VISIBLE);
        }
        holder.price.setText(item.price == 0.0f ? "免费" : "￥ " + item.price);
        holder.browse.setText(Utils.formateNumber(item.browseCount));
        holder.distance.setText(Utils.formateDistance(item.distance));

        if (item.status == 7) {
            holder.shadow.setVisibility(View.VISIBLE);
            holder.shadowName.setText("已预订");
        } else if (item.status == 8) {
            holder.shadow.setVisibility(View.VISIBLE);
            holder.shadowName.setText("已售出");
        } else {
            holder.shadow.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mStuffList == null ? 0 : mStuffList.size();
    }

    class ViewHolder extends IViewHolder {
        @BindView(R.id.m_list_item_stuff_title)
        TextView title;
        @BindView(R.id.m_list_item_stuff_image)
        ImageView image;
        @BindView(R.id.m_list_item_stuff_price)
        TextView price;
        @BindView(R.id.m_list_item_stuff_head)
        ImageView head;
        @BindView(R.id.m_list_item_stuff_browse)
        TextView browse;
        @BindView(R.id.m_list_item_stuff_distance)
        TextView distance;
        @BindView(R.id.m_list_item_stuff_shadow)
        RelativeLayout shadow;
        @BindView(R.id.m_list_item_stuff_shadow_name)
        TextView shadowName;
        @BindView(R.id.m_list_item_stuff_info_lp)
        RelativeLayout infoLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
