//package com.eric.xlee.weather;
//
//import java.util.Locale;
//
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//
//import com.eric.xlee.log.LogUtil;
//
//public class WeatherReceiver extends BroadcastReceiver {
//    private static final String TAG = "WeatherReceiver";
//    public static final String BROADCAT_ACTION = "net.myvst.v2.weather.setting";
//    public static final String RESPONSE_WEATHER = "com.mygica.itv52.v1.weatherservice.responseweather";
//    public static final String CITY_CODE = "city_code";
//    public static final String CITY_NAME = "city_name";
//    public static final String WEATHER_CITY_CODE_NAME = "com.vst.launcher_lemoon_weather_city_code_name";
//
//    @Override
//    public void onReceive(final Context context, Intent intent) {
//        String action = intent.getAction();
//        if (action.equals(RESPONSE_WEATHER) || action.equals(Intent.ACTION_DATE_CHANGED)
//                || action.equals(Intent.ACTION_LOCALE_CHANGED) || action.equals(BROADCAT_ACTION)
//                || action.equals(Intent.ACTION_TIME_CHANGED)) {
//            LogUtil.i(TAG, "weather action=" + action);
//            if (intent.getAction().equals(BROADCAT_ACTION)) {
//                String cityCode = intent.getStringExtra(CITY_CODE);
//                String cityName = intent.getStringExtra(CITY_NAME);
//                SharedPreferences cityCodePreferences = context.getSharedPreferences(WEATHER_CITY_CODE_NAME,
//                        Context.MODE_PRIVATE);
//                Editor editor = cityCodePreferences.edit();
//                editor.putString(CITY_CODE, cityCode);
//                editor.putString(CITY_NAME, cityName);
//                editor.commit();
//            }
//            if (context instanceof IWeatherUpdate) {
//                LogUtil.i(TAG, "WeatherReceiver");
//                final IWeatherUpdate listener = (IWeatherUpdate) context;
//                if (Locale.getDefault().getCountry().equals("CN") || Locale.getDefault().getCountry().equals("TW")) {
//                    new HttpWorkTask<CityWeatherInfoBean>(new HttpWorkTask.ParseCallBack<CityWeatherInfoBean>() {
//                        @Override
//                        public CityWeatherInfoBean onParse() {
//                            String code = WeatherBiz.getCityCode(context);
//                            return WeatherBiz.getWeatherFromHttp(code);
//                        }
//                    }, new HttpWorkTask.PostCallBack<CityWeatherInfoBean>() {
//
//                        @Override
//                        public void onPost(CityWeatherInfoBean result) {
//                            if (result != null) {
//                                LogUtil.i(TAG, "updateWeather result=" + result.toString());
//                                listener.updateWeather(result);
//                            }
//                        }
//                    }).execute();
//                } else {
//                    new HttpWorkTask<CityWeatherInfoBean>(new HttpWorkTask.ParseCallBack<CityWeatherInfoBean>() {
//                        @Override
//                        public CityWeatherInfoBean onParse() {
//                            String code = WeatherBiz.getCityCode(context);
//                            return WeatherBiz.getWeatherFromHttp(code);
//                        }
//                    }, new HttpWorkTask.PostCallBack<CityWeatherInfoBean>() {
//
//                        @Override
//                        public void onPost(CityWeatherInfoBean result) {
//                            if (result != null) {
//                                LogUtil.i(TAG, "updateWeather result=" + result.toString());
//                                listener.updateWeather(result);
//                            }
//                        }
//                    }).execute();
//                    new HttpWorkTask<YaHooWeatherBean>(new HttpWorkTask.ParseCallBack<YaHooWeatherBean>() {
//                        @Override
//                        public YaHooWeatherBean onParse() {
//                            return WeatherBiz.getWeatherFromYahoo(context);
//                        }
//                    }, new HttpWorkTask.PostCallBack<YaHooWeatherBean>() {
//
//                        @Override
//                        public void onPost(YaHooWeatherBean result) {
//                            if (result != null) {
//                                listener.updateWeather(result);
//                            }
//                        }
//                    }).execute();
//                }
//            }
//        }
//    }
//
//    public interface IWeatherUpdate {
//        public void updateWeather(CityWeatherInfoBean bean);
//    }
//}
