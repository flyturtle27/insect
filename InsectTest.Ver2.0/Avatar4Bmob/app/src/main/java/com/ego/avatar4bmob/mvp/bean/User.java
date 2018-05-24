package com.ego.avatar4bmob.mvp.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created on 17/7/5 09:19
 */

public class User extends BmobUser {


    BmobFile avatar;



    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }
}
