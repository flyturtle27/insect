package com.ego.avatar4bmob.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.ego.avatar4bmob.R;


/**
 * Created on 17/7/5 19:22
 */

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.progress_dialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCancelable(true);
        getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        TextView msg = this.findViewById(R.id.tv_msg);
        msg.setText("加载中……");
        msg.setTextColor(ContextCompat.getColor(getContext(), R.color.color_loading));

    }
}
