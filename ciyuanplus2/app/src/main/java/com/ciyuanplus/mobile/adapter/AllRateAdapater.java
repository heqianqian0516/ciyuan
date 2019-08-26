package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.bean.RateItem;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.MarkView;
import com.ciyuanplus.mobile.widget.RoundImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2018/1/9.
 */

public class AllRateAdapater extends RecyclerView.Adapter<AllRateAdapater.ViewHolder> {

    private final ArrayList<RateItem> mRateList;
    private final Context mContext;
    private final MyOnClickListener myOnclickListener = new MyOnClickListener() {

        @Override
        public void performRealClick(View v) {
            int id = v.getId();
            if (id == R.id.m_list_item_all_rates_head) {
                RateItem item = (RateItem) v.getTag(R.id.glide_item_tag);
                //   是自己发布的帖子，不允许进入他人页面
                if (Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(mContext, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, item.userUuid);
                mContext.startActivity(intent);
            }
        }
    };
    private final View.OnClickListener mItemClickListener;

    public AllRateAdapater(Context mContext, ArrayList<RateItem> mRateList, View.OnClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mRateList = mRateList;
        this.mItemClickListener = mItemClickListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_all_rates, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    public RateItem getItem(int position) {
        return mRateList.get(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RateItem item = mRateList.get(position);
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + item.photo).apply(new RequestOptions().dontAnimate()).into(holder.mListItemAllRatesHead);
        holder.mListItemAllRatesSex.setImageResource(UserInfoItem.getSexImageResource(item.sex));
        holder.mListItemAllRatesName.setText(item.nickname);
        boolean isNeighbor = Utils.isStringEquals(item.currentCommunityUuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid)
                && !Utils.isStringEquals(item.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid);
        holder.mListItemAllRatesNeighbor.setVisibility(isNeighbor ? View.VISIBLE : View.GONE);

        if (item.score > 0) {
            holder.mListItemAllRatesMarkView.setVisibility(View.VISIBLE);
            holder.mListItemAllRatesMarkView.setValue(item.score);
        } else holder.mListItemAllRatesMarkView.setVisibility(View.GONE);
        if (!Utils.isStringEmpty(item.contentText)) {
            holder.mListItemAllRatesContent.setText(item.contentText);
            holder.mListItemAllRatesContent.setVisibility(View.VISIBLE);
        } else {
            holder.mListItemAllRatesContent.setVisibility(View.GONE);
        }
        holder.mListItemAllRatesTime.setText(Utils.getFormattedTimeString(item.createTime));

        holder.mListItemAllRatesHead.setTag(R.id.glide_item_tag, item);
        holder.mListItemAllRatesHead.setOnClickListener(myOnclickListener);
    }

    @Override
    public int getItemCount() {
        return mRateList == null ? 0 : mRateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_all_rates_head)
        RoundImageView mListItemAllRatesHead;
        @BindView(R.id.m_list_item_all_rates_sex)
        ImageView mListItemAllRatesSex;
        @BindView(R.id.m_list_item_all_rates_name)
        TextView mListItemAllRatesName;
        @BindView(R.id.m_list_item_all_rates_neighbor)
        ImageView mListItemAllRatesNeighbor;
        @BindView(R.id.m_list_item_all_rates_mark_view)
        MarkView mListItemAllRatesMarkView;
        @BindView(R.id.m_list_item_all_rates_content)
        TextView mListItemAllRatesContent;
        @BindView(R.id.m_list_item_all_rates_time)
        TextView mListItemAllRatesTime;
        @BindView(R.id.m_list_item_all_rates_content_layout)
        LinearLayout mListItemAllRatesContentLayout;

        ViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);

        }
    }
}
