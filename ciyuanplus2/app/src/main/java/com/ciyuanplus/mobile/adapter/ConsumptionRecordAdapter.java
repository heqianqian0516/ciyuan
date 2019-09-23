package com.ciyuanplus.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciyuanplus.mobile.R;

public class ConsumptionRecordAdapter extends RecyclerView.Adapter<ConsumptionRecordAdapter.ViewHolder> {
    @NonNull
    @Override
    public ConsumptionRecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumption_record_item, parent, false);
        ConsumptionRecordAdapter.ViewHolder holder = new ConsumptionRecordAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumptionRecordAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
