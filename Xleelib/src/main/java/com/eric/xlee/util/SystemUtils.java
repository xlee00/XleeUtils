package com.eric.xlee.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import com.eric.xlee.log.Toast;


public class SystemUtils {

    public static void cleanMemory(Context context) {
        try {
            final ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            final List<RunningAppProcessInfo> list = am
                    .getRunningAppProcesses();
            final MemoryInfo mi = new MemoryInfo();
            am.getMemoryInfo(mi);
            final long availMem = mi.availMem;
            for (RunningAppProcessInfo info : list) {
                if (info.processName.startsWith(context.getPackageName())) {
                    continue;
                }
                String[] pkgs = info.pkgList;
                for (String pkg : pkgs) {
                    am.killBackgroundProcesses(pkg);
                }
            }
            am.getMemoryInfo(mi);
            Toast.makeText(context,
                    "已清理内存" + (mi.availMem - availMem) / (1024 * 1024) + "M",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
