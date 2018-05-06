package com.ego.avatar4bmob.mvp.view;


import com.ego.avatar4bmob.mvp.bean.User;

/**
 * Created on 17/7/5 09:52
 */

public interface LoginView extends BmobView {
    void  loginSuccess(User user, String password);
}
