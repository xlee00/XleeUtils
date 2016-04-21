/**
 * WorldWeaterDemo.java [V 1.0.0]
 * Classes : com.eric.xlee.activitys.WorldWeaterDemo
 * Xlee Create at 2016/4/21 11:46
 */
package com.eric.xlee.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

import com.eric.xlee.location.IpToLocation;
import com.eric.xlee.log.LogUtil;
import com.eric.xlee.utils.R;
import com.eric.xlee.world.weather.WeaterUtils;
import com.eric.xlee.world.weather.WeatherBean;
import com.eric.xlee.world.weather.WeatherBiz;
import com.eric.xlee.world.weather.WeatherReceiver;

/**
 * com.eric.xlee.activitys.WorldWeaterDemo
 *
 * @author Xlee <br/>
 *         Create at 2016/4/21 11:46
 */
public class WorldWeaterDemo extends Activity implements WeaterUtils.CallBack, IpToLocation.CallBack {
    private static final String TAG = WorldWeaterDemo.class.getSimpleName();

    private TextView mTxtWeather;
    private WeatherReceiver mWeatherReceiver;
    private WeatherBiz mWeatherBiz = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_weater);
        mTxtWeather = (TextView) findViewById(R.id.txt_weather);
        mWeatherBiz = new WeatherBiz();
        mWeatherBiz.setLocationCallBack(this);
        mWeatherBiz.setWeatherCallBack(this);
        mWeatherBiz.loadWeather();
        registerWeatherReceiver();
    }


    @Override
    protected void onDestroy() {
        unregisterWeatherReceiver();
        super.onDestroy();
    }

    private void registerWeatherReceiver() {
        if (null == mWeatherReceiver) {
            mWeatherReceiver = new WeatherReceiver();
            mWeatherReceiver.setWeatherUpdateable(new WeatherReceiver.WeatherUpdateable() {
                @Override
                public void updateWeather() {
                    LogUtil.i(TAG, "updating weather");
                    if (null != mWeatherBiz) {
                        mWeatherBiz.loadWeather();
                    } else {
                        LogUtil.w(TAG, "null == mWeatherBiz");
                    }
                }
            });
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_CHANGED);
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
            intentFilter.addAction(WeatherReceiver.REFRESH_WETAHTER);
            intentFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            registerReceiver(mWeatherReceiver, intentFilter);
        }
    }

    private void unregisterWeatherReceiver() {
        if (null != mWeatherReceiver) {
            unregisterReceiver(mWeatherReceiver);
        }
    }

    @Override
    public void onLocation(IpToLocation.Bean bean) {
        if (null != bean) {
            StringBuilder sb = new StringBuilder();
            sb.append("City:").append(bean.getCity());
            sb.append(" ").append("Region:").append(bean.getRegion());
            sb.append(" ").append("Country:").append(bean.getCountry());
            mTxtWeather.setText(sb);
        }
    }

    @Override
    public void onTemperatureUpdate(WeatherBean bean) {
        if (null != bean) {
            WeatherBean.QueryBean.ResultsBean.ChannelBean.LocationBean location = bean.getQuery().getResults().getChannel().getLocation();
            WeatherBean.QueryBean.ResultsBean.ChannelBean.ItemBean itemBean = bean.getQuery().getResults().getChannel().getItem();
            StringBuilder sb = new StringBuilder();
            sb.append("Condition:").append(" ").append(itemBean.getCondition().getText());
            sb.append(" ").append("Degree:").append(" ").append(WeaterUtils.fahrenheitToCentigrade(itemBean.getCondition().getTemp()) + "°");
            sb.append(" ").append("Location:").append(" ").append(location.getCity());
            mTxtWeather.setText(sb);
        }
    }
}
