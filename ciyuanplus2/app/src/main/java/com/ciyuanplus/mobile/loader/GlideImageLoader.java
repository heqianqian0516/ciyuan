package com.ciyuanplus.mobile.loader;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;


/**
 * @author kk
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
//        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_loading_rotate)
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
////        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
//        Glide.with(context.getApplicationContext())
//                .load(path)
//                .apply(options)
//                .apply()
//                .into(imageView);
//
////        //Picasso 加载图片简单用法
        Picasso.get().load(path.toString()).into(imageView);
//
//        //用fresco加载图片简单用法，记得要写下面的createImageView方法
//        Uri uri = Uri.parse((String) path);
//        imageView.setImageURI(uri);

    }

//    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}
