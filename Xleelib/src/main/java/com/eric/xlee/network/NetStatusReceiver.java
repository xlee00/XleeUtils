package com.eric.xlee.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.eric.xlee.log.LogUtil;


/**
 * 
 * @author hushujun
 * 
 */
public class NetStatusReceiver extends BroadcastReceiver {
    private static final String TAG = "NetStatusReceiver";
    private INetStatusUpdate mNetStatus;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context instanceof INetStatusUpdate) {
            mNetStatus = (INetStatusUpdate) context;
        } else {
            LogUtil.e(TAG,
                    "<<<====The Context show netstatus must implements INetStatus!====>>>");
            return;
        }
        String action = intent.getAction();
        Log.i(TAG, "Action:" + action);
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
                || action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)
                || action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            boolean isBreak = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            Log.i(TAG, "isBreak:" + isBreak);
            if (!isBreak) { // 有网络
                ConnectivityManager mConnMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (mConnMgr != null) {
                    NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
                    if (aActiveInfo != null) {
                        int type = aActiveInfo.getType();
                        if (type == ConnectivityManager.TYPE_ETHERNET) {
                            mNetStatus.netStatusEthernet();
                        } else if (type == ConnectivityManager.TYPE_WIFI) {
                            mNetStatus.netStatusWifi();
                            getSignalLevel(context);
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            mNetStatus.netStatus3G();
                        }
                    }
                } else {
                    mNetStatus.netStatusNone();
                }
            } else { // 无网络
                WifiManager wifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);// 获取Wifi服务
                WifiInfo info = wifiManager.getConnectionInfo();
                if (info != null && info.getNetworkId() != -1) {
                    mNetStatus.netStatusWifi();
                    getSignalLevel(context);
                } else {
                    mNetStatus.netStatusNone();
                }
            }
            // wifi信号强度发生变化
        } else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
            getSignalLevel(context);
        }
    }

    private void getSignalLevel(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getBSSID() != null) {
            // wifi名称
            String ssid = wifiInfo.getSSID();
            // wifi信号强度
            int signalLevel = WifiManager.calculateSignalLevel(
                    wifiInfo.getRssi(), 5);
            // wifi速度
            int speed = wifiInfo.getLinkSpeed();
            // wifi速度单位
            String units = WifiInfo.LINK_SPEED_UNITS;
            mNetStatus.netRSSIChanged(signalLevel);
            Log.d(TAG, "ssid=" + ssid + ",signalLevel=" + signalLevel
                    + ",speed=" + speed + ",units=" + units);
        }
    }

    public interface INetStatusUpdate {
        /**
         * 无线网络状态回调
         */
        public void netStatusWifi();

        /**
         * 有限网络状态回调
         */
        public void netStatusEthernet();

        /**
		 * 
		 */
        public void netStatus3G();

        /**
		 * 
		 */
        // public void netStatus4G();
        /**
         * 网络未连接
         */
        public void netStatusNone();

        /**
         * 无线网络信号强度变化
         * 
         * @param signalLevel
         */
        public void netRSSIChanged(int signalLevel);
    }

}
