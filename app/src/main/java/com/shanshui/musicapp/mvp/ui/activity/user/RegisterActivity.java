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
import com.shanshui.musicapp.di.component.DaggerRegisterComponent;
import com.shanshui.musicapp.di.module.RegisterModule;
import com.shanshui.musicapp.mvp.contract.RegisterContract;
import com.shanshui.musicapp.mvp.presenter.RegisterPresenter;
import com.shanshui.musicapp.mvp.ui.widget.ClearWriteEditText;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {

    @BindView(R.id.rg_img_backgroud)
    ImageView rgImgBackgroud;
    @BindView(R.id.reg_username)
    ClearWriteEditText regUsername;
    @BindView(R.id.reg_phone)
    ClearWriteEditText regPhone;
    @BindView(R.id.reg_code)
    ClearWriteEditText regCode;
    @BindView(R.id.reg_getcode)
    QMUIRoundButton regGetcode;
    @BindView(R.id.reg_password)
    ClearWriteEditText regPassword;
    @BindView(R.id.reg_button)
    QMUIRoundButton regButton;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .registerModule(new RegisterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_register; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTransparentForImageView(this, null);
        regGetcode.setChangeAlphaWhenPress(true);
        regButton.setChangeAlphaWhenPress(true);
        new Handler().postDelayed(() -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
            rgImgBackgroud.startAnimation(animation);
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


    @OnClick({R.id.reg_getcode, R.id.reg_button, R.id.reg_forget, R.id.reg_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg_getcode:
                break;
            case R.id.reg_button:
                break;
            case R.id.reg_forget:
                ArmsUtils.startActivity(RegisterActivity.this, ForgetPasswordActivity.class);
                killMyself();
                break;
            case R.id.reg_login:
                killMyself();
                break;
            default:
                break;
        }
    }
}
