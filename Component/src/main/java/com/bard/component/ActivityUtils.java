package com.bard.component;

import com.bard.arouter.ARouter;
import com.bard.arouter.IRouter;

public class ActivityUtils implements IRouter {
    @Override
    public void putActivity() {
        ARouter.getInstance().putActivity("component/component", com.bard.component.ComponentActivity.class);
    }
}
