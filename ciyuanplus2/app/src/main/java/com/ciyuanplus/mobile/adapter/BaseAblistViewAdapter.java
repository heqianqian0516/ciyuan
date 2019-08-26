package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * des:基础AblistView适配器
 * Created by xsf
 * on 2016.04.17:03
 */
class BaseAblistViewAdapter<T> extends android.widget.BaseAdapter {


    final Context mContext;
    private final LayoutInflater mInflater;
    private List<T> data = new ArrayList<>();

    BaseAblistViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    public void reset(List<T> d) {
        if (d == null) return;
        data.clear();
        data.addAll(d);
        notifyDataSetChanged();
    }

    List<T> getData() {
        return data;
    }

    /**
     * 设置数据源
     *
     * @param d
     */
    void setData(List<T> d) {
        data = d;
    }

    void add(T d) {
        if (d == null) return;
        if (data == null) data = new ArrayList<>();
        data.add(d);
        notifyDataSetChanged();
    }

    public void addAllAt(int position, List<T> d) {
        if (d == null) return;
        if (data == null) data = new ArrayList<>();
        data.addAll(position, d);
        notifyDataSetChanged();
    }

    void addAt(int position, T d) {
        if (d == null) return;
        if (data == null) data = new ArrayList<>();
        data.add(position, d);
        notifyDataSetChanged();
    }

    void addAll(List<T> d) {
        if (d == null) return;
        if (data == null) data = new ArrayList<>();
        data.addAll(d);
        notifyDataSetChanged();
    }

    void remove(int position) {
        if (data == null) return;
        data.remove(position);
        notifyDataSetChanged();
    }


    /**
     * 清空所有数据
     */
    public void clearAll() {
        data.clear();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }


}
