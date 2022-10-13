package com.bard.webview.mainprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 这里是服务端，也就是主进程！！！【并没有在Manifest里改，因为这个情景下 WebViewActivity是进程B】
 *
 * 客户端与服务端《绑定》时的回调，返回 一个对象，这个对象
 *
 * 1.是个Binder，是 Binder类型，因为 Stub继承自 Binder
 * 2.是IBinder，因为 Binder实现了 IBinder接口
 * 3.是Stub，因为 Stub就是 AIDL提供给我们用来 new的静态抽象类
 * 4.是等待实现接口里方法的抽象类，因为它实现了 IXXXInterface接口，但因为抽象 没有实现，留给开发人员在服务端具体实现
 *
 * 所以我们去 new 一个 IXXXInterface.Stub 的对象，并且在 new的时候完成接口方法
 *
 * 而后客户端就可以通过返回的代理对象 远程调用服务端的方法，即实现了跨进程通讯
 *
 * Stub就是Android AIDL为我们生成的、继承自Binder的、实现了IBinder接口的【因为Binder实现了IBinder接口】、等待我们实现方法的一个抽象类
 * 我们需要实现方法【handleWebCommand(...)】，这个方法就是服务端需要调用的方法
 *
 * 简单说：Service是服务端是，是进程B，IXXXInterface.Stub的实现是返回供服务端用的
 */
public class MainProcessCommandService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int pid = Process.myPid();
        Log.e("MainProcessCmdService", "pid = "+pid+" 客户端与服务端连接成功，服务端返回BinderPool.BinderPoolImpl【】");
        return MainProcessCommandsManager.getInstance();
    }
}
