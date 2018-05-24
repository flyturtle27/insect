package com.ego.avatar4bmob.mvp.presenter;


import com.ego.avatar4bmob.mvp.bean.User;
import com.ego.avatar4bmob.mvp.model.BmobModel;
import com.ego.avatar4bmob.mvp.view.SignUpView;

import rx.functions.Action1;

/**
 * Created on 17/7/5 11:06
 */

public class SignUpPresenter {
    private SignUpView mSignUpView;

    private BmobModel mBmobModel;

    public SignUpPresenter(SignUpView oneKeyView) {
        mSignUpView = oneKeyView;
        mBmobModel = new BmobModel();
    }

    public void oneKeyRegister(final String account, final String password) {
        mSignUpView.showDialog();
        mBmobModel.oneKeyRegister(account, password)
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {

                        mSignUpView.hideDialog();
                        mSignUpView.registerSuccess(account, password);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSignUpView.hideDialog();
                        mSignUpView.showError(throwable);
                    }
                });
    }
}
