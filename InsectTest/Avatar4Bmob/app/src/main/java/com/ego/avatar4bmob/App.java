package com.ego.avatar4bmob;

import android.app.Application;

import cn.bmob.v3.Bmob;



public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"33273f8a53287ea13fe31d8f2d5f5b57");
    }
}
