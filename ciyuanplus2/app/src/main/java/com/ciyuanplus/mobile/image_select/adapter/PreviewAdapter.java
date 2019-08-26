package com.ciyuanplus.mobile.image_select.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.ImgSelConfig;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.common.OnItemClickListener;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

/**
 * @author yuyh.
 * @date 2016/9/28.
 */
public class PreviewAdapter extends PagerAdapter {

    private Activity activity;
    private List<String> images;
    private ImgSelConfig config;
    private OnItemClickListener listener;

    public PreviewAdapter(Activity activity, List<String> images, ImgSelConfig config) {
        this.activity = activity;
        this.images = images;
        this.config = config;
    }

    @Override
    public int getCount() {
        if (config.needCamera)
            return images.size() - 1;
        else
            return images.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View root = View.inflate(activity, R.layout.item_pager_img_sel, null);
        final ImageView photoView = (ImageView) root.findViewById(R.id.m_ivImage);
        final ImageView ivChecked = (ImageView) root.findViewById(R.id.m_ivPhotoCheaked);

        if (config.multiSelect) {

            ivChecked.setVisibility(View.VISIBLE);
            final String image = images.get(config.needCamera ? position + 1 : position);
            if (Constant.imageList.contains(image)) {
                ivChecked.setImageResource(R.mipmap.launch_note_img_circle_sel);
            } else {
                ivChecked.setImageResource(R.mipmap.launch_note_img_circle_nor);
            }

            ivChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int ret = listener.onCheckedClick(position, image);
                        if (ret == 1) { // 局部刷新
                            if (Constant.imageList.contains(image)) {
                                ivChecked.setImageResource(R.mipmap.launch_note_img_circle_sel);
                            } else {
                                ivChecked.setImageResource(R.mipmap.launch_note_img_circle_nor);
                            }
                        }
                        notifyDataSetChanged();
                    }
                }
            });

            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onImageClick(position, images.get(position));
                    }
                }
            });
        } else {
            ivChecked.setVisibility(View.GONE);
        }

        container.addView(root, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        displayImage(photoView, images.get(config.needCamera ? position + 1 : position));

        return root;
    }

    private void displayImage(ImageView photoView, String path) {
        config.loader.displayImage(activity, "file:///" + path, photoView);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
