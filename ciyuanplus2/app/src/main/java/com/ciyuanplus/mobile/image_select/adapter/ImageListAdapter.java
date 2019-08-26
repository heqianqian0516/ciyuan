package com.ciyuanplus.mobile.image_select.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.ImgSelConfig;
import com.ciyuanplus.mobile.image_select.bean.Image;
import com.ciyuanplus.mobile.image_select.common.Constant;
import com.ciyuanplus.mobile.image_select.common.OnItemClickListener;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;


/**
 * @author yuyh.
 * @date 2016/8/5.
 */
public class ImageListAdapter extends EasyRVAdapter<Image> {

    private boolean showCamera;
    private boolean mutiSelect;

    private ImgSelConfig config;
    private Context context;
    private OnItemClickListener listener;

    public ImageListAdapter(Context context, List<Image> list, ImgSelConfig config) {
        super(context, list, R.layout.item_img_sel, R.layout.item_img_sel_take_photo);
        this.context = context;
        this.config = config;
    }

    @Override
    protected void onBindData(final EasyRVHolder viewHolder, final int position, final Image item) {

        if (position == 0 && showCamera) {
            ImageView iv = viewHolder.getView(R.id.m_ivTakePhoto);
            iv.setImageResource(R.mipmap.launch_icon_camera);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onImageClick(position, item.path);
                }
            });
            return;
        }

        if (mutiSelect) {
            viewHolder.getView(R.id.m_ivPhotoCheaked).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int ret = listener.onCheckedClick(position, item.path);
                        if (ret == 1) { // 局部刷新
                            if (Constant.imageList.contains(item.path)) {
                                viewHolder.setImageResource(R.id.m_ivPhotoCheaked, R.mipmap.launch_note_img_circle_sel);
                            } else {
                                viewHolder.setImageResource(R.id.m_ivPhotoCheaked, R.mipmap.launch_note_img_circle_nor);
                            }
                        }
                    }
                }
            });
        }

        viewHolder.setOnItemViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onImageClick(position, item.path);
            }
        });

        final ImageView iv = viewHolder.getView(R.id.m_ivImage);
        config.loader.displayImage(context, "file:///" + item.path, iv);

        if (mutiSelect) {
            viewHolder.setVisible(R.id.m_ivPhotoCheaked, true);
            if (Constant.imageList.contains(item.path)) {
                viewHolder.setImageResource(R.id.m_ivPhotoCheaked, R.mipmap.launch_note_img_circle_sel);
            } else {
                viewHolder.setImageResource(R.id.m_ivPhotoCheaked, R.mipmap.launch_note_img_circle_nor);
            }
        } else {
            viewHolder.setVisible(R.id.m_ivPhotoCheaked, false);
        }
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public void setMutiSelect(boolean mutiSelect) {
        this.mutiSelect = mutiSelect;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && showCamera) {
            return 1;
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
