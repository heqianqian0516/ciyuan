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
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.net.bean.WantsItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.RoundImageView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2018/1/9.
 */

public class AllWantsAdapater extends RecyclerView.Adapter<AllWantsAdapater.ViewHolder> {

    private final ArrayList<WantsItem> mWantsList;
    private final View.OnClickListener mItemClickListener;

    public AllWantsAdapater(Context mContext, ArrayList<WantsItem> mWantsList, View.OnClickListener mItemClickListener) {
        Context context = mContext;
        this.mWantsList = mWantsList;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_all_wants, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    public WantsItem getItem(int position) {
        return mWantsList.get(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WantsItem item = mWantsList.get(position);
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(new RequestOptions().dontAnimate()).into(holder.mListItemAllWantsHead);
        holder.mListItemAllWantsSex.setImageResource(UserInfoItem.getBigSexImageResource(item.sex));
        holder.mListItemAllWantsText.setText(item.nickname);
        boolean isNeighbor = Utils.isStringEquals(item.currentCommunityUuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid)
                && !Utils.isStringEquals(item.uuid, UserInfoData.getInstance().getUserInfoItem().uuid);
        holder.mListItemAllWantsNeighbor.setVisibility(isNeighbor ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mWantsList == null ? 0 : mWantsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_all_wants_head)
        RoundImageView mListItemAllWantsHead;
        @BindView(R.id.m_list_item_all_wants_sex)
        ImageView mListItemAllWantsSex;
        @BindView(R.id.m_list_item_all_wants_text)
        TextView mListItemAllWantsText;
        @BindView(R.id.m_list_item_all_wants_neighbor)
        ImageView mListItemAllWantsNeighbor;

        ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);

        }
    }
}
