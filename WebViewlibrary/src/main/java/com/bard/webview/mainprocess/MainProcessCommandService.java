package com.bard.webview.mainprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * AIDL步骤
 * 1.编写 aidl文件，也就是需要进程间调用的方法【记得标 输入in 输出out 输入输出inout】
 *      interface IMyAidlInterface {
 *          void addPerson(in Person person);
 *      }
 *
 *  2.build生成相关接口文件： IMyAidlInterface.java
 *      public interface IMyAidlInterface extends android.os.IInterface{
 *          public static class Default implements com.bard.gplearning.IMyAidlInterface{
 *              ...
 *          }
 *
 *          public static abstract class Stub extends android.os.Binder implements com.bard.gplearning.IMyAidlInterface{
 *              ...
 *          }
 *          ...
 *      }
 *
 *  3.编写一个 Service，用来给客户端返回 IBinder，Service是服务端，但是服务端和是不是主进程没关系
 *      这个 返回的IBinder，
 *      3.1 一定是 IMyAidlInterface.Stub的对象【或者是继承自它的子类的对象】
 *      3.2 一定实现了接口方法，供客户端调用
 *
 *      public class MyAidlService extends Service {
 *          @Override
 *          public IBinder onBind(Intent intent) {
 *              return mIBinder;
 *          }
 *
 *          private IBinder mIBinder = new IMyAidlInterface.Stub() {
 *              @Override
 *              public void addPerson(Person person) throws RemoteException {
 *                  mPersons.add(person);
 *              }
 *          };
 *      }
 *
 *  4.客户端通过bind的方式，开启Service，获取到Binder对象：
 *      Intent intent = new Intent(getApplicationContext(), MyAidlService.class);
 *      bindService(intent, mConnection, BIND_AUTO_CREATE);
 *
 *      bindService 方法要传一个 ServiceConnection 对象，获取Binder的行为就在这里发生，
 *      也是在这里获取到服务端返回的IBinder后，可以转换为 IMyAidlInterface，其实就是代理对象
 *
 *      private ServiceConnection mConnection = new ServiceConnection() {
 *         @Override
 *         public void onServiceConnected(ComponentName name, IBinder service) {
 *             //连接后拿到 Binder，转换成 AIDL，在不同进程会返回个代理
 *             mAidl = IMyAidlInterface.Stub.asInterface(service);
 *         }
 *
 *         @Override
 *         public void onServiceDisconnected(ComponentName name) {
 *             mAidl = null;
 *         }
 *     };
 *
 *
 *     客户端获取到代理对象，就可以执行服务端的方法了。而这里客户端也与是不是在主进程无关
 *
 *     在WebView的例子里：
 *     客户端【在BaseWebView里执行initAidlConnection方法】是WebViewActivity，是remote进程，
 *     在remote进程 调用了 executeCommand 方法，传入了 js想要调用原生的方法名、方法参数、还有 BaseWebView本身
 *     executeCommand 中 代理Binder调用了 handleWebCommand 方法【这不重要，就是封装了一个方法】
 *     并且 利用传入的 BaseWebView对象承接了回调方法
 *     【这很重要，就是客户端调用了方法，服务端来具体实现，实现完成后通过接口再通知客户端，相当于客户端跨到服务端，又跨了回来】
 *     【通过接口完成服务端向客户端的跨进程，而不是服务端再去调用跨进程方法】
 *     【因为这都基于先操作网页--调用js方法--调用原生方法--通过回调再调js方法【协商为callback】】
 *
 *     iWebProcessToMainProcessInterface.handleWebCommand(commandName, params, new ICallbackFromMainProcessToWebProcessInterface.Stub() {
 *          @Override
 *          public void onResult(String callbackname, String response) throws RemoteException {
 *              baseWebView.handleCallback(callbackname, response);
 *          }
 *     });
 *
 *
 *
 *     那如果是 原生方法--js方法 呢？
 *
 */

/**
 * 这里是服务端，也就是主进程！！！【并没有在Manifest里改Service的进程，因为这个情景下 WebViewActivity是进程B】
 *
 * 客户端与服务端《绑定》时的回调，返回 一个对象
 * 这个对象：
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
 * 简单说：Service是服务端，是进程B，IXXXInterface.Stub的实现是返回供服务端用的
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
