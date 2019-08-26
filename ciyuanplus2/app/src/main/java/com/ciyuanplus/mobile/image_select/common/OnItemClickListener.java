package com.ciyuanplus.mobile.image_select.common;

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
public interface OnItemClickListener {

    int onCheckedClick(int position, String image);

    void onImageClick(int position, String image);
}
