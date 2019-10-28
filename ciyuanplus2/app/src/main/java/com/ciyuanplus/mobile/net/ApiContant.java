package com.ciyuanplus.mobile.net;

import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.orhanobut.logger.Logger;

/**
 * Created by Alen on 2017/2/11.
 */
public class ApiContant {
    //次元币比商城详情规格
    public static final String SELECT_PROD_DETAIL_BYPRODID="/api/server/prod/selectProdDetailByProdId";
    //次元币商城列表
    public static final String SELECT_INTEGRAL_PRODL_LIST="/api/server/prod/selectIntegralProdList";
    //查询今天是否签到
    public static final String GET_IS_SIGN="/api/user/sign/isSign";
    //用户签到
    public static final String USER_SIGN="/api/user/sign/userSign";
    //查询用户连续签到天数
    public static final String SELECT_USER_CONTINUOUS_SIGN="/api/user/sign/selectUserContinuousSign";
    //查询用户本月补签次数
    public static final String SELECT_USER_RETROACTIVE_NUM="/api/user/sign/selectUserRetroactiveNum";
    //查询用户用户签到日期
    public static final String SELECT_USER_SIGN_DATE="/api/user/sign/selectUserSignDate";
    //补签
    public static final String SIGNED_SUPPLEMENT="/api/user/sign/signedSupplement";
    //查看是否完成了新手任务
    public static final String GET_USER_NEWTASK_STATUS="/api/user/sign/getUserNewTaskStatus";
       //获取总积分
    public static final String GET_INTEGRAL_DETAILS="/api/user/sign/getIntegralDetails";
      //查询用户资料完善情况以及发帖数量
    public static  final String GET_NEWTASK_DATA_DETAILS="/api/user/sign/getNewTaskDataDetails";
    // 浏览商城获得积分
    public static  final  String SELECT_SHOP_INTEGRAL="/api/user/sign/selectShopIntegral";
    //POST 分享获得积分
    public  static  final String SHARE_INTEGRAL="/api/user/sign/shareIntegral";
    //新手任务领取积分POST /api/user/sign/newTaskIntegral
    public  static  final String NEW_TASK_INTEGRAL="/api/user/sign/newTaskIntegral";
    //查询用户当天任务次数
    public  static  final String GET_USER_TODAY_TASKNUM="/api/user/sign/getUserTodayTaskNum";
    //查看用户今日获得积分
    public static  final String GET_USER_INTEGRALBY_TODAY="/api/user/sign/getUserIntegralByToday";
    //参加活动
    public static final String REQUEST_ACTIVITY_LIST = "/api/event/getEventList";
    //排行榜
    ///api/posting/getPostLikeCount
    public static final String REQUEST_GET_POST_LIKE_COUNT = "/api/posting/getPostLikeCount";
    //排行榜第一名api/posting/getPostLikeMax
    public static final String REQUEST_GET_POST_LIKE_MAX = "/api/posting/getPostLikeMax";
    //活动详情/api/event/getEventDetail
    public static final String REQUEST_GET_EVENT_DETAILS = "/api/event/getEventDetail";
    //获取帖子详情/api/posting/getPostDetail
    public static final String REQUEST_GET_POST_DETAIL = "/api/posting/getPostDetail";
    //api/posting/
    public static final String REQUEST_GET_POSTING = "/api/posting/";
    ///api/posting/addEventInfo
    public static final String REQUEST_ADD_EVENT_INFO = "/api/posting/addEventInfo";
    //卖家订单列表
    public static final String REQUEST_ORDER_LIST = "/api/prodOrder/getProdOrders";//订单列表
    public static final String REQUEST_ORDER_DETAIL = "/api/prodOrder/getOrderDetail";//订单详情
    public static final String CANCEL_ORDER = "/api/prodOrder/cancelOrder";//取消订单
    public static final String REQUEST_SELLER_COMFIRM_ORDER = "/api/prodOrder/confirmOrderSeller";//买家确认发货（接单）

    //http://59.110.142.102:40015/server/swagger-ui.html  接口文档地址
    //  public static String URL_HEAD = "http://182.92.154.225:9999";// http://59.110.142.102:9999
    public static String RELEASE_URL_HEAD = "http://39.98.205.18:9999";
    // 192.168.1.173   http://39.98.84.95:9999
    public static String DEBUG_URL_HEAD = "http://39.98.84.95:9999";
    public static String URL_HEAD = RELEASE_URL_HEAD;


