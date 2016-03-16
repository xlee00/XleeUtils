package com.eric.xlee.app;

import com.eric.xlee.lib.R;
import com.eric.xlee.log.Toast;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import java.util.List;

public class AppUtils {
    /**
     * 启动一个应用
     * 
     * @param context
     * @param packageName
     * @return 启动成功返回true，失败返回false
     */
    public static boolean bootApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.app_not_exist), Toast.LENGTH_LONG)
                    .show();
        }
        return false;
    }

    /**
     * 启动一个应用
     * 
     * @param context
     * @param packageName
     * @param activityName
     * @return 启动成功返回true，失败返回false
     */
    public static boolean bootApp(Context context, String packageName, String activityName) {
        Intent intent = new Intent("android.intent.action.MAIN");
        ComponentName comp = new ComponentName(packageName, activityName);
        intent.setComponent(comp);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory("android.intent.category.LAUNCHER");
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.app_not_exist), Toast.LENGTH_LONG)
                    .show();
        }
        return false;
    }

    /**
     * 删除一个应用
     * 
     * @param context
     * @param packageName
     */
    public static void deleteApp(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent unIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(unIntent);
    }

    /**
     * 查看应用详情
     * 
     * @param context
     * @param packageName
     * @return 成功true ，失败false
     */
    public static boolean showDetail(Context context, String packageName) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.not_support), Toast.LENGTH_LONG);
        }
        return false;
    }

    /**
     * 
     * @param context
     * @param intent
     */
    public static void startActivity(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.launche_failure), Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * need < uses-permission android:name =“android.permission.GET_TASKS” />
     * 判断是否前台运行
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    /**
     * need < uses-permission android:name =“android.permission.GET_TASKS” />
     * 判断是否前台运行
     */
    public static boolean isRunningForegroundByPackageName(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
