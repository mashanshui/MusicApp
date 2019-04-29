package com.shanshui.musicapp.mvp.ui.activity.music;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.base.BaseMusicActivity;
import com.shanshui.musicapp.app.utils.IToast;
import com.shanshui.musicapp.di.component.DaggerSearchMusicComponent;
import com.shanshui.musicapp.mvp.adapter.ViewPageFragmentAdapter;
import com.shanshui.musicapp.mvp.contract.SearchMusicContract;
import com.shanshui.musicapp.mvp.presenter.SearchMusicPresenter;
import com.shanshui.musicapp.mvp.ui.fragment.music.SearchMusicInputFragment;
import com.shanshui.musicapp.mvp.ui.fragment.music.SearchMusicPreviewFragment;
import com.shanshui.musicapp.mvp.ui.fragment.music.SearchMusicResultFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * @author mashanshui
 * @date 2019-04-18
 * @desc 音乐搜索页面
 */
public class SearchMusicActivity extends BaseMusicActivity<SearchMusicPresenter> implements SearchMusicContract.View {

    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private SearchMusicPreviewFragment previewFragment;
    private SearchMusicInputFragment inputFragment;
    private SearchMusicResultFragment resultFragment;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSearchMusicComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_search_music; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        previewFragment = SearchMusicPreviewFragment.newInstance();
        inputFragment = SearchMusicInputFragment.newInstance();
        resultFragment = SearchMusicResultFragment.newInstance();
        ViewPageFragmentAdapter viewPageFragmentAdapter = new ViewPageFragmentAdapter(getSupportFragmentManager());
        viewPageFragmentAdapter.addFragment(previewFragment);
        viewPageFragmentAdapter.addFragment(inputFragment);
        viewPageFragmentAdapter.addFragment(resultFragment);
        viewPager.setAdapter(viewPageFragmentAdapter);
        viewPager.setOffscreenPageLimit(5);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyWord = s.toString().trim();
                if (TextUtils.isEmpty(keyWord)) {
                    viewPager.setCurrentItem(0, false);
                } else {
                    Message data = new Message();
                    data.what = 0;
                    data.obj = keyWord;
                    inputFragment.setData(data);
                    viewPager.setCurrentItem(1, false);
                }
            }
        });
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
        finish();
    }

    @Override
    protected void onMediaControllerConnected() {

    }

    @OnClick({R.id.ibtn_back, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                killMyself();
                break;
            case R.id.btn_search:
                String keyword = edtSearch.getText().toString().trim();
                search(keyword);
                break;
            default:
                break;
        }
    }

    public void search(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            IToast.showShort("搜索内容不能为空");
            return;
        }
        //保存搜索历史
        Message data = new Message();
        data.what = 0;
        data.obj = keyword;
        previewFragment.setData(data);
        //跳到搜索结果界面
        viewPager.setCurrentItem(2, false);
        resultFragment.setData(data);
    }
}
