package com.shanshui.musicapp.mvp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.shanshui.musicapp.R;
import com.shanshui.musicapp.di.component.DaggerLoginComponent;
import com.shanshui.musicapp.di.module.LoginModule;
import com.shanshui.musicapp.mvp.contract.LoginContract;
import com.shanshui.musicapp.mvp.presenter.LoginPresenter;
import com.shanshui.musicapp.mvp.ui.widget.ClearWriteEditText;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.de_img_backgroud)
    ImageView deImgBackgroud;
    @BindView(R.id.de_login_phone)
    ClearWriteEditText deLoginPhone;
    @BindView(R.id.de_login_password)
    ClearWriteEditText deLoginPassword;
    @BindView(R.id.de_login_sign)
    QMUIRoundButton deLoginSign;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTransparentForImageView(this, null);
        deLoginSign.setChangeAlphaWhenPress(true);
        new Handler().postDelayed(() -> {
            Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
            deImgBackgroud.startAnimation(animation);
        }, 200);
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

    @OnClick({R.id.de_login_sign, R.id.de_login_forgot, R.id.de_login_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.de_login_sign:
                break;
            case R.id.de_login_forgot:
                ArmsUtils.startActivity(LoginActivity.this, ForgetPasswordActivity.class);
                break;
            case R.id.de_login_register:
                ArmsUtils.startActivity(LoginActivity.this, RegisterActivity.class);
                break;
            default:
                break;
        }
    }
}
