package com.shanshui.musicapp.mvp.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.base.BaseRefreshLazyFragment;
import com.shanshui.musicapp.di.component.DaggerSongSheetComponent;
import com.shanshui.musicapp.di.module.SongSheetModule;
import com.shanshui.musicapp.mvp.adapter.SongSheetAdapter;
import com.shanshui.musicapp.mvp.contract.SongSheetContract;
import com.shanshui.musicapp.mvp.model.bean.SongSheetBean;
import com.shanshui.musicapp.mvp.presenter.SongSheetPresenter;
import com.shanshui.musicapp.mvp.ui.activity.charts.ChartsDetailActivity;
import com.shanshui.musicapp.mvp.ui.activity.songsheet.SongSheetDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * @author mashanshui
 * @date 2018-11-09
 * @desc 歌单
 */
public class SongSheetFragment extends BaseRefreshLazyFragment<SongSheetPresenter, SongSheetBean> implements SongSheetContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private SongSheetAdapter adapter;

    public static SongSheetFragment newInstance() {
        SongSheetFragment fragment = new SongSheetFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song_sheet;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSongSheetComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .songSheetModule(new SongSheetModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        useLoadMore(true);
        adapter = new SongSheetAdapter(null);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SongSheetBean songSheetBean = (SongSheetBean) adapter.getItem(position);
                Intent intent = new Intent(context, SongSheetDetailActivity.class);
                intent.putExtra("specialId", songSheetBean.getSpecialid());
                intent.putExtra("specialName", songSheetBean.getSpecialname());
                intent.putExtra("bannerUrl", songSheetBean.getImgurl());
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
        return adapter;
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
    public void requestSuccess(List<SongSheetBean> list) {
        handleSuccess(list);
    }

    @Override
    public void requestFailure() {
        handleFailure();
    }
}
