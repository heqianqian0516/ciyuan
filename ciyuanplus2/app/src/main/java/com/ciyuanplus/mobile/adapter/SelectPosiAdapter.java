package com.ciyuanplus.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.module.register.select_location.SelectLocationActivity;
import com.ciyuanplus.mobile.widget.CustomDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/20.
 * 选择小区 的adapter
 */

public class SelectPosiAdapter extends BaseAdapter {
    private final SelectLocationActivity mContext;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            final PoiItem item = (PoiItem) view.getTag();
            CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
            builder.setMessage("确定选择该" + item.getTitle() + "?");
            builder.setPositiveButton("确定", (dialog, which) -> {

                mContext.mPresenter.mPoiItem = item;
                notifyDataSetChanged();
                dialog.dismiss();
                mContext.mPresenter.addCommunity();
            });
            builder.setNegativeButton("取消", (dialog, which) -> {

                dialog.dismiss();
                notifyDataSetChanged();
            });
            CustomDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    };
    private final List<PoiItem> mList;

    public SelectPosiAdapter(SelectLocationActivity mContext, List<PoiItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        PoiItem item = mList.get(i);
        if (null == convertView) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_item_select_posi, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(item.getTitle());
        holder.checkBox.setChecked(mContext.mPresenter.mPoiItem != null && mContext.mPresenter.mPoiItem == item);
        holder.checkBox.setTag(item);
        holder.checkBox.setOnClickListener(myOnClickListener);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.m_list_item_select_posi_check_box)
        CheckBox checkBox;
        @BindView(R.id.m_list_item_select_posi_name)
        TextView name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}