    public static final String WEB_HEAD_STSRT = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,user-scalable=0\"><title>次元PLUSApp</title><style type=\"text/css\">img{width: 100%;}</style></head><body>\n";
    public static final String WEB_HEAD_END = "</body></html>";
    public static String WEB_DETAIL_VIEW_URL = "http://ciyuanplus.ciyuanguiji.com/h5/";
    //http://ciyuanplus.ciyuanguiji.com/h5/test/
    public static String WEB_DETAIL_VIEW_URL_TB = "http://ciyuanplus.ciyuanguiji.com/h5/";
    public static String WEB_DETAIL_CESHI="http://39.98.84.95/h5/";
    //网络请求地址
    public static final String REQUEST_LOGIN_URL = "/api/user/login";
    public static final String REQUEST_AUTO_LOGIN = "/api/user/autoLoginWithToken";
    public static final String REQUEST_USER_ADD_COMMUNITY_URL = "/api/user/addCommunity";
    public static final String REQUEST_ADD_COMMUNITY_URL = "/api/community/addCommunity";
    public static final String REQUEST_SEND_SMS_CODE_URL = "/api/sms/sendSms";
    public static final String REQUEST_LOGOUT_URL = "/api/user/loginOut";
    public static final String REQUEST_UPLOAD_FILE_URL = "/api/files/upload";
    public static final String REQUEST_UPLOAD_FILES_URL = "/api/files/uploads";
    public static final String REQUEST_CHANGE_PHOTO_URL = "/api/user/editPhoto";
    public static final String REQUEST_CHANGE_NAME_URL = "/api/user/nickname";
    public static final String REQUEST_CHANGE_PHONE_URL = "/api/user/changeMobile";
    public static final String REQUEST_CHANGE_SEX_URL = "/api/user/editSex";
    public static final String REQUEST_CHANGE_PASSWORD_URL = "/api/user/editPassword";
    public static final String REQUEST_ADD_MY_NEWS_URL = "/api/posting/addPostInfo";
    public static final String REQUEST_UPDATE_MY_NEWS_URL = "/api/posting/editPostInfo";
    public static final String REQUEST_FORGET_PASSWORD_URL = "/api/user/forgetPassword";
    public static final String REQUEST_COMMUNITY_LIST_URL = "/api/user/getCommunityList";
    public static final String REQUEST_DELETE_COMMUNITY_URL = "/api/user/removeCommunity";
    public static final String REQUEST_SET_DEFAULT_COMMUNITY_URL = "/api/user/setCurrentCommunity";
    public static final String REQUEST_REGISTER_CONTRACT_URL = "/api/main/registerContract";
    public static final String REQUEST_ABOUT_US_URL = "/api/main/aboutUs";
    public static final String REQUEST_RONGYUN_TOKEN_URL = "/api/user/getRongYunUserToken";
    public static final String REQUEST_DELETE_MY_PUBLISH_POST_URL = "/api/posting/delPostInfo";
    public static final String REQUEST_POST_DETAIL_URL = "/api/posting/getPostDetail";
    public static final String REQUEST_GET_POST_COMMENTS_LIST_URL = "/api/posting/getPostReplyCommentList";
    public static final String REQUEST_POST_COMMENTS_URL = "/api/posting/addPostComment";
    public static String REQUEST_CANCEL_DISLIKE_URL = "/api/posting/cancelDislikePostInfo";
    public static final String REQUEST_CANCEL_LIKE_URL = "/api/posting/cancelLikePostInfo";
    public static final String REQUEST_LIKE_URL = "/api/posting/likePostInfo";
    public static String REQUEST_DISLIKE_URL = "/api/posting/dislikePostInfo";
    public static final String REQUEST_CANCEL_COLLECT = "/api/posting/cancelCollectPostInfo";
    public static final String REQUEST_COLLECT = "/api/posting/collectPostInfo";
    public static final String REQUEST_GET_OTHER_INFO_URL = "/api/posting/getUserInfo";
    public static final String REQUEST_FOLLOW_USER_URL = "/api/user/followUser";
    public static final String REQUEST_CANCEL_FOLLOW_POST_URL = "/api/user/cancelFollowUser";
    public static final String REQUEST_FAQ_URL = "/api/main/faq";
    public static final String REQUEST_FOLLOW_LIST_URL = "/api/user/queryMyFollowList";
    public static final String REQUEST_FAN_LIST_URL = "/api/user/queryMyFollowerList";
    public static final String REQUEST_DELETE_COMMENT_URL = "/api/posting/deletePostComment";
    public static final String REQUEST_APP_VERSION_URL = "/api/main/checkVersion";
    public static final String REQUEST_NOTICE_COUNT_URL = "/api/userNotice/queryNoticeCount";
    public static final String REQUEST_USER_NOTICE_LIST_URL = "/api/userNotice/queryUserNoticeList";
    public static final String REQUEST_SYSTEM_NOTICE_DELETE_URL = "/api/userNotice/deleteUserNotice";
    public static final String REQUEST_SYSTEM_NOTICE_READ_URL = "/api/userNotice/setIsRead";
    public static final String REQUEST_FEED_BACK_LIST_URL = "/api/userNotice/queryFeedbackMessageList";
    public static final String REQUEST_FEED_BACK_SEND_URL = "/api/userNotice/submitFeedback";
    public static final String REQUEST_SYSTEM_MESSAGE_DETAIL_URL = "/api/userNotice/querySystemMessageDetail";
    public static final String REQUEST_COMMUNITY_USERS_URL = "/api/user/queryCommunityUsers";
    public static final String REQUEST_LOGIN_USER_INFO_URL = "/api/posting/getLoginUserOtherInfo";
    public static final String REQUEST_GET_PUSH_SETTING_URL = "/api/pushSettings/queryPushSettings";
    public static final String REQUEST_SAVE_PUSH_SETTING_URL = "/api/pushSettings/savePushSettings";
    public static final String REQUEST_SAVE_PUSH_DEVICE_TOKEN_URL = "/api/pushSettings/savePushToken";
    public static final String REQUEST_SYSTEM_MESSAGE_COUNT_URL = "/api/userNotice/querySystemMessageCount";
    public static final String REQUEST_TEMP_USER_NOTICE_LIST_URL = "/api/userNotice/querySystemMessageList";
    public static final String REQUEST_TEMP_SYSTEM_NOTICE_DELETE_URL = "/api/userNotice/deleteSystemMessage";
    public static final String REQUEST_TEMP_SYSTEM_NOTICE_READ_URL = "/api/userNotice/setSystemMessageIsRead";
    public static final String REQUEST_UPLOAD_DOT_INFO_URL = "/api/dot/uploadDotInfo";
    public static final String REQUEST_UPLOAD_ERROR_INFO_URL = "/api/exception/uploadExceptionInfo";
    public static final String REQUEST_GET_USER_INFO = "/api/user/getUserChatResultList";
    public static final String REQUEST_GET_APP_CONFIG = "/api/main/getAppConfig";
    public static String REQUEST_DELETE_USER_NOTICE = "/api/userNotice/deleteUserNotice";
    // V1.0.1版本接口
    public static String REQUEST_ADD_DETAIL_ADDRESS = "/api/user/boundAddress";
    public static final String REQUEST_UPDATE_BRITHDAY = "/api/user/editBirthday";
    public static final String REQUEST_DELETE_DETAIL_ADDRESS = "/api/user/deleteBoundAddress";
    public static final String REQUEST_REPORT_POST = "/api/report/addReport";
    public static final String REQUEST_ADD_STUFF = "/api/stuff/addStuffInfo";
    public static final String REQUEST_STUFF_DETAIL = "/api/stuff/getStuffDetail";
    public static final String REQUEST_WORLD_STUFF_LIST = "/api/stuff/getStuffList";
    public static final String REQUEST_SEARCH_STUFF_LIST = "/api/stuff/searchStuffKeyword";
    public static final String REQUEST_MY_STUFF_LIST = "/api/stuff/queryMyStuffList";
    public static final String REQUEST_COLLECT_STUFF_LIST = "/api/stuff/queryCollectStuffList";
    public static final String REQUEST_USER_STUFF_LIST = "/api/stuff/queryUserStuffList";
    public static final String REQUEST_UPDATE_STUFF = "/api/stuff/editStuffInfo";
    public static final String REQUEST_CHANGE_STUFF_STATUS = "/api/stuff/changeStuffStatus";
    public static final String REQUEST_SEARCH_ALL_USER = "/api/posting/queryWorldUsers";
    public static final String REQUEST_UPDATE_OLD_POST_URL = "/api/posting/editPostInfo";
    // V1.2.0 版本
    public static String REQUEST_CANCEL_INVFD_WIKI_URL = "/api/wiki/cancelInvalidWiki";
    public static String REQUEST_CANCEL_VALID_WIKI_URL = "/api/wiki/cancelValidWiki";

