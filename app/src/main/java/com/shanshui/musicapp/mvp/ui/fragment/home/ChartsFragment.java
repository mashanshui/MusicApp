package com.shanshui.musicapp.mvp.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.base.BaseRefreshLazyFragment;
import com.shanshui.musicapp.di.component.DaggerChartsComponent;
import com.shanshui.musicapp.di.module.ChartsModule;
import com.shanshui.musicapp.mvp.adapter.ChartsAdapter;
import com.shanshui.musicapp.mvp.contract.ChartsContract;
import com.shanshui.musicapp.mvp.model.bean.ChartsOutBean;
import com.shanshui.musicapp.mvp.presenter.ChartsPresenter;
import com.shanshui.musicapp.mvp.ui.activity.charts.ChartsDetailActivity;

import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * @author mashanshui
 * @date 2018-11-09
 * @desc 歌曲排行榜
 */
public class ChartsFragment extends BaseRefreshLazyFragment<ChartsPresenter, ChartsOutBean> implements ChartsContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ChartsAdapter chartsAdapter;

    public static ChartsFragment newInstance() {
        ChartsFragment fragment = new ChartsFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerChartsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .chartsModule(new ChartsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_charts;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        useLoadMore(false);
        chartsAdapter = new ChartsAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chartsAdapter);
        chartsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ChartsOutBean chartsOutBean = (ChartsOutBean) adapter.getItem(position);
                Intent intent = new Intent(context, ChartsDetailActivity.class);
                intent.putExtra("rankId", chartsOutBean.getRankid());
                intent.putExtra("chartsId", chartsOutBean.getId());
                intent.putExtra("bannerUrl", chartsOutBean.getBanner7url());
                intent.putExtra("chartsName", chartsOutBean.getRankname());
                startActivity(intent);
            }
        });
        loadData();
    }

    @Override
    protected void loadData() {
        mPresenter.loadData();
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return chartsAdapter;
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        baseShowLoading();
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    @Override
    public void requestSuccess(List<ChartsOutBean> list) {
        handleSuccess(list);
    }

    @Override
    public void requestFailure() {
        handleFailure();
    }
}
