package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.module.found.market.MarketActivity;
import com.ciyuanplus.mobile.module.live_hood.LiveHoodActivity;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by kk on 2018/5/5.
 */

public class SquareFragmentBannerAdapter extends RecyclerView.Adapter<SquareFragmentBannerAdapter.ViewHolder> {

    private List<Integer> list;
    private final String mHomePrice;
    private final Context mContext;

    public SquareFragmentBannerAdapter(Context context, List<Integer> list, String homePrice) {

        this.mContext = context;
        this.list = list;
        this.mHomePrice = homePrice;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crid_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mImage.setImageResource(list.get(position));
        switch (position) {
            case 0:

                holder.mView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, MarketActivity.class);
                    mContext.startActivity(intent);
                });

                break;
            case 1:
                holder.mHomePriceLayout.setVisibility(View.VISIBLE);
                holder.mTextLable.setVisibility(View.VISIBLE);
                holder.mHomePrice.setVisibility(View.VISIBLE);
                holder.mUnit.setVisibility(View.VISIBLE);
                holder.mHomePrice.setText(mHomePrice);
                break;
            case 2:

                holder.mView.setOnClickListener(v -> {
//                    Intent intent2 = new Intent(mContext, NeighbourActivity.class);
//                    mContext.startActivity(intent2);
                });


                break;
            case 3:
                holder.mView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, LiveHoodActivity.class);
                    mContext.startActivity(intent);
                });
                break;
        }

    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImage;
        final TextView mTextLable;
        final TextView mHomePrice;
        final TextView mUnit;
        final LinearLayout mHomePriceLayout;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = view.findViewById(R.id.iv_card);
            mTextLable = view.findViewById(R.id.tv_label);
            mHomePrice = view.findViewById(R.id.tv_home_price);
            mUnit = view.findViewById(R.id.tv_unit);
            mHomePriceLayout = view.findViewById(R.id.ll_home_price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mHomePrice.getText() + "'";
        }
    }
}
