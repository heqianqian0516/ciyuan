package com.ciyuanplus.mobile.module.others.new_others

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import butterknife.ButterKnife
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ciyuanplus.mobile.MyBaseActivity
import com.ciyuanplus.mobile.R
import com.ciyuanplus.mobile.inter.LoadMoreStatusInterface
import com.ciyuanplus.mobile.inter.MyOnClickListener
import com.ciyuanplus.mobile.manager.EventCenterManager
import com.ciyuanplus.mobile.manager.UserInfoData
import com.ciyuanplus.mobile.module.like.other.OtherLikeListFragment
import com.ciyuanplus.mobile.module.others.news.OthersPostFragment
import com.ciyuanplus.mobile.net.response.SocialCountItem
import com.ciyuanplus.mobile.utils.Constants
import com.ciyuanplus.mobile.utils.Utils
import com.flyco.tablayout.listener.OnTabSelectListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.shuyu.gsyvideoplayer.GSYVideoManager
import crossoverone.statuslib.StatusUtil
import kotlinx.android.synthetic.main.activity_others_news.*
import kotlinx.android.synthetic.main.header_home_fragment.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Alen on 2017/5/17.
 * 他的个人中心页面
 */

class OthersActivity : MyBaseActivity(), OthersContract.View, EventCenterManager.OnHandleEventListener
        , OnRefreshListener, OnLoadMoreListener, ViewPager.OnPageChangeListener, LoadMoreStatusInterface {

    var mSelectedTab = 0
    private var mPostFragment: OthersPostFragment? = null
    private var mLikeListFragment: OtherLikeListFragment? = null

    private val mFragments = ArrayList<Fragment>()
    private val mTitles = arrayOf("动态", "喜欢")

    @Inject
    lateinit var mPresenter: OthersPresenter


    val userUuid: String
        get() = mPresenter.mUserUuid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_others_news)
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);
        this.initView()
        mPresenter.initData(intent)
    }

    private fun initData() {

        title_bar.setOnBackListener(View.OnClickListener {

            onBackPressed()
        })

        if (mPostFragment == null) {
            mPostFragment = OthersPostFragment.newInstance()
            mPostFragment?.let {
                this.mFragments.add(it)
                mPostFragment?.setLoadMoreStatusInterface(this@OthersActivity)
            }
        }

        if (mLikeListFragment == null) {
            mLikeListFragment = OtherLikeListFragment.newInstance()
            mLikeListFragment?.let {
                this.mFragments.add(it)
            }

            mLikeListFragment?.setLoadMoreStatusInterface(this@OthersActivity)

        }
        val adapter = object : FragmentPagerAdapter((this@OthersActivity).supportFragmentManager) {
            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }

            override fun getItem(arg0: Int): Fragment {
                return mFragments[arg0]
            }
        }
        viewPagerOther.adapter = adapter


        stl_tabs.setViewPager(viewPagerOther)
        stl_tabs.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {

                mSelectedTab = position
                when (mSelectedTab) {
                    0 -> {
                        mPostFragment?.let { it.isLoadMoreEnable.let { it2 -> smartRefreshLayout.setEnableLoadMore(it2) } }
                    }
                    1 -> {
                        mLikeListFragment?.let { it.isLoadMoreEnable.let { it2 -> smartRefreshLayout.setEnableLoadMore(it2) } }
                    }
                }
                switchTabSelect(position)


            }

            override fun onTabReselect(position: Int) {

            }
        })
    }

    private fun initView() {
        ButterKnife.bind(this)
        DaggerOthersPresenterComponent.builder()
                .othersPresenterModule(OthersPresenterModule(this)).build().inject(this)

        initData()
        smartRefreshLayout.setOnRefreshListener(this)
        smartRefreshLayout.setOnLoadMoreListener(this)
        smartRefreshLayout.setEnableNestedScroll(true)

        iv_head.setOnClickListener(myOnClickListener)
        tv_add.setOnClickListener(myOnClickListener)

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
        viewPagerOther.addOnPageChangeListener(this@OthersActivity)
        iv_head.requestFocus()// 防止直接跳到列表那边了
        image_back.setOnClickListener(){
            finish()
        }
    }

    // 更新页面的状态
    override fun updateView() {

        Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mUserInfo.photo).apply(RequestOptions().placeholder(R.mipmap.default_head_).error(R.mipmap.default_head_)
                .dontAnimate().centerCrop()).into(iv_head)

        tv_name.text = mPresenter.mUserInfo.nickname

        if (!Utils.isStringEquals(mPresenter.mUserInfo.uuid, UserInfoData.getInstance().userInfoItem.uuid)) {
            tv_add.visibility = View.VISIBLE
        } else {
            tv_add.visibility = View.GONE
        }

        if (!Utils.isStringEquals(mPresenter.mUserInfo.uuid, UserInfoData.getInstance().userInfoItem.uuid) && mPresenter.mUserInfo.isFollow != 1) { // 关注
           /* tv_add.text = "+关注"*/
            tv_add.setImageResource(R.mipmap.mine_personal_follow)
        } else {
          /*  tv_add.text = "已关注"*/
            tv_add.setImageResource(R.mipmap.mine_personal_concerned)
        }

        iv_head_sex.setImageResource(mPresenter.mUserInfo.sexImageResource)

        if (!StringUtils.isEmpty(mPresenter.mUserInfo.personalizedSignature)) {
            tv_sign.text = mPresenter.mUserInfo.personalizedSignature
        } else {
            tv_sign.hint = "我是签名，我是就爱签名"
        }

    }

    override fun switchTabSelect(position: Int) {
        viewPagerOther.currentItem = position
    }

    override fun updateInfo(item: SocialCountItem) {

        tv_like_count.text = item.likeCount.toString()
        tv_fans.text = item.fansCount.toString()
        tv_follow.text = item.followCount.toString()
    }

    override fun stopLoadMoreAndRefresh() {
        smartRefreshLayout.finishLoadMore()
        smartRefreshLayout.finishRefresh()
    }


    public override fun onDestroy() {
        super.onDestroy()
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this)
    }

    override fun onHandleEvent(eventMessage: EventCenterManager.EventMessage) {
        mPresenter.handleEvent(eventMessage)
    }

    override fun getDefaultContext(): Context {
        return this
    }


    override fun onRefresh(refreshlayout: RefreshLayout) {

        if (mSelectedTab == 0) {

            mPostFragment?.refresh()
        } else {
            mLikeListFragment?.refresh()
        }
    }

    override fun onLoadMore(refreshlayout: RefreshLayout) {

        if (mSelectedTab == 0) {

            mPostFragment?.loadMore()
        } else {
            mLikeListFragment?.loadMore()
        }
    }


    override fun setLoadMoreEnable(enable: Boolean) {

        smartRefreshLayout.setEnableLoadMore(enable)
    }

    override fun onFinishLoadMore(enable: Boolean) {

        stopLoadMoreAndRefresh()

        when (mSelectedTab) {
            0 -> {
                mPostFragment?.let { smartRefreshLayout.setEnableLoadMore(enable) }
            }
            1 -> {
                mLikeListFragment.let { smartRefreshLayout.setEnableLoadMore(enable) }
            }
        }

    }

    override fun onLoadMoreError() {
        stopLoadMoreAndRefresh()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {


    }

    override fun onPageSelected(position: Int) {

    }

    private val myOnClickListener = object : MyOnClickListener() {
        public override fun performRealClick(v: View) {

            val id = v.id
            mPresenter.handleClick(id)
        }
    }

    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) return else super.onBackPressed()
    }
}
