/**
 *WeaterUtils.java[V 1.0.0]
 *classes : com.eric.sen5.weater.WeaterUtils
 * Xlee Create at 2016-4-18 下午3:08:17
 */
package com.eric.xlee.world.weather;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.eric.xlee.http.HttpSimple;
import com.eric.xlee.http.HttpWorkTask;
import com.eric.xlee.location.IpToLocation;
import com.eric.xlee.log.LogUtil;
import com.google.gson.Gson;

/**
 * com.eric.sen5.weater.WeaterUtils
 * 
 * @author Xlee <br/>
 *         create at 2016-4-18 下午3:08:17
 */
public class WeaterUtils {
    private static final String TAG = WeaterUtils.class.getSimpleName();
    private Gson mGson;
    private CallBack mCallBack;
    private boolean mIsLoading = false;

    public WeaterUtils() {
        mGson = new Gson();
    }

    public void getTemperature(final IpToLocation.Bean bean) {
        if (mIsLoading) {
            LogUtil.w(TAG, "MultiCall getTemperature()");
            return;
        }
        new HttpWorkTask<String>(new HttpWorkTask.ParseCallBack<String>() {
            @Override
            public String onParse() {
                mIsLoading = true;
                return HttpSimple.getContent(getTemperatureUrl(bean), null);
            }
        }, new HttpWorkTask.PostCallBack<String>() {
            @Override
            public void onPost(String netString) {
                mIsLoading = false;
                if (!TextUtils.isEmpty(netString)) {
                    LogUtil.i(TAG, "content == " + netString);
                    if (null != mCallBack) {
                        WeatherBean bean = null;
                        try {
                            bean = mGson.fromJson(netString, WeatherBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mCallBack.onTemperatureUpdate(bean);
                    }
                } else {
                    LogUtil.w(TAG, "onFailure()");
                }
            }
        }).execute();
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    private String getTemperatureUrl(IpToLocation.Bean bean) {
        return getUrlByLocation(bean.getCity(), bean.getRegionName(), bean.getCountry());
    }

    private String getUrlByLocation(String city, String regionName, String country) {
        if (null == city) {
            city = "";
        }
        if (null == regionName) {
            regionName = "";
        }
        if (null == country) {
            country = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22");
        sb.append(city);
        sb.append("%2C");
        sb.append(regionName);
        sb.append("%2c");
        sb.append(country);
        sb.append("%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
        return sb.toString();
    }

    public interface CallBack {
        void onTemperatureUpdate(WeatherBean bean);
    }

    public static String fahrenheitToCentigrade(String strDegree) {
        String result = "";
        try {
            double degree = Double.parseDouble(strDegree);
            result = String.format("%.0f", fahrenheitToCentigrade(degree));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static double fahrenheitToCentigrade(double degree) {
        return Math.round((((double) 5 / 9) * (degree - 32)));
    }

    public static String getImageUrl(String description) {
        description = getCDATAString(description);
        if (!TextUtils.isEmpty(description)) {
            int start = description.indexOf("<img src=\"");
            start += "<img src=\"".length();
            int end = description.indexOf("\"/>");
            if (end > start && 0 <= start && 0 <= end) {
                description = description.substring(start, end);
                LogUtil.i(TAG, "result == " + description);
            }
        }
        return description;
    }

    private static String getCDATAString(String description) {
        if (TextUtils.isEmpty(description)) {
            return "";
        }
        Matcher m = Pattern.compile("<!\\[CDATA\\[([^\\]]+)\\]").matcher(description);
        if (m.find()) {
            String result = m.group(1);
            return result;
        }
        return "";
    }
}
