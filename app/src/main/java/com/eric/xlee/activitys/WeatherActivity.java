/**
 * WeatherActivity.java [V 1.0.0]
 * Classes : com.eric.xlee.activitys.WeatherActivity
 * Xlee Create at 03/01/16 21:07
 */
package com.eric.xlee.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.xlee.log.LogUtil;
import com.eric.xlee.network.NetStatusReceiver;
import com.eric.xlee.utils.R;
import com.eric.xlee.weather.CityWeatherInfoBean;
import com.eric.xlee.weather.WeatherReceiver;

import java.util.Calendar;

/**
 * com.eric.xlee.activitys.WeatherActivity
 *
 * @author Xlee <br/>
 *         Create at 03/01/16 21:07
 */
public class WeatherActivity extends Activity implements NetStatusReceiver.INetStatusUpdate, WeatherReceiver.IWeatherUpdate {
    private static final String TAG = WeatherActivity.class.getSimpleName();

    private WeatherReceiver mWeatherReceiver;
    private NetStatusReceiver mNetStatReceiver;

    private ImageView mIvNetwork;
    private TextView mTxtWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        initViews();
        registNetReceiber();
        registWeatherReceiver();
    }

    protected void registWeatherReceiver() {
        mWeatherReceiver = new WeatherReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WeatherReceiver.RESPONSE_WEATHER);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(WeatherReceiver.BROADCAT_ACTION);
        registerReceiver(mWeatherReceiver, filter);
        sendBroadcast(new Intent(WeatherReceiver.RESPONSE_WEATHER));
        updateWeather(null);
    }

    protected void registNetReceiber() {
        mNetStatReceiver = new NetStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        registerReceiver(mNetStatReceiver, filter);
    }

    private void initViews() {
        mIvNetwork = (ImageView) findViewById(R.id.iv_network);
        mTxtWeather = (TextView) findViewById(R.id.txt_weather);
    }

    @Override
    public void netRSSIChanged(int signalLevel) {
    }

    @Override
    public void netStatusWifi() {
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_net_wifi);
        drawable.setBounds(0, 0, 48, 48);
        mIvNetwork.setImageDrawable(drawable);
    }

    @Override
    public void netStatusEthernet() {
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_net_wired);
        drawable.setBounds(0, 0, 48, 48);
        mIvNetwork.setImageDrawable(drawable);
    }

    @Override
    public void netStatusNone() {
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_net_off);
        drawable.setBounds(0, 0, 48, 48);
        mIvNetwork.setImageDrawable(drawable);
    }

    @Override
    public void netStatus3G() {
    }


    @Override
    public void updateWeather(CityWeatherInfoBean bean) {
        LogUtil.i(TAG, "weather =" + (null != bean ? bean.toString() : "null"));
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date = month + getResources().getString(R.string.month) + day
                + getResources().getString(R.string.date);
        if (null == bean) {
            mTxtWeather.setText(date);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(bean.getCityName());
            sb.append(" ");
            sb.append(date);
            mTxtWeather.setText(sb.toString());
            sb = new StringBuilder();
            sb.append(bean.getWeatherInfo());
            sb.append(" ");
            sb.append(bean.gettTemp());
            sb.append("-");
            sb.append(bean.getfTemp());
            mTxtWeather.setText(sb.toString());
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mWeatherReceiver);
        unregisterReceiver(mNetStatReceiver);
        super.onDestroy();
    }
}
