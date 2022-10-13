package com.bard.gplearning.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bard.gplearning.IMyAidlInterface;
import com.bard.gplearning.MainActivity;
import com.bard.gplearning.aidl.aidlbean.Person;

import java.util.ArrayList;
import java.util.List;

public class MyAidlService extends Service {

    /**
     * 这里是服务端 也就是android:process=":aidl" 进程
     *
     * 客户端与服务端绑定时的回调，返回 mIBinder 后客户端就可以通过它远程调用服务端的方法，即实现了通讯
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mPersons = new ArrayList<>();
        Log.e("MyAidlService", "onBind pid="+ Process.myPid());
        return mIBinder;
    }



    private ArrayList<Person> mPersons;

    /**
     * 服务端
     * 创建生成的本地 Binder 对象，实现 AIDL 制定的方法
     */
    private IBinder mIBinder = new IMyAidlInterface.Stub() {

        @Override
        public void addPerson(Person person) throws RemoteException {
            Log.e("MyAidlService", "addPerson pid="+ Process.myPid());
            mPersons.add(person);
        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            Log.e("MyAidlService", "getPersonList pid="+ Process.myPid());
            return mPersons;
        }
    };


//    Messenger serverMessenger = new Messenger(new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg != null && msg.getData() != null && msg.arg1 == ConfigHelper.MSG_ID_CLIENT) {
//                String content = (String) msg.getData().get(ConfigHelper.MSG_CONTENT);  //接收客户端的消息
//                Log.d(TAG, "Message from client: " + content);
//
//                //回复消息给客户端
//                Message replyMsg = Message.obtain();
//                replyMsg.arg1 = ConfigHelper.MSG_ID_SERVER;
//                Bundle bundle = new Bundle();
//                bundle.putString(ConfigHelper.MSG_CONTENT, "听到你的消息了，请说点正经的");
//                replyMsg.setData(bundle);
//
//                try {
//                    msg.replyTo.send(replyMsg);     //回信
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    });
//
//    @Nullable
//    @Override
//    public IBinder onBind(final Intent intent) {
//        return serverMessenger.getBinder();
//    }
}
