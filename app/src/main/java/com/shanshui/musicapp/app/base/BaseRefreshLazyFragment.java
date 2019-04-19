package com.shanshui.musicapp.app.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.CacheType;
import com.jess.arms.integration.lifecycle.FragmentLifecycleable;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.mvp.AppConstant;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * @author mashanshui
 * @date 2018-11-08
 * @desc 懒加载带下拉刷新和上拉加载的fragment，copy@{@link BaseLazyFragment}中的代码
 */
public abstract class BaseRefreshLazyFragment<P extends IPresenter, T> extends LazyFragment implements FragmentLifecycleable
        , OnRefreshListener
        , OnLoadMoreListener {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private Unbinder mUnbinder;

    private int page = 1;
    private int length = AppConstant.PAGE_SIZE;

    @Inject
    @Nullable
    protected P mPresenter;//如果当前页面逻辑简单, Presenter 可以为 null

    @NonNull
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArmsUtils.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().register(this);//注册到事件主线
        setupFragmentComponent(ArmsUtils.obtainAppComponentFromContext(getActivity()));
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            //绑定到butterknife
            mUnbinder = ButterKnife.bind(this, getRealRootView());
        }
        initRefresh();
        initData(savedInstanceState);
    }

    private void initRefresh() {
//        BGAMeiTuanRefreshViewHolder bgaRefreshViewHolder = new BGAMeiTuanRefreshViewHolder(context, true);
//        bgaRefreshViewHolder.setPullDownImageResource(R.mipmap.bga_refresh_mt_pull_down);
//        bgaRefreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
//        bgaRefreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);
//        refreshLayout.setRefreshViewHolder(bgaRefreshViewHolder);
//        refreshLayout.setDelegate(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setEnableLoadMore(true);
    }

    protected abstract int getLayoutId();

    protected abstract void setupFragmentComponent(AppComponent appComponent);

    protected abstract void setData(Object data);

    public abstract void initData(@Nullable Bundle savedInstanceState);

    protected abstract void loadData();

    protected abstract BaseQuickAdapter getAdapter();

    protected void handleSuccess(List<T> datas) {
        if (refreshLayout == null || datas == null || getAdapter() == null) {
            return;
        }
        //下拉刷新
        if (refreshLayout.getState() == RefreshState.Refreshing) {
            getAdapter().setNewData(datas);
        }
        //上拉加载
        else if (refreshLayout.getState() == RefreshState.Loading) {
            //上拉加载后数据小于length，此时认为后台没有更多数据了
            if (datas.size() < length) {
                useLoadMore(false);
            }
            getAdapter().addData(datas);
        }
        //普通加载
        else {
            getAdapter().setNewData(datas);
        }
        baseHideLoading(false);
    }


    protected void handleFailure() {
        if (refreshLayout == null) {
            return;
        }
        //加载失败将page重置到加载之前
        if (refreshLayout.getState() == RefreshState.Loading) {
            page--;
        }
        baseHideLoading(true);
    }


    public void baseShowLoading() {
        //上拉加载和下拉刷新不需要loading
        if (refreshLayout.getState() == RefreshState.Loading || refreshLayout.getState() == RefreshState.Refreshing) {
            return;
        }
        showLoadingView();
    }

    public void baseHideLoading(boolean error) {
        if (error) {
            showErrorView();
        } else {
            showEmptyView();
        }
        refreshOrLoadmoreFinish();
    }

    private void showEmptyView() {
        if (getAdapter() != null) {
            View view = getLayoutInflater().inflate(R.layout.layout_empty_view_default, (ViewGroup) getRealRootView(), false);
            getAdapter().setEmptyView(view);
            view.findViewById(R.id.text_empty_title).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });
        }
    }

    private void showErrorView() {
        if (getAdapter() != null) {
            View view = getLayoutInflater().inflate(R.layout.layout_error_view_default, (ViewGroup) getRealRootView(), false);
            getAdapter().setEmptyView(view);
            view.findViewById(R.id.text_error_title).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });
        }
    }

    private void showLoadingView() {
        if (getAdapter() != null) {
            getAdapter().setEmptyView(R.layout.layout_loading_view_default, (ViewGroup) getRealRootView());
        }
    }

    /**
     * 刷新或加载完成
     */
    private void refreshOrLoadmoreFinish() {
        if (refreshLayout == null) {
            return;
        }
        if (refreshLayout.getState() == RefreshState.Refreshing) {
            refreshLayout.finishRefresh();
        }
        if (refreshLayout.getState() == RefreshState.Loading) {
            refreshLayout.finishLoadMore();
        }
    }

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mUnbinder = null;
        if (mPresenter != null)
            mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(this);//注册到事件主线
    }


    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    public boolean useEventBus() {
        return true;
    }

    /**
     * @param use 是否使用上拉加载
     */
    public void useLoadMore(boolean use) {
        refreshLayout.setEnableLoadMore(use);
    }

    /**
     * @param use 是否使用下拉刷新
     */
    private void usePullRefresh(boolean use) {
        refreshLayout.setEnableRefresh(use);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        loadData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        loadData();
    }

    public RefreshState getRefreshStatus() {
        return refreshLayout.getState();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
