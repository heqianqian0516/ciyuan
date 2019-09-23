package com.ciyuanplus.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.MyZoomImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

//import java.io.File;

/**
 * Created by Alen on 2017/3/19.
 */
public class FullScreenPreViewAdapter extends PagerAdapter {
    private String[] images;
    private FullScreenImageActivity mContext;
    private int screenWidth;
    private CustomDialog dialog;

    public FullScreenPreViewAdapter(String[] images, FullScreenImageActivity context) {
        this.images = images;
        this.mContext = context;
        this.screenWidth = Utils.getScreenWidth();
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), Constants.SETTLE_FILE_PATH);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");
            e.printStackTrace();
        } catch (IOException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        CommonToast.getInstance("图片保存成功").show();
    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View root = View.inflate(mContext, R.layout.item_full_screen_preview, null);
        MyZoomImageView photoView = root.findViewById(R.id.m_ivImage);
        container.addView(root, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        photoView.setOnClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                mContext.finish();
            }
        });
        photoView.setOnLongClickListener(view -> {
            if (dialog != null && dialog.isShowing()) return false;
            final String url = (String) view.getTag(R.id.glide_item_tag);
            if (Utils.isStringEmpty(url)) return false; // 如果图片没显示出来
            CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
            builder.setMessage("确定要保存图片吗？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                loadBitmap(url);
                // saveImageToGallery(mContext, bm);
                dialog.dismiss();
            });
            builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return false;
        });
        if (images[position].startsWith("/storage")) {

            RequestOptions options = new RequestOptions().error(R.mipmap.imgfail);
            Glide.with(mContext).load(images[position]).apply(options).into(photoView);
            photoView.setTag(R.id.glide_item_tag, images[position]);
            //ImageLoader.getInstance().displayImage("file:///" + images[position], photoView);

        } else if (images[position].startsWith("http://") || images[position].startsWith("https://")) {

            RequestOptions options = new RequestOptions().error(R.mipmap.imgfail).dontAnimate();
            Glide.with(mContext).load(images[position]).apply(options).into(photoView);
            photoView.setTag(R.id.glide_item_tag, images[position]);
            //ImageLoader.getInstance().displayImage(images[position], photoView);
        } else {

            RequestOptions options = new RequestOptions().error(R.mipmap.imgfail).dontAnimate();
            Glide.with(mContext).load(Constants.IMAGE_LOAD_HEADER + images[position]).apply(options).into(photoView);
            photoView.setTag(R.id.glide_item_tag, Constants.IMAGE_LOAD_HEADER + images[position]);
            //ImageLoader.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + images[position], photoView);
        }
        return root;
    }

    public void loadBitmap(String url) {
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(mContext).asBitmap().load(url).apply(options).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                saveImageToGallery(mContext, (Bitmap) resource);
                return false;
            }


        }).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }
}
