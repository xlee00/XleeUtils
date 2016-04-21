/**
 * WeatherReceiver.java[V 1.0.0]
 * classes : com.eric.sen5.weater.WeatherReceiver
 * Xlee Create at 2016-4-19 下午4:05:14
 */
package com.eric.xlee.world.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.eric.xlee.log.LogUtil;


/**
 * com.eric.sen5.weater.WeatherReceiver
 *
 * @author Xlee <br/>
 *         create at 2016-4-19 下午4:05:14
 */
public class WeatherReceiver extends BroadcastReceiver {
    private static final String TAG = WeatherReceiver.class.getSimpleName();

//        private void registerWeatherReceiver() {
//        if (null == mWeatherReceiver) {
//            mWeatherReceiver = new WeatherReceiver();
//            mWeatherReceiver.setWeatherUpdateable(new WeatherReceiver.WeatherUpdateable() {
//                @Override
//                public void updateWeather() {
//                    LogUtil.i(TAG, "updating weather");
//                    if (null != mWeatherBiz) {
//                        mWeatherBiz.loadWeather();
//                    } else {
//                        LogUtil.w(TAG, "null == mWeatherBiz");
//                    }
//                }
//            });
//            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_CHANGED);
//            intentFilter.addAction(Intent.ACTION_TIME_TICK);
//            intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
//            intentFilter.addAction(WeatherReceiver.REFRESH_WETAHTER);
//            intentFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
//            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//            registerReceiver(mWeatherReceiver, intentFilter);
//        }
//    }
//
//    private void unregisterWeatherReceiver() {
//        if (null != mWeatherReceiver) {
//            unregisterReceiver(mWeatherReceiver);
//        }
//    }

    public static final String REFRESH_WETAHTER = "com.sen5.eric.xlee.weatherservice.refresh_weather";
    private static final int TIME_TO_UPDATE = 60;// 60 min

    private WeatherUpdateable mWeatherUpdateable;
    private int mTimeTick = 0;

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(REFRESH_WETAHTER) || action.equals(Intent.ACTION_DATE_CHANGED)
                || action.equals(Intent.ACTION_LOCALE_CHANGED) || action.equals(Intent.ACTION_TIME_CHANGED)) {
            if (null != mWeatherUpdateable) {
                mWeatherUpdateable.updateWeather();
            }
        } else if (action.equals(Intent.ACTION_TIME_TICK)) {
            LogUtil.i(TAG, "ACTION_TIME_TICK == " + (mTimeTick + 1));
            if (needUpdate() && null != mWeatherUpdateable) {
                mWeatherUpdateable.updateWeather();
            }
        } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
                || action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)
                || action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            boolean isBreak = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (!isBreak) {// netWork not break;
                ConnectivityManager mConnMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (mConnMgr != null) {
                    NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
                    if (aActiveInfo != null) {
                        int type = aActiveInfo.getType();
                        if (type == ConnectivityManager.TYPE_ETHERNET) {
                            if (null != mWeatherUpdateable) {
                                mWeatherUpdateable.updateWeather();
                            }
                        } else if (type == ConnectivityManager.TYPE_WIFI) {
                            if (null != mWeatherUpdateable) {
                                mWeatherUpdateable.updateWeather();
                            }
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            if (null != mWeatherUpdateable) {
                                mWeatherUpdateable.updateWeather();
                            }
                        }
                    }
                } else {
                    // netStatusNone();
                }
            }
        }
    }

    public void setWeatherUpdateable(WeatherUpdateable weatherUpdateable) {
        mWeatherUpdateable = weatherUpdateable;
    }

    private boolean needUpdate() {
        mTimeTick++;
        if (mTimeTick % TIME_TO_UPDATE == 0) {
            mTimeTick = 0;
            return true;
        }
        return false;
    }

    public interface WeatherUpdateable {
        void updateWeather();
    }


}
