package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.base.irecyclerview.IViewHolder;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.bean.RankListItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankingListAdapter extends RecyclerView.Adapter<RankingListAdapter.ViewHolder> {
    private final Activity mContext;
    private final ArrayList<RankListItem> mList;
    @NonNull
    @Override
    public RankingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_list_item, parent, false);
        RankingListAdapter.ViewHolder holder = new RankingListAdapter.ViewHolder(view);
        return holder;

    }
    public RankingListAdapter(Activity mContext, ArrayList<RankListItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    public RankListItem getItem(int i) {
        return mList.get(i);
    }
    @Override
    public void onBindViewHolder(@NonNull RankingListAdapter.ViewHolder holder, int position) {
        RankListItem item = mList.get(position);
        Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop()).into(holder.icon);
        holder.name.setText(item.nickname);
        holder.likeNumber.setText(item.likeCount+"");
     //   holder.numberRank.setText(item.rank+"");

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
        //return 10;
    }

    class ViewHolder extends IViewHolder {
        @BindView(R.id.riv_head_image)
        RoundImageView icon;
        @BindView(R.id.tv_name)
        TextView name;
        /*@BindView(R.id.text_rank)
        TextView numberRank;*/
        @BindView(R.id.text_number)
        TextView likeNumber;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
