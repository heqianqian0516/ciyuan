package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.base.irecyclerview.IViewHolder;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity;
import com.ciyuanplus.mobile.module.home.activity.RankDetailActivity;
import com.ciyuanplus.mobile.module.mine.stuff.MineLikeFragment;
import com.ciyuanplus.mobile.net.bean.RankListItem;
import com.ciyuanplus.mobile.net.bean.RankedFirstBean;
import com.ciyuanplus.mobile.net.bean.RankedFirstItem;
import com.ciyuanplus.mobile.net.bean.RankedListBean;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankedFirstAdapter extends RecyclerView.Adapter<RankedFirstAdapter.ViewHolder> {
    private final Activity mContext;
    private ArrayList<RankedFirstItem> mList;
    private ArrayList<RankListItem> mDatas;

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 1 : 2;
    }

    @NonNull
    @Override
    public RankedFirstAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_first_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_list_item, parent, false);
        }
        RankedFirstAdapter.ViewHolder holder = new RankedFirstAdapter.ViewHolder(view);
        return holder;

    }

    public RankedFirstAdapter(Activity mContext, ArrayList<RankedFirstItem> mList, ArrayList<RankListItem> mData) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
        this.mDatas = new ArrayList<>();
    }

    public void setFirsList(ArrayList<RankedFirstItem> mList) {
        if (mList != null && mList.size() > 0) {
            this.mList = mList;
            notifyDataSetChanged();
        }
    }

    public void setLists(RankListItem[] mData) {
        if (mData != null && mData.length > 0) {
            for (RankListItem item :
                    mData) {
                this.mDatas.add(item);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RankedFirstAdapter.ViewHolder holder, int position) {
        String[] imgs = mList.get(0).imgs.split("\\,");
        if (getItemViewType(position) == 1) {
            if (holder.ivFristBg != null)

                Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + imgs[0]).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                        .dontAnimate().centerCrop()).into(holder.ivFristBg);
            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + mList.get(position).photo).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                    .dontAnimate().centerCrop()).into(holder.icon);
            holder.name.setText(mList.get(position).nickname);
            holder.likeNumber.setText(mList.get(position).likeCount + "");
        } else {
            if(position==1){
                holder.tvRank.setVisibility(View.INVISIBLE);
                holder.numberBg.setImageResource(R.drawable.rank_second); //设置第二名bg
                holder.numberBg.setVisibility(View.VISIBLE);
            }else{
                if(position==2){
                    holder.tvRank.setVisibility(View.INVISIBLE);
                    holder.numberBg.setImageResource(R.drawable.rank_third); //设置第三名bg
                    holder.numberBg.setVisibility(View.VISIBLE);
            }else{
               holder.numberBg.setVisibility(View.INVISIBLE);
               holder.tvRank.setVisibility(View.VISIBLE);
                }

            }


            if(holder.tvRank!=null)holder.tvRank.setText( mDatas.get(position-1).rank+"");
            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + mDatas.get(position-1).photo).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                    .dontAnimate().centerCrop()).into(holder.icon);
            holder.name.setText(mDatas.get(position-1).nickname);
            holder.likeNumber.setText(mDatas.get(position-1).likeCount + "");
        }

        holder.tvBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItemViewType(position) == 1){
                Intent intent = new Intent(mContext, RankDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mList.get(position).postUuid);
                intent.putExtra(Constants.INTENT_BIZE_TYPE,mList.get(position).bizType);
                mContext.startActivity(intent);
                }else{
                    Intent intent = new Intent(mContext, RankDetailActivity.class);
                    intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mDatas.get(position-1).postUuid);
                    intent.putExtra(Constants.INTENT_BIZE_TYPE,mDatas.get(position-1).bizType);
                    mContext.startActivity(intent);
                }
            }
        });
        holder.ivFristBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RankDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, mList.get(position).postUuid);
                intent.putExtra(Constants.INTENT_BIZE_TYPE,mList.get(position).bizType);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (mList.size() > 0) i += mList.size();
        if (mDatas.size() > 0) i += mDatas.size();
        return i ;
        //return 10;
    }

    class ViewHolder extends IViewHolder {
        @BindView(R.id.riv_head_image)
        RoundImageView icon;
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.text_number)
        TextView likeNumber;
        @BindView(R.id.ivFristBg)
        ImageView ivFristBg;
        @BindView(R.id.numberBg)
        ImageView numberBg;
        @BindView(R.id.tvRank)
        TextView tvRank;
        @BindView(R.id.tvBg)
                TextView tvBg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
