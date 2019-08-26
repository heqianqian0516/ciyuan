package com.ciyuanplus.mobile.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.statistics.StatisticsManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

/**
 * Created by Alen on 2016/11/26.
 */
public class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * 获取自适应的高度
     * height=imgheight*width/imgWidth;
     *
     * @return
     */
    public static int getSelfHeight(int imgWidth, int imgHeight) {
        WindowManager manage = (WindowManager) App.mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = manage != null ? manage.getDefaultDisplay().getWidth() : 0;
        return imgHeight * width / imgWidth;
    }

    /**
     * Check two string is equals or not.
     *
     * @param str1
     * @return boolean
     * @oaram str2
     */
    public static boolean isStringEquals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }

        if (str1 != null && str2 == null) {
            return false;
        }

        if (str1 == null) {
            return false;
        }

        return str1.equals(str2);
    }

    /**
     * Check a string is empty or not.
     *
     * @param str
     * @return boolean
     */
    public static boolean isStringEmpty(String str) {
        return null == str || str.length() == 0 || str.trim().length() == 0 || isStringEquals(str, "null");
    }


    /**
     * 是否是有效的密码， 密码必须要6-16未
     */
    public static boolean isValidPassword(String password) {
        return password.length() >= 6 && password.length() <= 16;
    }

    /**
     * Check the url is valid or not.
     *
     * @param url the url need check
     * @return boolean
     */
    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            return false;
        }
    }

    /**
     * MD5 encryption: 32 bits
     *
     * @param str plan text
     * @return cipher text
     */
    public static String md5(String str) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String hexTempString = Integer
                        .toHexString(0xFF & aMessageDigest);
                if (hexTempString.length() == 1)
                    hexString.append("0").append(hexTempString);
                else
                    hexString.append(hexTempString);
            }
            return hexString.toString();
        } catch (Exception e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }

        return "";
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) App.mContext.getSystemService(Context.WINDOW_SERVICE);
        return wm != null ? wm.getDefaultDisplay().getWidth() : 0;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) App.mContext.getSystemService(Context.WINDOW_SERVICE);
        return wm != null ? wm.getDefaultDisplay().getHeight() : 0;
    }

    /**
     * 获取dpi
     *
     * @return float
     */
    public static float getDispalyDensity() {
        WindowManager manage = (WindowManager) App.mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        if (manage != null) {
            manage.getDefaultDisplay().getMetrics(localDisplayMetrics);
        }
        return localDisplayMetrics.density;
    }

    /**
     * 将dip值转换为px值
     *
     * @param paramFloat （dip 值）
     * @return
     */
    public static int px2dip(float paramFloat) {
        WindowManager manage = (WindowManager) App.mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        if (manage != null) {
            manage.getDefaultDisplay().getMetrics(localDisplayMetrics);
        }
        int result = (int) (paramFloat / localDisplayMetrics.density);
        return result == 0 ? (int) paramFloat : result;
    }

    /**
     * 将dip值转换为px值
     *
     * @param paramFloat （dip 值）
     * @return
     */
    public static int dip2px(float paramFloat) {
        WindowManager manage = (WindowManager) App.mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        if (manage != null) {
            manage.getDefaultDisplay().getMetrics(localDisplayMetrics);
        }
        int result = (int) (paramFloat * localDisplayMetrics.density);
        return result == 0 ? (int) paramFloat : result;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = App.mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.mContext.getPackageName(), 0);
            String version = info.versionName;
            return "V" + version;
        } catch (Exception e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
            return "V1.0.0";
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本code
     */
    public static int getVersionCode() {
        try {
            PackageManager manager = App.mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.mContext.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
            return 1000;
        }
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)", idCard);
    }

    //是否是有效手机号
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1]\\d{10}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }


    public static String getFormattedTimeString(String time) {
        if (Utils.isStringEmpty(time)) return "";
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = format.format(date);
        long delta = new Date().getTime() - date.getTime();
        if (delta < 60000L) {
            long seconds = delta / 1000L;
            return (seconds <= 0 ? 1 : seconds) + "秒前";
        }
        if (delta < 3600000L) {
            long minutes = delta / 60000L;
            return minutes + "分钟前";
        }
        if (delta < 24L * 3600000L) {
            long hours = delta / 3600000L;
            return hours + "小时前";
        }
        return result;
    }

    public static String getFormattedTimeString(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = format.format(date);
        long delta = new Date().getTime() - date.getTime();
        if (delta < 60000L) {
            long seconds = delta / 1000L;
            return (seconds <= 0 ? 1 : seconds) + "秒前";
        }
        if (delta < 3600000L) {
            long minutes = delta / 60000L;
            return minutes + "分钟前";
        }
        if (delta < 24L * 3600000L) {
            long hours = delta / 3600000L;
            return hours + "小时前";
        }
        return result;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date().getTime());
    }


    public static String getCurrentTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date().getTime());
    }

    // 获取当前进程名
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager != null ? activityManager
                .getRunningAppProcesses() : null) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取渠道名
     *
     * @return 如果没有获取成功，那么返回值为空
     * <p>
     * 以后改为Walle 打包
     */
    public static String getChannelName() {
        String channelName = "guanfang";
        try {
            PackageManager packageManager = App.mContext.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(App.mContext.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }
        return Utils.isStringEmpty(channelName) ? "guanfang" : channelName;
    }


    /**
     * 关键字高亮变色
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字
     * @return
     */
    public static SpannableString matcherSearchTitle(int color, String text,
                                                     String keyword) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF || codePoint >= 0xE000 && codePoint <= 0xFFFD;
    }


    /**
     * 判断当前是否是测试环境
     */
    public static boolean isDebug() {
       /* return App.mContext.getApplicationInfo() != null &&
                (App.mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;*/
         return false;
    }

    // 判断是否是正常的日期格式
    public static boolean isDate(String str_input, String rDateFormat) {
        if (!isStringEmpty(str_input)) {
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str_input));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 规范化输出 距离
     */
    public static String formateDistance(double distance) {
        String result;
        int dis = (int) (distance);
        if (dis > 1000) {
            result = (int) (distance / 1000) + "km";
        } else {
            result = dis + "m";
        }
        return result;
    }

    /**
     * 规范化输出 数量
     * 小于1000  直接显示
     * 大于1000  小于10000 显示 x.xx K
     * 大于10000  显示 x.xx W
     */
    public static String formateNumber(int number) {
        String result;
        if (number < 1000) {
            result = number + "";
        } else if (number < 10000) {
            result = number / 1000 + "." + (number % 1000) / 100 + "K";
        } else {
            result = number / 10000 + "." + (number % 10000) / 1000 + "W";
        }
        return result;
    }


    /****
     * 获取   年   月    日   周几
     * 封装成字符串数组 输出
     * */
    public static String[] formateDate(long time) {
        String[] result = new String[4];
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        result[0] = c.get(Calendar.YEAR) + "";
        result[1] = c.get(Calendar.MONTH) + 1 + "";
        result[2] = c.get(Calendar.DAY_OF_MONTH) + "";
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                result[3] = "星期天";
                break;
            case 2:
                result[3] = "星期一";
                break;
            case 3:
                result[3] = "星期二";
                break;
            case 4:
                result[3] = "星期三";
                break;
            case 5:
                result[3] = "星期四";
                break;
            case 6:
                result[3] = "星期五";
                break;
            case 7:
                result[3] = "星期六";
                break;
        }

        return result;
    }


}
