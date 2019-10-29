package com.ciyuanplus.mobile.module.video;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ciyuanplus.mobile.R;
import com.kris.baselibrary.base.LazyLoadBaseFragment;

/**
 * 视频关注页
 */
public class AttentionFragment extends LazyLoadBaseFragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attention, container, false);
        return view;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_attention;
    }

    @Override
    public void lazyLoad() {

    }
}
