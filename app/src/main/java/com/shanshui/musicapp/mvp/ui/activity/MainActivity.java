package com.shanshui.musicapp.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.app.base.BaseMusicActivity;
import com.shanshui.musicapp.app.utils.IToast;
import com.shanshui.musicapp.di.component.DaggerMainComponent;
import com.shanshui.musicapp.di.module.MainModule;
import com.shanshui.musicapp.mvp.adapter.DrawerAdapter;
import com.shanshui.musicapp.mvp.adapter.ViewTabPagerAdapter;
import com.shanshui.musicapp.mvp.contract.MainContract;
import com.shanshui.musicapp.mvp.model.DrawerList;
import com.shanshui.musicapp.mvp.presenter.MainPresenter;
import com.shanshui.musicapp.mvp.ui.activity.music.SearchMusicActivity;
import com.shanshui.musicapp.mvp.ui.fragment.home.ChartsFragment;
import com.shanshui.musicapp.mvp.ui.fragment.home.MineFragment;
import com.shanshui.musicapp.mvp.ui.fragment.home.SingerFragment;
import com.shanshui.musicapp.mvp.ui.fragment.home.SongSheetFragment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * @author mashanshui
 * @date 2019-01-23
 * @desc 主界面
 */
public class MainActivity extends BaseMusicActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.nav_recycler)
    RecyclerView navRecycler;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ll_content)
    FrameLayout llContent;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private Toolbar toolbar;
    private MineFragment mineFragment;

    private DrawerAdapter drawerAdapter;
    private List<String> tabTitle = Arrays.asList("我的", "榜单", "歌单", "歌手");
    private List<Fragment> fragmentList;

    //侧滑菜单菜单项
    private List<DrawerList> drawerList = Arrays.asList(
            new DrawerList(R.mipmap.ic_launcher, R.string.drawer_menu_search),
            new DrawerList(R.mipmap.ic_launcher, R.string.drawer_menu_exit)
    );

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this, drawerLayout, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        StatusBarUtil.setColorForDrawerLayout(this, drawerLayout, ContextCompat.getColor(this, R.color.colorPrimaryDark), 20);
        initToolbar();
        initDrawer();
        initTabLayout();
        getKey();
    }

    private void initTabLayout() {
        fragmentList = new ArrayList<>();
        mineFragment = MineFragment.newInstance();
        fragmentList.add(mineFragment);
        fragmentList.add(ChartsFragment.newInstance());
        fragmentList.add(SongSheetFragment.newInstance());
        fragmentList.add(SingerFragment.newInstance());
        ViewTabPagerAdapter adapter = new ViewTabPagerAdapter(getSupportFragmentManager(), fragmentList, tabTitle);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                llContent.setTranslationX(slideOffset * drawerView.getWidth());
            }
        });
        toggle.syncState();
    }

    private void initDrawer() {
        drawerAdapter = new DrawerAdapter(drawerList);
        navRecycler.setLayoutManager(new LinearLayoutManager(this));
        navRecycler.setAdapter(drawerAdapter);
        drawerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DrawerList list = drawerList.get(position);
                switch (list.getResTitleId()) {
                    case R.string.drawer_menu_search:
                        Intent intent = new Intent(MainActivity.this, SearchMusicActivity.class);
                        startActivity(intent);
                        break;
                    case R.string.drawer_menu_exit:
                        AppUtils.exitApp();
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onMediaControllerConnected() {
        mineFragment.onConnected();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            //moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, SearchMusicActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getKey() {
        String mid = k(PhoneUtils.getDeviceId());
        String key = EncryptUtils.encryptMD5ToString("98b683e0ab6c360324cc9ed0f035bef8" + "57ae12eb6890223e355ccfcb74edf70d" + "1005" + mid + "1101986746");
        Log.e(TAG, "getKey: " + mid);
        Log.e(TAG, "getKey: " + key);
    }

    public String k(String str) {
        BigInteger b1 = new BigInteger("0");
        BigInteger b2 = new BigInteger("16");
        String str1 = EncryptUtils.encryptMD5ToString(str.getBytes());
        //System.out.println(str1);
        for (int i = 0; i < str1.length(); i++) {
            //System.out.println(i);
            BigInteger b3;
            StringBuilder sb = new StringBuilder();
            String str2 = "";
            sb.append(str2);
            char c1 = str1.charAt(i);
            sb.append(c1);
            String str3 = sb.toString();
            int n1 = 0x10;
            b3 = new BigInteger(str3, n1);
            int n2 = -0x1;
            int n3 = str1.length() + n2;
            n3 = n3 - i;
            BigInteger b4 = b2.pow(n3);
            //System.out.println(b4);
            b3 = b3.multiply(b4);
            b1 = b1.add(b3);
        }
        return b1.toString();
    }
}
