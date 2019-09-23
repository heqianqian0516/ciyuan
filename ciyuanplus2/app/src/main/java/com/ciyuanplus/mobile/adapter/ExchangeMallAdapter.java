package com.ciyuanplus.mobile.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciyuanplus.mobile.R;


public class ExchangeMallAdapter extends RecyclerView.Adapter<ExchangeMallAdapter.ViewHolder> {
    @NonNull
    @Override
    public ExchangeMallAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exchange_mall_item, parent, false);
        ExchangeMallAdapter.ViewHolder holder = new ExchangeMallAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeMallAdapter.ViewHolder holder, int position) {
          holder.ll_exchange_mall.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(clickCallBack!=null){
                      clickCallBack.callBack();
                  }
              }
          });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout ll_exchange_mall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_exchange_mall = itemView.findViewById(R.id.ll_exchange_mall);
        }
    }
    //定义接口
    private ClickCallBack clickCallBack;

    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ClickCallBack {
        void callBack();
    }
}
