package com.ego.avatar4bmob;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ego.avatar4bmob.image_selector.multi_image_selector.utils.BmobUtils;
import com.ego.avatar4bmob.mvp.presenter.SignUpPresenter;
import com.ego.avatar4bmob.mvp.view.SignUpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/9/3 08:54
 */

public class SignUpActivity extends BaseActivity implements SignUpView {
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.edt_account)
    EditText mEdtAccount;
    @BindView(R.id.edt_password)
    EditText mEdtPassword;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    private SignUpPresenter mSignUpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mToolBar.setTitle("注册");
        mToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.color_white));
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_theme));
        mToolBar.setNavigationIcon(R.mipmap.game_sdk_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }
        });

        mSignUpPresenter = new SignUpPresenter(this);
    }

    @OnClick(R.id.btn_register)
    public void onViewClicked() {

        String account = mEdtAccount.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            BmobUtils.toast(mContext, "账号不能为空");
            return;
        }

        String password = mEdtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            BmobUtils.toast(mContext, "密码不能为空");
            return;
        }

        /**
         * 注册时需要同时设置installationId
         */
        mSignUpPresenter.oneKeyRegister(account, password);
    }

    @Override
    public void showDialog() {

        showLoading();
    }

    @Override
    public void hideDialog() {
        hideLoading();
    }

    @Override
    public void showError(Throwable throwable) {
        BmobUtils.toast(mContext, throwable.getMessage());
    }

    @Override
    public void registerSuccess(String account, String password) {


        startActivity(new Intent(mContext, MainActivity.class));
        finish();
        BmobUtils.toast(mContext, "注册成功");
    }

}
