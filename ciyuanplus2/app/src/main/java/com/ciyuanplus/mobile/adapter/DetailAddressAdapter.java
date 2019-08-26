package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.CommunityManager;
import com.ciyuanplus.mobile.net.bean.CommunityDetailItem;
import com.ciyuanplus.mobile.widget.CustomDialog;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 * Drop
 * 我的小区 详细地址适配器， 暂时不用了
 */

public class DetailAddressAdapter extends RecyclerView.Adapter<DetailAddressAdapter.ViewHolder> {
    private final Context mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            if (view.getId() == R.id.m_list_item_detail_address_delete) {
                final String uuid = (String) view.getTag();
                CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                builder.setMessage("确定删除吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    CommunityManager.getInstance().deleteDetailAddress(uuid);// 删除成功之后会刷新页面
                    dialog.dismiss();
                });
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                CustomDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }
    };
    private final CommunityDetailItem[] CommunityDetailItems;
    private View.OnClickListener mItemClickListener;

    public DetailAddressAdapter(Context mContext, CommunityDetailItem[] CommunityDetailItems, View.OnClickListener itemClickListener) {
        this.mContext = mContext;
        this.CommunityDetailItems = CommunityDetailItems;
        this.mItemClickListener = itemClickListener;
    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public CommunityDetailItem getItem(int i) {
        return CommunityDetailItems[i];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_detail_address, parent, false);
        DetailAddressAdapter.ViewHolder holder = new DetailAddressAdapter.ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommunityDetailItem item = CommunityDetailItems[position];
        holder.name.setText(item.address);

        holder.delete.setTag(item.addressUuid);
        holder.delete.setOnClickListener(myOnClickListener);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return CommunityDetailItems == null ? 0 : CommunityDetailItems.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.m_list_item_detail_address_name)
        TextView name;
        @BindView(R.id.m_list_item_detail_address_delete)
        ImageView delete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}