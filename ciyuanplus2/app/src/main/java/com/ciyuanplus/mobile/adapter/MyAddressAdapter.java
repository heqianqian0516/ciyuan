package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.CommunityManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.bean.CommunityItem;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 */

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ViewHolder> {
    private final Context mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            if (view.getId() == R.id.m_list_item_my_address_spread) {
                CommunityItem item = (CommunityItem) view.getTag();
                item.isClosed = !item.isClosed;
                notifyDataSetChanged();
            }
//            else if (view.getId() == R.id.m_list_item_my_address_add_detail) {
//
//                CommunityItem item = (CommunityItem) view.getTag();
//                if (item.userCommunityAddressResults.length >= 5) {
//                    CommonToast.getInstance("您最多只能添加5个详细地址").show();
//                    return;
//                }
//                Intent intent = new Intent(mContext, AddDetailAddressActivity.class);
//                intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, AddDetailAddressActivity.ACTIVITY_ADDRESS_MANGE_TYPE);
//                intent.putExtra(Constants.INTENT_COMMUNITY_NAME, item.commName);
//                intent.putExtra(Constants.INTENT_COMMUNITY_ID, item.uuid);
//                mContext.startActivity(intent);
//            }
            else if (view.getId() == R.id.m_list_item_my_address_delete) {
                final String uuid = (String) view.getTag();
                if (Utils.isStringEquals(uuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid)) {
                    // 默认小区不能删除，解决了 删除默认小区无默认小区问题，和 不能删掉所有小区的需求
                    CommonToast.getInstance(mContext.getResources().getString(R.string.string_delete_default_community_alert)).show();
                    return;
                }
                CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                builder.setMessage("确定删除吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    CommunityManager.getInstance().deleteCommunity(uuid);// 删除成功之后会刷新页面
                    dialog.dismiss();
                });
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                CustomDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }
    };
    private CommunityItem[] communityItems;
    private View.OnClickListener mItemClickListener;

    public MyAddressAdapter(Context mContext, CommunityItem[] communityItems, View.OnClickListener itemClickListener) {
        this.mContext = mContext;
        this.communityItems = communityItems;
        this.mItemClickListener = itemClickListener;
    }

    public void setCommunityItems(CommunityItem[] communityItems) {
        this.communityItems = communityItems;
    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public CommunityItem getItem(int i) {
        return communityItems[i];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_address, parent, false);
        MyAddressAdapter.ViewHolder holder = new MyAddressAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommunityItem item = communityItems[position];
        holder.name.setText(item.commName);
        if (Utils.isStringEquals(item.uuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid)) {
            holder.icon.setImageResource(R.mipmap.icon_address_list_orange);
            holder.name.setTextColor(0xfff0590e);
        } else {
            holder.icon.setImageResource(R.mipmap.icon_address_list_grey);
            holder.name.setTextColor(0xff333333);
        }
        if (item.isClosed) {
            holder.detailLayout.setVisibility(View.GONE);
            holder.spread.setImageResource(R.mipmap.launch_icon_open);
        } else {
            holder.detailLayout.setVisibility(View.VISIBLE);
            holder.spread.setImageResource(R.mipmap.launch_icon_stop);
        }

//        DetailAddressAdapter adapter = new DetailAddressAdapter(mContext, item.userCommunityAddressResults);
//        holder.detailList.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        holder.delete.setTag(item.uuid);
        holder.delete.setOnClickListener(myOnClickListener);
        holder.addDetail.setTag(item);
        holder.addDetail.setOnClickListener(myOnClickListener);
        holder.spread.setTag(item);
        holder.spread.setOnClickListener(myOnClickListener);
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
        @BindView(R.id.m_list_item_my_address_name)
        TextView name;
        @BindView(R.id.m_list_item_my_address_icon)
        ImageView icon;
        @BindView(R.id.m_list_item_my_address_delete)
        ImageView delete;
        @BindView(R.id.m_list_item_my_address_spread)
        ImageView spread;
        @BindView(R.id.m_list_item_my_address_spread_lp)
        LinearLayout detailLayout;
        //        @BindView(R.id.m_list_item_my_address_detail_list) ListViewForScrollView detailList;
        @BindView(R.id.m_list_item_my_address_add_detail)
        LinearLayout addDetail;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }
}