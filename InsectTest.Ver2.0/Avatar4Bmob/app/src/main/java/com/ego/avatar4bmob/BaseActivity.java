package com.ego.avatar4bmob;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ego.avatar4bmob.dialog.LoadingDialog;


/**
 * Created on 17/7/5 19:30
 */

public class BaseActivity extends AppCompatActivity {

    private LoadingDialog mBmobLoadingDialog;


    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    public void showLoading() {
        if (mBmobLoadingDialog == null) {
            mBmobLoadingDialog = new LoadingDialog(mContext);
        }
        mBmobLoadingDialog.show();
    }


    public void hideLoading() {
        if (mBmobLoadingDialog != null)
            mBmobLoadingDialog.dismiss();
    }
}
