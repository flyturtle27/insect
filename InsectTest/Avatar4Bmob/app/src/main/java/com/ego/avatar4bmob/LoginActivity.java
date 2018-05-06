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
import com.ego.avatar4bmob.mvp.bean.User;
import com.ego.avatar4bmob.mvp.presenter.LoginPresenter;
import com.ego.avatar4bmob.mvp.view.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 17/9/3 08:52
 */

public class LoginActivity extends BaseActivity implements LoginView {
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.edt_account)
    EditText mEdtAccount;
    @BindView(R.id.edt_password)
    EditText mEdtPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_to_sign_up)
    Button mBtnToSignUp;

    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mToolBar.setTitle("登录");
        mToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.color_white));
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_theme));

        mLoginPresenter = new LoginPresenter(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_to_sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
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
                mLoginPresenter.login(account, password);
                break;
            case R.id.btn_to_sign_up:
                startActivity(new Intent(mContext, SignUpActivity.class));
                finish();
                break;
        }
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
    public void loginSuccess(User user, String password) {
        BmobUtils.toast(mContext, "登录成功");
        startActivity(new Intent(mContext, MainActivity.class));



    }
}
