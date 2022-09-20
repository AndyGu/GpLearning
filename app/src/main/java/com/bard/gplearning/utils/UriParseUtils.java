package com.bard.gplearning.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UriParseUtils {

    private static Uri getUriForFile(Context context, File file){
         return FileProvider.getUriForFile(context, getFileProvider(context), file);
    }

    private static String getFileProvider(Context context) {
        return context.getApplicationInfo().packageName + ".fileprovider";
    }

    public static void installApk(Activity activity, File apkFile){
        if (!apkFile.exists())  return;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startInstallO(activity, apkFile);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startInstallN(activity, apkFile);
            } else {
                startInstall(activity, apkFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        Log.e("installApk","apkFile="+apkFile.length());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Uri fileUri = getUriForFile(activity, apkFile);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        }else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
    }




    /**
     * android1.x-6.x
     */
    private static void startInstall(Activity activity, File apkFile) throws Exception {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(install);
    }

    /**
     * android7.x
     */
    private static void startInstallN(Activity activity, File apkFile) throws Exception {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.setDataAndType(getUriForFile(activity, apkFile), "application/vnd.android.package-archive");
        activity.startActivity(install);
    }


    /**
     * android8.x
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallO(final Activity activity, File apkFile) throws Exception {

        boolean isGranted = activity.getPackageManager().canRequestPackageInstalls();
        if (isGranted) {
            startInstallN(activity, apkFile);//安装应用的逻辑(写自己的就可以)
        } else {
            new AlertDialog.Builder(activity)
                    .setCancelable(false)
                    .setTitle("安装应用需要打开未知来源权限，请去设置中开启权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int w) {
                            //https://blog.csdn.net/changmu175/article/details/78906829
                            Uri packageURI = Uri.parse("package:" + activity.getPackageName());
                            //注意这个是8.0新API
                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                            activity.startActivity(intent);
                        }
                    })
                    .show();
        }




    }


    // 适配android9的安装方法。
    public void install28(Activity activity, File apkFile) {
        PackageInstaller packageInstaller = activity.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams sessionParams
                = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        sessionParams.setSize(apkFile.length());

        int sessionId = createSession(packageInstaller, sessionParams);
        if (sessionId != -1) {
            boolean copySuccess = copyInstallFile(packageInstaller, sessionId, apkFile.getAbsolutePath());
            if (copySuccess) {
                execInstallCommand(packageInstaller, sessionId);
            }
        }
    }

    private int createSession(PackageInstaller packageInstaller,
                              PackageInstaller.SessionParams sessionParams) {
        int sessionId = -1;
        try {
            sessionId = packageInstaller.createSession(sessionParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionId;
    }


    private boolean copyInstallFile(PackageInstaller packageInstaller,
                                    int sessionId, String apkFilePath) {
        InputStream in = null;
        OutputStream out = null;
        PackageInstaller.Session session = null;
        boolean success = false;
        try {
            File apkFile = new File(apkFilePath);
            session = packageInstaller.openSession(sessionId);
            out = session.openWrite("base.apk", 0, apkFile.length());
            in = new FileInputStream(apkFile);
            int total = 0, c;
            byte[] buffer = new byte[65536];
            while ((c = in.read(buffer)) != -1) {
                total += c;
                out.write(buffer, 0, c);
            }
            session.fsync(out);
            Log.i("copyInstallFile", "streamed " + total + " bytes");
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (session != null) {
                session.close();
            }
        }
        return success;
    }


    private void execInstallCommand(PackageInstaller packageInstaller, int sessionId) {
//        PackageInstaller.Session session = null;
//        try {
//            session = packageInstaller.openSession(sessionId);
//            Intent intent = new Intent(this, InstallResultReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
//                    1, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            session.commit(pendingIntent.getIntentSender());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
    }

}
