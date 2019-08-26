package com.ciyuanplus.mobile;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ciyuanplus.mobile.activity.MainActivityNew;
import com.ciyuanplus.mobile.activity.MessageCenterActivity;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.mine.mine.MineFragmentNew;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.kris.baselibrary.base.CrashHandler;
import com.lzy.ninegrid.NineGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.taobao.sophix.SophixManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


/**
 * Created by Alen on 2017/5/8.
 */

@SuppressLint("Registered")
public class App extends MultiDexApplication {
    public static Context mContext;

    private String mOderId = "";

    public static PushAgent mPushAgent;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initBugly();
        initLogger();

        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        ((App) mContext).setOderId("");
        SophixManager.getInstance().queryAndLoadNewPatch();
//        initLogger();

        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);
        //UniversalImageLoader初始化
        ImageLoader.getInstance().init(config);

        initNineGirdViewAdapter();

        //初始化X5 webview的相关
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {

                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        //TODO 初始化app配置
        ApiContant.setServerHost();
        Constants.setImageHost();
        //初始化 EventCenterManager
        EventCenterManager.initEventCenterManager();

        /**
         * 初始化Umeng 分享
         * 注意：如果您已经在AndroidManifest.xml中配置过appkey和channel值，可以调用此版本初始化函数。
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "2b52c3f0b0667cd6fac3c8485580f073");
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5bfe1d70f1f556652300011e");
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent = PushAgent.getInstance(this);
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        // 选用MANUAL页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
        // 打开统计SDK调试模式
//        UMConfigure.setLogEnabled(true);

        PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_APPSECRET);
        PlatformConfig.setQQZone(Constants.QQ_APP_ID, Constants.QQ_APPSECRET);

        PlatformConfig.setSinaWeibo(Constants.WB_APP_ID, Constants.WB_APPSECRET, "https://api.weibo.com/oauth2/default.html");

        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Logger.d("deviceToken = " + deviceToken);

                SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_UMENG_DEVICE_TOKEN, deviceToken);

            }

            @Override
            public void onFailure(String s, String s1) {
                Logger.d("token", "register fail" + s + s1);
            }
        });

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                super.handleMessage(context, uMessage);
            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {
//            super.launchApp(context, uMessage);
                LoginStateManager.isLogin();
                String noticeType = "";
                try {
                    JSONObject object = new JSONObject(uMessage.extra);
                    noticeType = object.getString("noticeType");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!Utils.isStringEquals(noticeType, "8")) {
                    Intent intent = new Intent(App.mContext, MainActivityNew.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, 0);
                    intent.putExtra("Animeter", true);
                    App.mContext.startActivity(intent);
                }
            }
        };
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                // 不管是啥消息，先取刷新一下红点数据

                String noticeType = "";
                try {
                    JSONObject object = new JSONObject(uMessage.extra);
                    noticeType = object.getString("noticeType");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!Utils.isStringEquals(noticeType, "8")) {
                    EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT));
                }


                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                    }
                    Notification.Builder builder = new Notification.Builder(context, "channel_id");
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setContentTitle(uMessage.title)
                            .setContentText(uMessage.text)
                            .setAutoCancel(true);
                    return builder.build();
                } else {
                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setContentTitle(uMessage.title)
                            .setContentText(uMessage.text)
                            .setAutoCancel(true);
                    return builder.build();
                }
            }

            /**
             * 自定义消息的回调方法
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_HOT_RED_DOT));
                super.dealWithCustomMessage(context, msg);
            }
        };

        mPushAgent.setMessageHandler(messageHandler);
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

//

    public static DisplayImageOptions imageLoaderOptions = new DisplayImageOptions.Builder()//
            .showImageOnLoading(R.drawable.ic_default_image_007)    //设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.drawable.ic_default_image_007)  //设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.imgfail)       //设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)                                //设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)                                  //设置下载的图片是否缓存在SD卡中
            .build();                                           //构建完成


    private void initNineGirdViewAdapter() {

        NineGridView.setImageLoader(new GlideImageLoader());

    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                // (Optional) Whether to show thread info or not. Default true
                .showThreadInfo(true)
                // (Optional) How many method line to show. Default 2
                .methodCount(3)
                // (Optional) Hides internal method calls up to offset. Default 5
                .methodOffset(7)
                // (Optional) Changes the log strategy to print out. Default LogCat
//                .logStrategy((priority, tag, message) -> {
//
//                })
                // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .tag("Kris")
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
        Logger.addLogAdapter(new DiskLogAdapter());
    }

    /**
     * Glide 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)
                    .placeholder(R.drawable.ic_default_image_007)
                    .error(R.mipmap.imgfail)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .dontAnimate()
                    .centerCrop()
                    .thumbnail(0.5f)
                    .skipMemoryCache(true)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                            imageView.setImageDrawable(resource);
                        }
                    });
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    public String getOderId() {
        return mOderId;
    }

    public void setOderId(String oderId) {
        mOderId = oderId;
    }

    //增加上报进程控制 判断是否是主进程（通过进程名是否为包名来判断），并在初始化Bugly时增加一个上报进程的策略配置。

    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getAppNameByPID(context, android.os.Process.myPid());

        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "1c0b102fc0", Utils.isDebug(), strategy);
    }

    public static String getAppNameByPID(Context context, int pid) {
        ActivityManager manager
                = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo processInfo : Objects.requireNonNull(manager != null ? manager.getRunningAppProcesses() : null)) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }
}
