package com.ciyuanplus.mobile.manager;

import android.os.Environment;

import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Alen on 2016/11/26.
 */
public class CacheManager {
    private static CacheManager instance = null;

    private CacheManager() {

    }

    public static CacheManager getInstance() {
        if (null == instance) {
            instance = new CacheManager();
        }
        return instance;
    }

    private static long sizeOf(File file) {
        if (file == null || !file.exists()) {
            String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (file.isDirectory()) {
            return sizeOfDirectory(file);
        } else {
            return file.length();
        }
    }

    private static long sizeOfDirectory(File directory) {
        if (directory == null || !directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }
        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }
        long size = 0;
        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            return 0L;
        }
        for (File file : files) {
            size += sizeOf(file);
        }
        return size;
    }

    //获取缓存大小
    //返回格式 xx.xx M
    public String getCacheSize() {
        long size = getExternalCacheSize();
        if (size <= 0) {
            size = 0;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(size / (1024 * 1024d)) + "M";
    }

    /**
     * get External File Cache Size, in bytes. if exception occurs, return -1
     *
     * @return long cache file size
     */
    private long getExternalCacheSize() {
        try {
            File directory = new File(getCacheDirectory());
            if (!directory.exists()) {
                String message = directory + " does not exist";
                throw new IllegalArgumentException(message);
            }
            return sizeOf(directory);
        } catch (Exception e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
            return -1;
        }
    }

    public String getCacheDirectory() {
        return Environment.getExternalStorageDirectory() + Constants.CACHE_FILE_PATH;
    }

    public String getSettleDirectory() {
        return Environment.getExternalStorageDirectory() + Constants.SETTLE_FILE_PATH;
    }


    /**
     * Retrieve the content of url from cache.
     *
     * @param url
     * @return the content corresponding the url. if no data, return null.
     */
    public File getCacheFile(String url) {
        if (null == url) {
            return null;
        }
        String fileName = Utils.md5(url) + Constants.TEMP_CACHE_SUFFIX;
        File file = new File(getCacheDirectory(), fileName);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    public String getCacheNameForFile(String url) {
        String fileName = Utils.md5(url) + Constants.TEMP_CACHE_SUFFIX;
        return getCacheDirectory() + fileName;
    }

    //清除缓存文件， 可能很长时间，请在异步线程调用。
    // 返回值为是否清理成功
    public boolean clearCacheFiles() {
        try {
            File directory = new File(getCacheDirectory());
            return forceDelete(directory);
        } catch (Exception e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
            return false;
        }
    }

    private boolean forceDelete(File file) {
        if (file.isDirectory()) {
            return deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    return false;
                }
                String message = "Unable to delete file: " + file;
                return false;
            }
            return true;
        }
    }

    private boolean deleteDirectory(File directory) {
        if (!directory.exists()) {
            return true;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            return false;
        }
        return true;
    }

    private boolean cleanDirectory(File directory) {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            return true;
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            return false;
        }

        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            return true;
        }


        for (File file : files) {
            forceDelete(file);
        }

        return true;
    }
}
