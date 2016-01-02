package com.eric.xlee.util;

/**
 * SettingsApi.java[V 1.0.0]
 * classes : com.xlee.lib.settings.SettingsApi
 * 李志华 Create at 2014年7月30日 下午8:22:40
 */

import java.util.List;

import com.eric.xlee.lib.R;
import com.eric.xlee.log.Toast;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * com.xlee.lib.settings.SettingsApi
 *
 * @author 李志华 <br/>
 *         create at 2014年7月30日 下午8:22:40
 */
public class SettingsUtil {

    /**
     * 高级设置
     *
     * @return
     */
    public static Intent getSeniorSettings() {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        // or intent.setAction(Settings.ACTION_SETTINGS);
        return intent;
    }

    /**
     * 显示设置
     *
     * @return
     */
    public static Intent getScreenSettings() {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.DisplaySettings");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        return intent;
    }

    /**
     * 网速测试
     *
     * @return
     */
    public static Intent getSpeed() {
        return new Intent("myvst.intent.action.SpeedSettingActivity");
    }

    /**
     * wifi设置
     *
     * @return
     */
    public static Intent getWifiSettings() {
        return new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
    }

    /**
     * 移动网络设置
     *
     * @return
     */
    public static Intent getGPRSSettings() {
        return new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
    }

    /**
     * 有线设置
     *
     * @return
     */
    public static Intent getNetSettings() {
        return new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
    }

    /**
     * 音量设置
     *
     * @return
     */
    public static Intent getSoundSettings() {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.SoundSettings");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        return intent;

    }

    /**
     * * 恢复出厂设置 uses-permission android:name="android.permission.MASTER_CLEAR"
     *
     * @param context
     * @return
     */
    public static void sendMasterClear(Context context) {
        context.sendBroadcast(new Intent("Android.intent.action.MASTER_CLEAR"));
    }

    /**
     * 关于手机 Testing
     *
     * @return
     */
    public static Intent getDeviceInfo() {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.TestingSettings");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        return intent;
    }

    /**
     * 清除后台程序
     *
     * @param context
     * @return
     */
    @SuppressWarnings("unused")
    private static boolean cleanMemory(final Context context) {
        try {
            final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
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
                    context.getString(R.string.one_key_cls_ok) + (mi.availMem - availMem) / (1024 * 1024) + "M",
                    Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 经过测试，使用下面字段可以在软件中直接打开相应的系统界面
    // 　　com.android.settings.AccessibeilitySettings 辅助功能设置
    // 　　com.android.settings.ActivityPicker 选择活动
    // 　　com.android.settings.ApnSettings APN设置
    // 　　com.android.settings.ApplicationSettings 应用程序设置
    // 　　com.android.settings.BandMode 设置GSM/UMTS波段
    // 　　com.android.settings.BatteryInfo 电池信息
    // 　　com.android.settings.DateTimeSettings 日期和时间设置
    // 　　com.android.settings.DateTimeSettingsSetupWizard 日期和时间设置
    // 　　com.android.settings.DevelopmentSettings 应用程序设置=》开发设置
    // 　　com.android.settings.DeviceAdminSettings 设备管理器
    // 　　com.android.settings.DeviceInfoSettings 关于手机
    // 　　com.android.settings.Display 显示——设置显示字体大小及预览
    // 　　com.android.settings.DisplaySettings 显示设置
    // 　　com.android.settings.DockSettings 底座设置
    // 　　com.android.settings.IccLockSettings SIM卡锁定设置
    // 　　com.android.settings.InstalledAppDetails 语言和键盘设置
    // 　　com.android.settings.LanguageSettings 语言和键盘设置
    // 　　com.android.settings.LocalePicker 选择手机语言
    // 　　com.android.settings.LocalePickerInSetupWizard 选择手机语言
    // 　　com.android.settings.ManageApplications 已下载（安装）软件列表
    // 　　com.android.settings.MasterClear 恢复出厂设置
    // 　　com.android.settings.MediaFormat 格式化手机闪存
    // 　　com.android.settings.PhysicalKeyboardSettings 设置键盘
    // 　　com.android.settings.PrivacySettings 隐私设置
    // 　　com.android.settings.ProxySelector 代理设置
    // 　　com.android.settings.RadioInfo 手机信息
    // 　　com.android.settings.RunningServices 正在运行的程序（服务）
    // 　　com.android.settings.SecuritySettings 位置和安全设置
    // 　　com.android.settings.Settings 系统设置
    // 　　com.android.settings.SettingsSafetyLegalActivity 安全信息
    // 　　com.android.settings.SoundSettings 声音设置
    // 　　com.android.settings.TestingSettings 测试——显示手机信息、电池信息、使用情况统计、Wifi
    // information、服务信息
    // 　　com.android.settings.TetherSettings 绑定与便携式热点
    // 　　com.android.settings.TextToSpeechSettings 文字转语音设置
    // 　　com.android.settings.UsageStats 使用情况统计
    // 　　com.android.settings.UserDictionarySettings 用户词典
    // 　　com.android.settings.VoiceInputOutputSettings 语音输入与输出设置
    // 　　com.android.settings.WirelessSettings 无线和网络设置
}
