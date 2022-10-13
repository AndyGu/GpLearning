package com.bard.webview.mainprocess;

import android.os.RemoteException;

import com.bard.webview.ICallbackFromMainProcessToWebProcessInterface;
import com.bard.webview.IWebProcessToMainProcessInterface;
import com.bard.webview.command.Command;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class MainProcessCommandsManager extends IWebProcessToMainProcessInterface.Stub {

    private static MainProcessCommandsManager sInstance;
    private static HashMap<String, Command> mCommands = new HashMap<>();

    public static MainProcessCommandsManager getInstance() {
        if (sInstance == null) {
            synchronized (MainProcessCommandsManager.class) {
                sInstance = new MainProcessCommandsManager();
            }
        }
        return sInstance;
    }


    private MainProcessCommandsManager(){
        ServiceLoader<Command> serviceLoader = ServiceLoader.load(Command.class);
        for(Command command : serviceLoader){
            if(!mCommands.containsKey(command.name())){
                mCommands.put(command.name(), command);
            }
        }
    }
    public void executeCommand(String commandName, Map params, ICallbackFromMainProcessToWebProcessInterface callback) {
        if(mCommands.get(commandName) != null){
            mCommands.get(commandName).execute(params, callback);
        }
    }

    @Override
    public void handleWebCommand(String commandName, String jsonParams, ICallbackFromMainProcessToWebProcessInterface callback) throws RemoteException {
        //服务端-主进程，执行Command
        MainProcessCommandsManager.getInstance().executeCommand(commandName, new Gson().fromJson(jsonParams, Map.class), callback);
    }
}
