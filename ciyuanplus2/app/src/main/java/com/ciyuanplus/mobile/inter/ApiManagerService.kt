package com.ciyuanplus.mobile.inter


/**
 * User: wanglg
 * Date: 2018-05-14
 * Time: 11:28
 */
interface ApiManagerService {
//    /**
//     * 首页精选
//     */
//    @GET("v2/feed?")
//    fun getFirstHomeData(@Query("num") num:Int): Observable<HomeBean>
//
//    /**
//     * 根据 nextPageUrl 请求数据下一页数据
//     */
//    @GET
//    fun getMoreHomeData(@Url url: String): Observable<HomeBean>
//
//    /**
//     * 根据item id获取相关视频
//     */
//    @GET("v4/video/related?")
//    fun getRelatedData(@Query("id") id: Long): Observable<HomeBean.Issue>

    /**
     * 	@param macId String macId;//用户设备码
     * 	@param tagMain 1漫画，2小说，用户看的是漫画的书架还是小说的书架
     * 	@param lookingLastBookId ;//最后加载的观看记录bookId，0从第一个开始发，默认一次发送10个
     * 	@param saveLastBookId ;//最后加载的收藏记录bookId，0从第一个开始发，默认一次发送10个
     * */

//
//    @POST("/ebook/2018/kuyomi/looking/android_1")
//    @FormUrlEncoded
//    fun getBookShelfComics(@Field("macId") macId: String
//                           , @Field("tagMain") tagMain: Int
//                           , @Field("lookingLastBookId") lookingLastBookId: Long
//                           , @Field("saveLastBookId") saveLastBookId: Long): Observable<ComicListBean>

//    /**
//     * method：网络请求的方法（区分大小写）
//     * path：网络请求地址路径
//     * hasBody：是否有请求体
//     */
//    @HTTP(method = "POST", path = "/ebook/2018/kuyomi/looking/{build}/{macId}/{tagMain}/{lookingLastBookId}/{saveLastBookId}", hasBody = false)
//    fun getBookShelfComics(@Body placeHolder: String,
//                           @Path("build") build: String,
//                           @Path("macId") macId: String
//                           , @Path("tagMain") tagMain: Int
//                           , @Path("lookingLastBookId") lookingLastBookId: Long
//                           , @Path("saveLastBookId") saveLastBookId: Long): Observable<ComicListBean>

//    /**
//     * 主页
//     * url: /ebook/2018/kuyomi/mainpage/:build/:macId/:tagMain/
//     * */
//    @POST("/ebook/2018/kuyomi/mainpage/{build}/{macId}/{tagMain}/")
//    @FormUrlEncoded
//    fun getStackRoomList(@Field("placeHolder") placeHolder: String,
//                         @Path("build") build: String,
//                         @Path("macId") macId: String,
//                         @Path("tagMain") tagMain: Int): Observable<MainListBean>
//
//    /**
//     * 获取书架
//     * method：网络请求的方法（区分大小写）
//     * path：网络请求地址路径
//     * hasBody：是否有请求体
//     */
//    @POST("/ebook/2018/kuyomi/looking/{build}/{macId}/{tagMain}/{lookingLastBookId}/{saveLastBookId}/")
//    @FormUrlEncoded
//    fun getBookShelfComics(
//            @Field("placeHolder") placeHolder: String,
//            @Path("build") build: String,
//            @Path("macId") macId: String,
//            @Path("tagMain") tagMain: Int,
//            @Path("lookingLastBookId") lookingLastBookId: Long,
//            @Path("saveLastBookId") saveLastBookId: Long): Observable<ComicListBean>
//
//    /** 在首页根据type点击更多，查看更多的书
//     * String macId;//用户设备码
//    int tagMain;//1漫画，2小说，用户看的是漫画的书架还是小说的书架
//    int type;//1热门推荐，2新刊上架，3最新更新，4女生，5男生
//    long maxBookId;//maxBookId当前显示的第几页，如果是第一次，则发送0，之后就显示到第几个发送相应的
//     * */
//
//    @POST("/ebook/2018/kuyomi/getmoresametypebook/{build}/{macId}/{tagMain}/{type}/{maxBookId}/")
//    @FormUrlEncoded
//    fun getMoreBooks(
//            @Field("placeHolder") placeHolder: String,
//            @Path("build") build: String,
//            @Path("macId") macId: String,
//            @Path("tagMain") tagMain: Int,
//            @Path("type") type: Int,
//            @Path("maxBookId") maxBookId: Long): Observable<MoreBookListBean>
//
//    /**
//     * 热搜词
//     * */
//    @POST("/ebook/2018/kuyomi/hotworld/gethotworld/{build}/")
//    @FormUrlEncoded
//    fun getHotWords(
//            @Field("placeHolder") placeHolder: String,
//            @Path("build") build: String): Observable<HotWordListBean>
//
//    /**
//     * 14：查找
//    url：/ebook/2018/kuyomi/findbooks/:build/:macId/:bookNameOrAuthor/:tag/:state/
//    C2S：{
//    String macId;//用户设备码
//    String bookNameOrAuthor;//根据 bookNameOrAuthor，没填的部分用0替代
//    int tag;//根据tag,按照标签进行筛选目标书，没填的部分用0替代 1到12
//    int state;//1连载，2完结，没填的部分用0替代
//    }
//     * */
//
//    @POST("/ebook/2018/kuyomi/findbooks/{build}/{macId}/{tagMain}/{bookNameOrAuthor}/{tag}/{state}/")
//    @FormUrlEncoded
//    fun getQueryBookList(
//            @Field("placeHolder") placeHolder: String,
//            @Path("build") build: String,
//            @Path("macId") macId: String,
//            @Path("tagMain") tagMain: Int,
//            @Path("bookNameOrAuthor") bookNameOrAuthor: String,
//            @Path("tag") tag: Int,
//            @Path("state") state: Int): Observable<CategoryListBean>
//
//    /**
//     * 9：查看某一本书的详情
//    url: /ebook/2018/kuyomi/bookdetails/:build/:macId/:bookId/
//    C2S: {
//    String macId;//用户设备码
//    long bookId;//图书唯一编号
//    }
//     * */
//
//    @POST("/ebook/2018/kuyomi/bookdetails/{build}/{macId}/{bookId}/")
//    @FormUrlEncoded
//    fun getBookDetail(
//            @Field("placeHolder") placeHolder: String,
//            @Path("build") build: String,
//            @Path("macId") macId: String,
//            @Path("bookId") bookId: Long
//    ): Observable<BookDetailBean>
//
//    /**
//     * 11：收藏某一本书
//    url: /ebook/2018/kuyomi/booksave/:build/:macId/:bookId/
//     * */
//    @POST("/ebook/2018/kuyomi/booksave/{build}/{macId}/{bookId}/")
//    @FormUrlEncoded
//    fun doCollect(@Field("placeHolder") placeHolder: String,
//                  @Path("build") build: String,
//                  @Path("macId") macId: String,
//                  @Path("bookId") bookId: Long): Observable<BaseResponse>
//
//    /**
//     * 11.1：删除收藏或观看记录
//    url: /ebook/2018/kuyomi/deletebooksave/:build/:macId/:bookIds/:type/
//    C2S: {
//    String macId;//用户设备码
//    String bookIds;//图书唯一编号List,每个书bookId之间用;分割
//    int type;//1:删除收藏，2删除观看记录
//    }
//
//     * */
//    @POST("/ebook/2018/kuyomi/deletebooksave/{build}/{macId}/{bookIds}/{type}/")
//    @FormUrlEncoded
//    fun cancelCollect(@Field("placeHolder") placeHolder: String,
//                      @Path("build") build: String,
//                      @Path("macId") macId: String,
//                      @Path("bookIds") bookIds: String,
//                      @Path("type") type: Int): Observable<BaseResponse>
//
//    /**
//     * 7：获取书架
//    url: /ebook/2018/kuyomi/looking/:build/:macId/:tagMain/:lookingLastBookId/:saveLastBookId/
//    C2S: {
//    String macId;//用户设备码
//    int tagMain;//1漫画，2小说，用户看的是漫画的书架还是小说的书架
//    long lookingLastBookId;//最后加载的观看记录bookId，0从第一个开始发，默认一次发送10个
//    long saveLastBookId;//最后加载的收藏记录bookId，0从第一个开始发，默认一次发送10个
//    }
//     * */
//
//    @POST("/ebook/2018/kuyomi/looking/{build}/{macId}/{tagMain}/{lookingLastBookId}/{saveLastBookId}/")
//    @FormUrlEncoded
//    fun getCollectAndBookMark(@Field("placeHolder") placeHolder: String,
//                              @Path("build") build: String,
//                              @Path("macId") macId: String,
//                              @Path("tagMain") tagMain: Int,
//                              @Path("lookingLastBookId") lookingLastBookId: Long,
//                              @Path("saveLastBookId") saveLastBookId: Long): Observable<CollectionAndBookmarkBean>
//
//    /**
//     *
//    1：用户登录：
//    url: /ebook/2018/kuyomi/userlogin/:build/:macId/
//     * */
//    @POST("/ebook/2018/kuyomi/userlogin/{build}/{macId}/")
//    @FormUrlEncoded
//    fun login(@Field("placeHolder") placeHolder: String,
//              @Path("build") build: String,
//              @Path("macId") macId: String): Observable<LoginBean>
//
//    /**
//     * 16：签到
//    url：/ebook/2018/kuyomi/signIn/:build/:macId/
//     * */
//
//    @POST("/ebook/2018/kuyomi/signIn/{build}/{macId}/")
//    @FormUrlEncoded
//    fun sign(@Field("placeHolder") placeHolder: String,
//             @Path("build") build: String,
//             @Path("macId") macId: String): Observable<SignBean>
//
//    /**
//     *
//    2：获取手机校验码：
//    url：/ebook/2018/kuyomi/getphonecode/:build/:macId/:phone/
//    C2S：{
//    String macId;//用户设备码
//    String phone;//电话号码
//    }
//    S2C：{
//    isOk;//0成功，1错误
//    String msg;//信息
//    }
//     * */
//    @POST("/ebook/2018/kuyomi/getphonecode/{build}/{macId}/{phone}/")
//    @FormUrlEncoded
//    fun getCode(@Field("placeHolder") placeHolder: String,
//                @Path("build") build: String,
//                @Path("macId") macId: String,
//                @Path("phone") phone: String): Observable<BaseResponse>
//
//    /**
//     * 4：绑定手机：(需要先获取手机校验码)
//    url: /ebook/2018/kuyomi/bindphone/:build/:macId/:phone/:phoneCode/
//    C2S: {
//    String macId;//用户设备码
//    String phone;//电话号码
//    String phoneCode;//验证码,客户端发送macId,手机号,验证码发送给服务器，服务器验证成功后绑定手机。
//    }
//     * */
//
//    @POST("/ebook/2018/kuyomi/bindphone/{build}/{macId}/{phone}/{phoneCode}/")
//    @FormUrlEncoded
//    fun bindPhone(@Field("placeHolder") placeHolder: String,
//                  @Path("build") build: String,
//                  @Path("macId") macId: String,
//                  @Path("phone") phone: String,
//                  @Path("phoneCode") phoneCode: String): Observable<BaseResponse>
//
//    /**
//    19.获取充值列表详情
//    url：/ebook/2018/kuyomi/getVipDetails/:build/:macId/
//     * */
//
//    @POST("/ebook/2018/kuyomi/getVipDetails/{build}/{macId}/")
//    @FormUrlEncoded
//    fun getVipList(@Field("placeHolder") placeHolder: String,
//                   @Path("build") build: String,
//                   @Path("macId") macId: String): Observable<VipListBean>
//
//    /**
//    19.充值
//    url：/ebook/2018/kuyomi/zhiFuBaoChongZhi/{build}//{macId}/{chongZhiId}/
//     * */
//
//    @POST("/ebook/2018/kuyomi/zhiFuBaoChongZhi/{build}/{macId}/{chongZhiId}/")
//    @FormUrlEncoded
//    fun getOrderBean(@Field("placeHolder") placeHolder: String,
//                       @Path("build") build: String,
//                       @Path("macId") macId: String,
//                       @Path("chongZhiId") chongZhiId: String): Observable<VipOrderBean>
//
//    /**
//    19.充值
//    url：/ebook/2018/kuyomi/zhiFuBaoChongZhi/{build}//{macId}/{chongZhiId}/
//     * */
//
//    @POST("/ebook/2018/kuyomi/zhiFuBaoChongZhi/{build}/{macId}/{chongZhiId}/")
//    @FormUrlEncoded
//    fun getOrderString(@Field("placeHolder") placeHolder: String,
//                       @Path("build") build: String,
//                       @Path("macId") macId: String,
//                       @Path("chongZhiId") chongZhiId: String): Observable<String>
//

}