    // V1.2.1 版本
    public static String REQUEST_INVALID_WIKI_URL = "/api/wiki/invalidWiki";
    public static String REQUEST_VALID_WIKI_URL = "/api/wiki/validWiki";
    public static final String REQUEST_GET_BANNER_LIST_URL = "/api/banner/getBannerList";
    public static final String REQUEST_ADD_BROWSE_COUNT_URL = "/api/posting/addPostBrowseCount";
    public static final String REQUEST_GET_BROWSE_COUNT_URL = "/api/posting/getPostBrowseCount";
    //V2.1.0
    public static final String REQUEST_AROUND_BANNER_STUFF_LIST = "/api/stuff/getLdleStuffList";
    //V2.1.3
    public static String REQUEST_USER_BOMB_BOX_INIFO_URL = "/api/user/getUserBombBoxInfo";
    public static final String REQUEST_MY_GIFT_LIST_URL = "/api/user/queryMyGiftList";
    public static final String REQUEST_USER_INFO_RED_DOT_URL = "/api/user/getUserInfoRedDot";
    //V2.2.0
    public static final String REQUEST_LIVE_HOOD_LIST_URL = "/api/livelihood/getLivelihoodList";
    public static final String REQUEST_SQUARE_TAG_LIST_URL = "/api/livelihood/getSquareTagList";
    //V2.3.0
    public static String REQUEST_DAILY_LABEL_URL = "/api/daily/getDailyLabel";
    public static final String REQUEST_COMMUNITY_POST_URL = "/api/posting/getCommunityPostList";
    public static final String REQUEST_WORLD_POST_URL = "/api/posting/getWorldPostList";
    public static final String REQUEST_GET_MY_PUBLISH_POST_URL = "/api/posting/getLoginUserPostInfo";
    public static final String REQUEST_GET_MY_LIKE_POST_URL = "/api/posting/getLoginUserLikePostInfo";
    //精品列表/api/posting/getHighLightList
    public static final String REQUEST_GET_HIGHT_LIGHT_LIST = "/api/posting/getHighLightList";
    // 获取用户主页新鲜事列表
    public static final String REQUEST_GET_OTHER_PUBLISH_POST_URL = "/api/posting/getUserPostInfo";
    public static final String REQUEST_CHECK_USER_NICK_NAME_URL = "/api/user/checkUserNickname";
    public static final String REQUEST_GET_COMMUNITY_WIKI_URL = "/api/wiki//getCommunityWiki";
    public static final String REQUEST_ADD_PLACE_URL = "/api/place/addPlace";
    public static final String REQUEST_ADD_MY_POST_URL = "/api/posting/addPostInfo";
    public static final String REQUEST_UPDATE_MY_POST_URL = "/api/posting/editPostInfo";
    public static final String REQUEST_GET_POST_COMMENTS_URL = "/api/posting/getPostCommentList";
    public static final String REQUEST_GET_COMMENTS_DETAIL_URL = "/api/posting/V2/getPostCommentDetail";
    public static final String REQUEST_SUBMMIT_POST_COMMENT_SCORE_URL = "/api/posting/postCommentScore";
    public static final String REQUEST_GET_ALL_WISH_URL = "/api/place/getAllWishs";
    public static final String REQUEST_GET_ALL_RATE_URL = "/api/place/getPlaceComment";
    public static final String REQUEST_GET_PLACE_DETAIL_URL = "/api/place/getPlaceDetail";
    public static final String REQUEST_GET_USER_PLACE_COMMENT_URL = "/api/place/getUserPlaceComment";
    public static final String REQUEST_CANCEL_LIKE_POST_COMMENT_URL = "/api/posting/cancelLikePostCommentInfo";
    public static final String REQUEST_LIKE_POST_COMMENT_URL = "/api/posting/likePostCommentInfo";
    //V2.3.3
    public static final String REQUEST_OTHER_LOGIN_URL = "/api/user/otherLogin";
    //v2.4.0
    public static final String REQUEST_GET_RANDOM_PHOTO_AND_NAME_URL = "/api/user/getRandomPhotoAndNickName";
    public static final String REQUEST_REGISTER_FORMAL_USER_URL = "/api/user/register";
    public static final String REQUEST_QQ_UNION_ID_URL = "https://graph.qq.com/oauth2.0/me";
    public static final String REQUEST_BIND_OTHER_PLATFORM_URL = "/api/user/bindingMobile";

