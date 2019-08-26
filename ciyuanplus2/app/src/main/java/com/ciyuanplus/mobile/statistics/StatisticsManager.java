package com.ciyuanplus.mobile.statistics;

import com.orhanobut.logger.Logger;

/**
 * Created by Alen on 2017/7/3.
 */

public class StatisticsManager {
    private static final String[] ETC_S = new String[]{"s0", "s1", "s2", "s3", "s4"};//打点

    /**
     * 非实时打点的接口
     * ALERT：如果出现打点s0没有，s1有的情况，需要这样传值（"", "s1的值"），不能直接使用("s1的值")
     *
     * @param module 定义的vaule值,必须传
     * @param option 定义的vaule值,必须传
     * @param args   可变参数，最多5个
     */
    public static void onEventInfo(String module, String option, String... args) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            int size = args.length;
//            int limit = ETC_S.length;//防止出现越界
//            for (int counter = 0; counter < size && counter < limit; counter++) {
//                jsonObject.put(ETC_S[counter], Utils.isStringEmpty(args[counter]) ? "" : args[counter]);//做空字符串异常处理
//            }
//            //对没有的进行补偿
//            for (int counter = size; counter < limit; counter++) {
//                jsonObject.put(ETC_S[counter], "");
//            }

            //打点
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("version", Utils.getVersion());
//            map.put("os", "android");
//            map.put("phoneType", SystemUtil.getSystemModel());
//            map.put("imei", SystemUtil.getIMEI(App.mContext));
//            map.put("systemVersion", SystemUtil.getSystemVersion() + "");
//            map.put("screenWidth", Utils.getScreenWidth() + "");
//            map.put("screenHeight", Utils.getScreenHeight() + "");
//            map.put("dpi", Utils.getDispalyDensity() + "");
//            map.put("time", new Date().getTime());
//            if(!Utils.isStringEmpty(UserInfoData.getInstance().getUserInfoItem().uuid))
//                map.put("userUuid", UserInfoData.getInstance().getUserInfoItem().uuid);
//            map.put("dot", "");

//            LogHandlerManager.onEvent(module, option, jsonObject.toString());

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 异常信息上传
     */
    public static void onErrorInfo(String errorinfo, String etc) {
//        LogHandlerManager.onError(errorinfo, etc);

    }

    public static String getStackMsg(Exception e) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackArray = e.getStackTrace();
        Logger.d(e.getMessage());
        for (StackTraceElement element : stackArray) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
