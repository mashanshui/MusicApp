package com.shanshui.musicapp.mvp.ui.fragment.music;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.di.component.DaggerSearchMusicPreviewComponent;
import com.shanshui.musicapp.mvp.adapter.SearchHistoryAdapter;
import com.shanshui.musicapp.mvp.adapter.SearchHotAdapter;
import com.shanshui.musicapp.mvp.contract.SearchMusicPreviewContract;
import com.shanshui.musicapp.mvp.model.bean.SearchHotBean;
import com.shanshui.musicapp.mvp.model.bean.SearchTipBean;
import com.shanshui.musicapp.mvp.presenter.SearchMusicPreviewPresenter;
import com.shanshui.musicapp.mvp.ui.activity.WebBrowserActivity;
import com.shanshui.musicapp.mvp.ui.activity.music.SearchMusicActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * @author mashanshui
 * @date 2019-04-19
 * @desc 搜索音乐——》预览页面
 */
public class SearchMusicPreviewFragment extends BaseFragment<SearchMusicPreviewPresenter> implements SearchMusicPreviewContract.View {

    @BindView(R.id.rv_search_hot)
    RecyclerView rvSearchHot;
    @BindView(R.id.rv_search_history)
    RecyclerView rvSearchHistory;
    Unbinder unbinder;

    private SearchHotAdapter searchHotAdapter;
    private SearchHistoryAdapter searchHistoryAdapter;

    public static SearchMusicPreviewFragment newInstance() {
        SearchMusicPreviewFragment fragment = new SearchMusicPreviewFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSearchMusicPreviewComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_music_preview, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initSearchHot();
        initSearchHistory();
        mPresenter.getSearchHotData();
        mPresenter.getSearchHistory();
    }

    private void initSearchHistory() {
        searchHistoryAdapter = new SearchHistoryAdapter(null);
        rvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchHistory.setNestedScrollingEnabled(false);
        rvSearchHistory.setHasFixedSize(true);
        rvSearchHistory.setFocusable(false);
        rvSearchHistory.setAdapter(searchHistoryAdapter);
        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((SearchMusicActivity) getActivity()).search(((String) adapter.getItem(position)));
            }
        });
    }

    private void initSearchHot() {
        searchHotAdapter = new SearchHotAdapter(null);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rvSearchHot.setNestedScrollingEnabled(false);
        rvSearchHot.setHasFixedSize(true);
        rvSearchHot.setFocusable(false);
        rvSearchHot.setLayoutManager(gridLayoutManager);
        rvSearchHot.setAdapter(searchHotAdapter);
        searchHotAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SearchHotBean searchHotBean = (SearchHotBean) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), WebBrowserActivity.class);
                intent.putExtra("url", searchHotBean.getJumpurl());
                startActivity(intent);
            }
        });
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
        if (data instanceof Message) {
            switch (((Message) data).what) {
                case 0:
                    mPresenter.setSearchHistory((String) ((Message) data).obj);
                    mPresenter.getSearchHistory();
                    break;
                default:
                    break;
            }
        }
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
    public void updateSearchHot(List<SearchHotBean> data) {
        searchHotAdapter.setNewData(data);
    }

    @Override
    public void updateSearchHistory(List<String> stringList) {
        if (stringList.isEmpty()) {
            searchHistoryAdapter.removeAllFooterView();
        } else {
            addFooterView();
        }
        searchHistoryAdapter.setNewData(stringList);
    }

    private void addFooterView() {
        TextView textView = new TextView(getContext());
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        textView.setText("清空搜索历史");
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, ConvertUtils.dp2px(12), 0, 0);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageNegativeDialog();
            }
        });
        searchHistoryAdapter.addFooterView(textView);
    }

    private void showMessageNegativeDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setMessage("确定要删除吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        mPresenter.clearSearchHistory();
                        mPresenter.getSearchHistory();
                        dialog.dismiss();
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
    }

}