    //获取广场接口
    public static final String REQUEST_all = "/api/posting/getWorldPostList";
    //获取小区信息
    public static final String REQUEST_plot = "/api/posting/getCommunityPostList";
    //获取实时房价
    public static final String REQUEST_homeprice = "/api/posting/queryRealTimeHousePrice";
    //发布问答求助接口
    public static final String REQUEST_help = "/api/posting/addInterlocution";
    //采纳接口
    //点赞新鲜事
    public static final String REQUEST_zan = "/api/posting/likePostInfo";
    //更新浏览量
    // 三方帐号绑定情况
    public static String REQUEST_OTHER_PLATFORM_BIND_LIST = "/api/user/bindList";
    public static String REQUEST_HEAD_LINE_LIST = "/api/useRecom/getUserRecomList";

    //绑定
    public static final String REQUEST_NEED_BIND_WECHAT = "/api/user/getUserActivityGameBoxInfo"; //获取用户游戏活动弹框信息
    public static final String REQUEST_BIND_WECHAT = "/api/user/bindWX";//绑定微信
    public static final String REQUEST_BIND_QQ = "/api/user/bindQQ";//绑定qq
    public static final String REQUEST_BIND_Sina = "/api/user/bindSina";//绑定微博


    public static final String REQUEST_USER_SOCIAL_COUNT = "/api/posting/getUserSocialCountInfo";//
    public static final String REQUEST_USER_LIKE_LIST = "/api/posting/getUserLikePostInfo";//

