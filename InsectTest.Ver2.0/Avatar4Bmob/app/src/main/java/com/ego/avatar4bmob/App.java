package com.ego.avatar4bmob;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created on 17/9/13 14:26
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"c60d67b9a645e540249debc80ffe7abb");
    }
}
