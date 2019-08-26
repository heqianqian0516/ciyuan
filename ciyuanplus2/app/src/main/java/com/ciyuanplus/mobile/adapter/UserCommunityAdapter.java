package com.ciyuanplus.mobile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.module.mine.change_community.ChangeCommunityActivity;
import com.ciyuanplus.mobile.module.popup.select_stuff_community.SelectStuffCommunityActivity;
import com.ciyuanplus.mobile.net.bean.CommunityItem;
import com.ciyuanplus.mobile.utils.Utils;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/11.
 * 用户小区列表
 */

public class UserCommunityAdapter extends RecyclerView.Adapter<UserCommunityAdapter.ViewHolder> {
    private final Activity mContext;
    private CommunityItem[] communityItems;
    private final View.OnClickListener mItemClickListener;

    public UserCommunityAdapter(Activity mContext, CommunityItem[] communityItems, View.OnClickListener mItemClickListener) {
        this.mContext = mContext;
        this.communityItems = communityItems;
        this.mItemClickListener = mItemClickListener;
    }

    public void setCommunityItems(CommunityItem[] communityItems) {
        this.communityItems = communityItems;
    }

    public CommunityItem getItem(int i) {
        return communityItems[i];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user_community, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommunityItem item = communityItems[position];
        if ((mContext instanceof ChangeCommunityActivity && Utils.isStringEquals(((ChangeCommunityActivity) mContext).mPresenter.mDefaultCommunity.uuid, item.uuid))
                || (mContext instanceof SelectStuffCommunityActivity && Utils.isStringEquals(((SelectStuffCommunityActivity) mContext).selectCommunityId, item.uuid))) {
            holder.name.setTextColor(0xfff0590e);
            holder.image.setImageResource(R.mipmap.my_icon_location_sel);
            //holder.layout.setBackgroundColor(0xfff6f6f6);
        } else {
            holder.name.setTextColor(0xff333333);
            holder.image.setImageResource(R.mipmap.my_icon_location_nor);
            //holder.layout.setBackgroundColor(0x00f6f6f6);
        }
        holder.name.setText(item.commName);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return communityItems == null ? 0 : communityItems.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_user_community_image)
        ImageView image;
        @BindView(R.id.m_list_item_user_community_name)
        TextView name;
        @BindView(R.id.m_list_item_user_community_lp)
        RelativeLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
