package com.ciyuanplus.mobile.image_select.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.ImgSelConfig;
import com.ciyuanplus.mobile.image_select.bean.Folder;
import com.ciyuanplus.mobile.image_select.common.OnFolderChangeListener;
import com.yuyh.easyadapter.abslistview.EasyLVAdapter;
import com.yuyh.easyadapter.abslistview.EasyLVHolder;

import java.util.List;


/**
 * @author yuyh.
 * @date 2016/8/5.
 */
public class FolderListAdapter extends EasyLVAdapter<Folder> {

    private Context context;
    private List<Folder> folderList;
    private ImgSelConfig config;

    private int selected = 0;
    private OnFolderChangeListener listener;

    public FolderListAdapter(Context context, List<Folder> folderList, ImgSelConfig config) {
        super(context, folderList, R.layout.item_img_sel_folder);
        this.context = context;
        this.folderList = folderList;
        this.config = config;
    }

    @Override
    public void convert(EasyLVHolder holder, final int position, Folder folder) {
        if (position == 0) {
            holder.setText(R.id.m_tvFolderName, "所有图片")
                    .setText(R.id.m_tvImageNum, "共" + getTotalImageSize() + "张");
            ImageView ivFolder = holder.getView(R.id.m_ivFolder);
            if (folderList.size() > 0) {
                config.loader.displayImage(context, "file:///" + folder.cover.path, ivFolder);
            }
        } else {
            holder.setText(R.id.m_tvFolderName, folder.name)
                    .setText(R.id.m_tvImageNum, "共" + folder.images.size() + "张");
            ImageView ivFolder = holder.getView(R.id.m_ivFolder);
            if (folderList.size() > 0) {
                config.loader.displayImage(context, "file:///" + folder.cover.path, ivFolder);
            }
        }

        if (selected == position) {
            holder.setVisible(R.id.m_indicator, true);
        } else {
            holder.setVisible(R.id.m_indicator, false);
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectIndex(position);
            }
        });
    }

    public void setData(List<Folder> folders) {
        folderList.clear();
        if (folders != null && folders.size() > 0) {
            folderList.addAll(folders);
        }
        notifyDataSetChanged();
    }

    private int getTotalImageSize() {
        int result = 0;
        if (folderList != null && folderList.size() > 0) {
            for (Folder folder : folderList) {
                result += folder.images.size();
            }
        }
        return result;
    }

    public int getSelectIndex() {
        return selected;
    }

    public void setSelectIndex(int position) {
        if (selected == position)
            return;
        if (listener != null)
            listener.onChange(position, folderList.get(position));
        selected = position;
        notifyDataSetChanged();
    }

    public void setOnFloderChangeListener(OnFolderChangeListener listener) {
        this.listener = listener;
    }
}
