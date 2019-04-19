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
import com.shanshui.musicapp.di.component.DaggerForgetPasswordComponent;
import com.shanshui.musicapp.di.module.ForgetPasswordModule;
import com.shanshui.musicapp.mvp.contract.ForgetPasswordContract;
import com.shanshui.musicapp.mvp.presenter.ForgetPasswordPresenter;
import com.shanshui.musicapp.mvp.ui.widget.ClearWriteEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements ForgetPasswordContract.View {

    @BindView(R.id.rg_img_backgroud)
    ImageView rgImgBackgroud;
    @BindView(R.id.forget_phone)
    ClearWriteEditText forgetPhone;
    @BindView(R.id.forget_code)
    ClearWriteEditText forgetCode;
    @BindView(R.id.forget_getcode)
    QMUIRoundButton forgetGetcode;
    @BindView(R.id.forget_password)
    ClearWriteEditText forgetPassword;
    @BindView(R.id.forget_password1)
    ClearWriteEditText forgetPassword1;
    @BindView(R.id.forget_button)
    QMUIRoundButton forgetButton;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerForgetPasswordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .forgetPasswordModule(new ForgetPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_forget_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTransparentForImageView(this, null);
        forgetButton.setChangeAlphaWhenPress(true);
        forgetGetcode.setChangeAlphaWhenPress(true);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.forget_getcode, R.id.forget_button, R.id.de_login, R.id.de_login_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_getcode:
                break;
            case R.id.forget_button:
                break;
            case R.id.de_login:
                killMyself();
                break;
            case R.id.de_login_register:
                ArmsUtils.startActivity(ForgetPasswordActivity.this, RegisterActivity.class);
                killMyself();
                break;
            default:
                break;
        }
    }
}
