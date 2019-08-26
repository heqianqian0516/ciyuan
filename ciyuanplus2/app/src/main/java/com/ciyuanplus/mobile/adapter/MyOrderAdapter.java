package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.bean.MyOrderItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.SquareImageView;
import com.kris.baselibrary.util.NumberUtil;

import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kk on 2018/5/24.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private List<MyOrderItem> myOrderItemList;
    private final Context mContext;
    private final View.OnClickListener mViewOnClickListener;

    public MyOrderAdapter(Context context, List<MyOrderItem> myOrderItemList, View.OnClickListener onClickListener) {
        this.myOrderItemList = myOrderItemList;
        this.mContext = context;
        this.mViewOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(mContext, R.layout.list_item_my_order, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(mViewOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MyOrderItem item = myOrderItemList.get(position);
        holder.status.setTextColor(Color.parseColor("#eda108"));
        holder.status.setText(item.getStatusStr());
        holder.price.setText(String.format("￥%s", NumberUtil.getAmountValue(String.valueOf((float)item.getPrice()/100))));

        MyOrderItem.SubOrderInfo subOrderInfo = item.getSubOrderInfo().get(0);
        if (subOrderInfo != null) {
            if (!StringUtils.isEmpty(subOrderInfo.getProdImg())) {
                Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + subOrderInfo.getProdImg())
                        .apply(new RequestOptions().dontAnimate().error(R.mipmap.imgfail)).into(holder.imageView);
            }
            holder.title.setText(subOrderInfo.getProdName());
            holder.specification.setText(String.format(Locale.getDefault(), "%sx%d", subOrderInfo.getProdName(), subOrderInfo.getProdCount()));
        }
    }

    @Override
    public int getItemCount() {
        return null == myOrderItemList ? 0 : myOrderItemList.size();
    }

    public MyOrderItem getItem(int position) {
        return myOrderItemList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.siv_order_image)
        SquareImageView imageView;
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_price)
        TextView price;
        @BindView(R.id.tv_status)
        TextView status;
        @BindView(R.id.tv_specification)
        TextView specification;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
