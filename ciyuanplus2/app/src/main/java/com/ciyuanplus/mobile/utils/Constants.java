package com.ciyuanplus.mobile.utils;


import com.ciyuanplus.mobile.manager.SharedPreferencesManager;

/**
 * Created by Alen on 2017/4/20.
 */

public class Constants {
    public static final String CACHE_FILE_PATH = "/milin/cache/";
    public static final String SETTLE_FILE_PATH = "/milin/files/";
    public static final String TEMP_CACHE_SUFFIX = ".cache";
    public static final String ZONE_SEARCH_KEY_WORD = "小区";
    public static final String ZONE_SEARCH_KEY_TYPE = "120300|120301|120302";// 高德地图定义的住宅相关的搜索类型
    public static final String ALL_LOCATION_SEARCH_KEY_TYPE = "汽车服务 | 汽车销售 | 汽车维修 | 摩托车服务 | 餐饮服务" +
            " | 购物服务 | 生活服务 | 体育休闲服务 | 医疗保健服务 | 住宿服务 | 风景名胜" +
            " | 商务住宅 |政府机构及社会团体 | 科教文化服务 | 交通设施服务 | 金融保险服务 " +
            "| 公司企业 | 道路附属设施 | 地名地址信息 | 公共设施";// 餐饮服务
    public static final String FOOD_LOCATION_SEARCH_KEY_TYPE = "050000";// 餐饮服务
    public static final String LIVE_LOCATION_SEARCH_KEY_TYPE = "010000|020000|030000|040000|060000|070000" +
            "|080000|090000|100000|110000|120000|140000|150000|160000";
    //汽车服务 | 汽车销售 | 汽车维修 | 摩托车服务 | 购物服务 | 生活服务 | 体育休闲服务
    // | 医疗保健服务 | 住宿服务 | 风景名胜 | 商务住宅 | 科教文化服务 | 交通设施服务 | 金融保险服务

    public static String WX_APP_ID = "wx018ce9bef8b8acbf";
    public static String WX_APPSECRET = "7ca9c7e3e3dc24cd22314f358be9c70f";
    public static String QQ_APP_ID = "1107996120";
    public static String QQ_APPSECRET = "54L4Jzc3elPaIpBr";
    public static String WB_APP_ID = "1545538292";
    public static String WB_APPSECRET = "70ecb21fcb0e964526fc550d515ca322";

