package com.xiangxue.usercenter;

import android.content.Intent;

import com.bard.base.autoservice.BaseApplication;
import com.bard.common.autoservice.IUserCenterService;
import com.google.auto.service.AutoService;

@AutoService({IUserCenterService.class})
public class IUserCenterServiceImpl implements IUserCenterService {
    @Override
    public boolean isLogined() {
        return false;
    }

    @Override
    public void login() {
        Intent intent = new Intent(BaseApplication.sApplication, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.sApplication.startActivity(intent);
    }
}
