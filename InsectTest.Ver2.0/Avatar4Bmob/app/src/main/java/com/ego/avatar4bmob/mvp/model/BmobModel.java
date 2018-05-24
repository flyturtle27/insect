package com.ego.avatar4bmob.mvp.model;


import com.ego.avatar4bmob.mvp.bean.User;

import cn.bmob.v3.BmobUser;
import rx.Observable;

/**
 * Created on 17/7/5 09:36
 */

public class BmobModel {

    public BmobModel() {

    }


    /**
     * 登录
     *
     * @param account  账号/手机密码
     * @param password 密码
     * @return
     */
    public Observable<User> login(String account, String password) {
        return BmobUser.loginByAccountObservable(User.class, account, password);
    }




    /**
     * 一键注册
     *
     * @param account
     * @param password
     * @return
     */
    public Observable<User> oneKeyRegister(final String account, final String password) {
        User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        return user.signUpObservable(User.class);
    }




}