    //微信支付
    // APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static final String APP_ID = "wx018ce9bef8b8acbf"; //AppID：wx018ce9bef8b8acbf Android平台
//    应用下载地址：未填写

//    应用签名：8760868dba365f6df029f85ee2970cb1

//    包名：com.asdfghjjkk.superiordiaryokdsakd

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }
    public static final String INTENT_OPEN_TYPE = "activityType";
    public static final String BehaviourLogPath = "/milin/behaviour_report/";
    public static final String ErrorLogPath = "/milin/error_report/";
    public static final String SHARED_PREFERENCES_SET = "common_setting";
    public static final String SHARED_PREFERENCES_SERVER_SET = "server_setting";
    public static final String SHARED_PREFERENCES_HAS_OPEN = "has_open";
    public static final String SHARED_PREFERENCES_HAS_GUIDE = "has_guide";
    public static final String SHARED_PREFERENCES_START_POST_HAS_GUIDE = "start_post_has_guide";
    public static final String SHARED_PREFERENCES_START_STUFF_HAS_GUIDE = "start_stuff_has_guide";
    public static final String SHARED_PREFERENCES_MINE_CENTER_HAS_GUIDE = "mine_center_has_guide";
    public static final String SHARED_PREFERENCES_POST_TYPE_DICT = "post_type_dict";
    public static final String SHARED_PREFERENCES_WIKI_TYPE_DICT = "wiki_type_dict";
    public static final String SHARED_PREFERENCES_HAS_OPEM_SEARCH_VIEW = "has_open_search_view";
    public static final String SHARED_PREFERENCES_LOGIN_ISLOGIN = "login_islogin";
    public static final String SHARED_PREFERENCES_LOGIN_ACCOUNT = "login_account";
    public static final String SHARED_PREFERENCES_LOGIN_PASSWORD = "login_password";
    public static final String SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY = "login_user_session_key";
    public static final String SHARED_PREFERENCES_LOGIN_USER_RONG_SESSION_KEY = "login_user_rong_session_key";
    public static final String SHARED_PREFERENCES_LOGIN_USER_ID = "login_user_id";
    public static final String SHARED_PREFERENCES_TEMP_USER_ID = "temp_user_id";
    public static final String SHARED_PREFERENCES_TEMP_USER_RONG_SESSION_KEY = "temp_user_rong_session_key";
    public static final String SHARED_PREFERENCES_TEMP_USER_SEX = "temp_user_sex";
    public static final String SHARED_PREFERENCES_USRER_INFO = "user_info";
    public static final String SHARED_NOTICE_SETTING_COMMENT_INFO = "notice_comment";
    public static final String SHARED_NOTICE_SETTING_FANS_INFO = "notice_fans";
    public static final String SHARED_NOTICE_SETTING_SYSTEM_INFO = "notice_system";
    public static final String SHARED_NOTICE_SETTING_CHAT_INFO = "notice_chat";
    public static final String SHARED_UMENG_DEVICE_TOKEN = "umeng_device_token";
    public static final String SHARED_UPDATE_IGNORE_VERSION = "update_ignore_version";
    public static final String SHARED_SHARE_TITLE = "share_title";
    public static final String SHARED_SHARE_LINK = "share_link";
    public static final String SHARED_SHARE_CONTENT = "share_content";
    public static final String SHARED_BANNER_LIST_CONTENT = "banner_list_content";
    public static final String SHARED_PREFERENCES_FLASH_IMAGE = "flash_image";
    public static final String SHARED_PREFERENCES_FLASH_TIMEOUT = "flash_timeout";
    public static final String SHARED_PREFERENCES_FLASH_IMAGE_PATH = "flash_image_path";
    public static final String SHARED_PREFERENCES_FLASH_LINK_URL = "flash_image_link";
    public static final String SHARED_PREFERENCES_DAILY_POP_UP_SHOW_TIME = "daily_pop_up_show_time";
    public static final String SHARED_PREFERENCES_CHANGE_DEBUG_SERVER = "change_debug_server";
    public static final String SHARED_PREFERENCES_LOGIN_TYPE = "login_type";
    public static final String SHARED_PREFERENCES_OTHER_PLATRORM_ACCOUNT = "other_platform_account";

    //以下是 REQUEST_CODE
    public static final int REQUEST_CODE_SELECT_PITCURE_OPTION = 10001;
    public static final int REQUEST_CODE_REQUEST_CAMERA_OPTION = 10002;
    public static final int REQUEST_CODE_SELECT_IMAGE = 10003;
    public static final int REQUEST_CODE_SEARCH_COMMUNITY = 10004;
    public static final int REQUEST_CODE_SELECT_EDIT_DELETE_NEWS = 10005;
    public static final int REQUEST_CODE_SELECT_CONTACTS = 10006;
    public static final int REQUEST_CODE_SELECT_DELETE_COMMENT = 10007;
    public static final int REQUEST_CODE_SELECT_STUFF_STATE = 10008;
    public static final int REQUEST_CODE_SELECT_STUFF_COMMUNITY = 10009;
    public static final int REQUEST_CODE_SELECT_SEARCH_TYPE = 10010;
    public static final int REQUEST_CODE_IMAGE_CROP_CODE = 10011;
    public static final int REQUEST_CODE_POP_INVITE_SHARE_CODE = 10012;
    public static final int REQUEST_CODE_INPUT_USER_NAME = 10013;
    public static final int REQUEST_CODE_SELECT_POST_LOCATION = 10014;
    public static final int REQUEST_SELECT_SHOW_TYPE = 10015;

    //以下是 Intent 传递过程中使用的常量
    public static final String INTENT_ACTIVITY_UUID = "uuid";

    public static final String INTENT_ACTIVITY_TYPE = "activity_type";
    public static final String INTENT_UDPATE_NENWS_ITEM = "edit_news_item";
    public static final String INTENT_NEWS_ITEM = "news_item";
    public static final String INTENT_BANNERS_ITEM = "banner_item";
    public static final String INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES = "full_screen_images";
    public static final String INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX = "full_screen_index";
    public static final String INTENT_MY_FRIENDS_TYPE = "my_friends_type";
    public static final String INTENT_NEWS_ID_ITEM = "news_id";
    public static final String INTENT_NEWS_IS_MINE = "is_mine";
    public static final String INTENT_USER_ID = "other_user_id";
    public static final String INTENT_CONTACTS_ITEM = "contacts_item";
    public static final String INTENT_SEARCH_CONTENT = "search_content";
    public static final String INTENT_SEARCH_CITY = "search_city";
    public static final String INTENT_COMMUNITY_ITEM = "community_item";
    public static final String INTENT_COMMUNITY_NAME = "community_name";
    public static final String INTENT_COMMUNITY_ID = "community_id";
    public static final String INTENT_COMMENT_ITEM = "comment_item";
    public static final String INTENT_SYSTEM_MESSAGE_ID = "system_message_id";
    public static final String INTENT_SEARCH_NEWS_TYPE = "search_news_type";
    public static final String INTENT_POST_HAS_COLLECTED = "post_has_collected";
    public static final String INTENT_POST_ID = "post_id";
    public static final String INTENT_POST_STATUS = "post_status";
    public static final String INTENT_COMMENT_ID_ITEM = "comment_id";
    public static final String INTENT_RENDER_TYPE = "renderType";
    public static final String INTENT_BIZE_TYPE = "bizType";
    public static final String INTENT_OPEN_URL = "open_url";
    public static final String INTENT_IS_BANNER = "is_banner";
    public static final String INTENT_IS_STUFF = "is_stuff";
    public static final String INTENT_POST_TYPE_ITEM = "post_type_item";
    public static final String INTENT_POSITION_ITEM = "position_item";
    public static final String INTENT_HIDE_TAG = "hide_tag";
    public static final String INTENT_BOMB_ITEM = "bomb_item";
    public static final String INTENT_DAILY_LABEL = "daily_label";
    public static final String INTENT_JS_WEB_VIEW_PARAM = "js_web_view_param";
    public static final String INTENT_FILE_PATH = "file_path";
    public static final String INTENT_PLACE_ID = "place_id";
    public static final String INTENT_WIKI_ID = "wiki_id";
    public static final String INTENT_USER_NAME = "user_name";
    public static final String INTENT_USER_PHOTO = "user_photo";
    public static final String INTENT_USER_PHONE = "user_phone";
    public static final String INTENT_USER_ADDRESS = "user_address";
    public static final String INTENT_OTHER_PLATFORM_ID = "other_platform_id";
    public static final String INTENT_OTHER_PLATFORM_TYPE = "other_platform_type";
    public static final String INTENT_OTHER_PLATFORM_HAS_PASSWORD = "other_platform_has_password";
    public static final String INTENT_SEARCH_TYPE = "search_type";
    public static final String INTENT_IS_YOURSELF = "is_yourself";
    public static final String INTENT_BIND_MOBILE = "bind_mobile";
    public static final String INTENT_SIGN = "user_sign";
    public static final String INTENT_SHOW_TITLE_BAR = "web_show_title_bar";
    public static final String INTENT_IS_EDIT_MODE = "is_edit_mode";
    public static final String INTENT_ADDRESS_ID = "address_id";
    public static final String INTENT_TITLE_BAR_TITLE = "intent_title_bar_title";
    public static final String INTENT_PAY_TOTAL_MONEY = "pay_total_money";
    public static final String TAO_BAO_LINK = "taobaoLink";
    public static final String COUPON_LINK = "couponLink";
    public static final String  PROD_ID="prodId" ;
    public static final String SHARED_PREFERENCES_SQUARE_HOME_HAS_GUIDE = "mine_center_has_guide";

    // 以下是EventCenter 需要用到的信息关注更新
    public static final int EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE = 30001; //头像更新
    public static final int EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH = 30002;
    public static final int EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH = 30003;
    public static final int EVENT_MESSAGE_UPDATE_HOT_RED_DOT = 30004;
    public static final int EVENT_MESSAGE_UPDATE_RONG_HOT_RED_DOT = 30005;
    public static final int EVENT_MESSAGE_UPDATE_MINE_NEWS_LIST = 30007;
    public static final int EVENT_MESSAGE_UPDATE_PEOPLE_STATE = 30010; //
    public static final int EVENT_MESSAGE_UPDATE_CHAT_CONVERSATION_LIST_ITEM = 30013;
    public static final int EVENT_MESSAGE_UPDATE_NOTICE_SETTING = 30014;
    public static final int EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST = 30015;
    public static final int EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST = 30016;
    public static final int EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT = 30008;
    public static final int EVENT_MESSAGE_REFRESH_NEWS_ITEM = 30019;
    public static final int EVENT_MESSAGE_UPDATE_STUFF_ITEM = 30017;
    public static final int EVENT_MESSAGE_UPDATE_NEWS_DELETE = 30009;
    public static final int EVENT_MESSAGE_ADD_COMMENT_OR_REPLY = 30011;
    public static final int EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY = 30012;
    public static final int EVENT_MESSAGE_UPDATE_BROWSE_COUNT = 30018;
    public static final int EVENT_MESSAGE_UPDATE_STUFF_LIST = 30021;
    public static final int EVENT_MESSAGE_USER_FORCE_LOGOUT = 30020;
    public static final int EVENT_MESSAGE_ITEM_BEEN_MARKED = 30022;
    public static final int EVENT_MESSAGE_ITEM_BEEN_COLLECTED = 30023;
    public static final int EVENT_MESSAGE_ITEM_CANCEL_COLLECTED = 30024;
    public static final int EVENT_MESSAGE_ITEM_CANCEL_MARKED = 30025;
    public static final int EVENT_MESSAGE_ITEM_CANCEL_ONE = 30026;
    public static final int EVENT_MESSAGE_ITEM_CANCEL_TWO = 30027;
    public static final int EVENT_MESSAGE_ITEM_CANCEL_THREE = 30028;
    public static final int EVENT_MESSAGE_ITEM_CANCEL_FOUR = 30029;
    public static final int EVENT_MESSAGE_PAY_SUCCESS = 30030;
    public static final int EVENT_MESSAGE_PAY_INFO = 30031;

    public static final int EVNET_MESSAGE_SHOW_HOME_ACTIVITYBUTTON = 30030;
    public static final int EVENT_MESSAGE_SHOW_ACTIVITY_WEBVIEW = 30031;
    public static final int EVENT_MESSAGE_ACCEPT_ORDER_SUCCESS = 30032;

    public static String IMAGE_LOAD_HEADER = "http://cyplus01.oss-cn-beijing.aliyuncs.com/";
    public static final String IMAGE_LOAD_THUMB_END = "?x-oss-process=image/auto-orient,1/quality,q_90/format,jpg/interlace,1/resize,m_fill,limit_1,";


    // 设置一下测试和正式环境的图片上传地址
    // 需要在app启动的时候马上进行设置
    public static void setImageHost() {
        if (Utils.isDebug()) {
            int testServerType = SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SERVER_SET, Constants.SHARED_PREFERENCES_CHANGE_DEBUG_SERVER, 0);
            if (testServerType == 1) {
                IMAGE_LOAD_HEADER = "http://cyplus01.oss-cn-beijing.aliyuncs.com/";
            } else {
                IMAGE_LOAD_HEADER = "http://cyplus01.oss-cn-beijing.aliyuncs.com/";// 测试服务器
            }
        } else {
            IMAGE_LOAD_HEADER = "http://cyplus01.oss-cn-beijing.aliyuncs.com/";
        }
    }
}