    //商城
    public static final String REQUEST_COMMODITY_TYPE = "/api/server/prod/getProdCategorys";
    public static final String REQUEST_COMMODITY_LIST = "/api/server/prod/selectProdList";

    //支付宝
    public static final String REQUEST_ALI_PAY_ORDER_INFO = "/api/prodOrder/getPaymentInfoAlipay";// 获取支付宝预支付信息"
    public static final String REQUEST_WEIXIN_ORDER_INFO = "/api/prodOrder/getPaymentInfo"; //获取微信预支付信息;

    //购物车
    public static final String REQUEST_ADD_SHOP_CART_ITTEM = "/api/server/cartItem/addCartItem";// 添加或者修改购物车数量
    public static final String REQUEST_REMOVE_SHOP_CART_ITEM = "/api/server/cartItem/deleteCartItem";// 删除购物车（支持批量删除）

    //修改签名

    public static final String REQUEST_CHANGE_SIGN = "/api/user/editPersonalizedSignature";// 个性签名修改
    public static final String REQUEST_ADDRESS_LIST = "/api/server/shoppingAddress/getShoppingAddress";// 地址

    //支付详情
    public static final String REQUEST_PAY_DETAIL = "/api/prodOrder/getProdOrderDetailByWXTransactionId";// 查询订单详情-根据微信支付流水

    // 设置一下测试和正式环境的ip地址
    // 需要在app启动的时候马上进行设置
    public static void setServerHost() {
        if (Utils.isDebug()) {
            int testServerType = SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SERVER_SET, Constants.SHARED_PREFERENCES_CHANGE_DEBUG_SERVER, 0);
            if (testServerType == 1) {
                //正式环境
                URL_HEAD = RELEASE_URL_HEAD;
                WEB_DETAIL_VIEW_URL = "http://ciyuanplus.ciyuanguiji.com/h5/";
            } else {
                //测试
                URL_HEAD = DEBUG_URL_HEAD;
                WEB_DETAIL_VIEW_URL = "http://ciyuanplus.ciyuanguiji.com/h5/test/";
            }

        } else {
            //正式环境
            URL_HEAD = RELEASE_URL_HEAD;
            WEB_DETAIL_VIEW_URL = "http://ciyuanplus.ciyuanguiji.com/h5/";
        }

        Logger.d("URL_HEAD = " + URL_HEAD);
        Logger.d("WEB_DETAIL_VIEW_URL = " + WEB_DETAIL_VIEW_URL);
    }
}
