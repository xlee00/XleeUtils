//package com.eric.xlee.weather;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.eric.xlee.http.HttpSimple;
//
//
//public class WeatherBiz {
//
//    private static final String TAG = "WeatherBiz";
//    private static final String dbName = "db_weather.db";
//    public static final String DEFAULT_CITYCODE = "101010100"; // 默认北京
//    private static String mCityName = "no_city";
//
//    private static void copyWetherData(Context context) {
//        byte[] buf = new byte[30720]; // 30k
//        try {
//            InputStream is = context.getAssets().open(dbName);// 得到数据库文件的数据流
//            File file = context.getDatabasePath(dbName);
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdir();
//                file.createNewFile();
//            }
//            FileOutputStream os = new FileOutputStream(file);// 得到数据库文件的写入流
//            int count = -1;
//            while ((count = is.read(buf)) != -1) {
//                os.write(buf, 0, count);
//            }
//            is.close();
//            os.close();
//            Log.e(TAG, "copy weather database success");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            buf = null;
//        }
//    }
//
//    /**
//     * 获取默认的城市编号 先取偏好设置，有设置直接返回，如果没有则自动粗略定位，定位失败初始为北京
//     *
//     * @param context
//     * @return
//     */
//    public static String getCityCode(final Context context) {
//        SharedPreferences sp = context.getSharedPreferences(WeatherReceiver.WEATHER_CITY_CODE_NAME,
//                Context.MODE_PRIVATE);
//        String cityCode = sp.getString(WeatherReceiver.CITY_CODE, "0");
//        if (cityCode.equals("0")) {
//            copyWetherData(context);
//            String cityCodeAuto = null;
//            try {
//                cityCodeAuto = getRoughlyLocation(context);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (cityCodeAuto != null) {
//                return cityCodeAuto;
//            } else {
//                return DEFAULT_CITYCODE;
//            }
//        } else {
//            return cityCode;
//        }
//    }
//
//    public static CityWeatherInfoBean getWeatherFromHttp(String cityCode) {
//
//        String url = "http://www.weather.com.cn/data/cityinfo/" + cityCode + ".html";
//        String json = HttpSimple.getContent(url, null);
//        if (json != null) {
//            Log.e(TAG, json);
//            try {
//                CityWeatherInfoBean bean = new CityWeatherInfoBean();
//                JSONObject jsonObject = new JSONObject(json);
//                JSONObject jsonInfro = jsonObject.getJSONObject("weatherinfo");
//                bean.setCityId(jsonInfro.getString("cityid"));
//                bean.setCityName(jsonInfro.getString("city"));
//                bean.setfTemp(jsonInfro.getString("temp1"));
//                bean.settTemp(jsonInfro.getString("temp2"));
//                bean.setWeatherInfo(jsonInfro.getString("weather"));
//                return bean;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 获取当前的粗略位置
//     *
//     * @return
//     */
//    private static String getRoughlyLocation(Context context) {
//        String location[] = new String[4];
//        SQLiteDatabase weatherDb = null;
//        String url = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";
//        String json = HttpSimple.getContent(url, null);
//        if (json == null) {
//            return null;
//        }
//        Log.e(TAG, json);
//        try {
//            JSONObject object = new JSONObject(json);
//            location[0] = object.getString("country");// 国
//            location[1] = object.getString("province");// 省
//            location[2] = object.getString("city");// 市
//            location[3] = object.getString("district");// 区
//            // 将获取到的位置转化为城市编码
//            weatherDb = SQLiteDatabase.openDatabase(context.getDatabasePath(dbName).toString(), null,
//                    SQLiteDatabase.OPEN_READONLY);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;// 如果JSON解析出错直接返回空
//        } catch (Exception e) {
//            e.printStackTrace();
//            // copyWetherData(context);
//            // try {
//            // weatherDb = SQLiteDatabase.openDatabase(context
//            // .getDatabasePath(dbName).toString(), null,
//            // SQLiteDatabase.OPEN_READONLY
//            // );
//            // } catch (Exception e1) {
//            // e1.printStackTrace();
//            // }
//        }
//        if (weatherDb == null) {
//            return null;
//        }
//        // 查询城市编号
//        Cursor cursor = null;
//        if (location[3] != null && !location[3].equals("")) {
//            cursor = weatherDb.query("citys", new String[] { "city_num" }, "name=?", new String[] { location[2] + "."
//                    + location[3] }, null, null, null);
//        } else {
//            try {
//                cursor = weatherDb.query("citys", new String[] { "city_num" }, "name=?", new String[] { location[2] },
//                        null, null, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
//            String citycode = cursor.getString(cursor.getColumnIndex("city_num"));
//            cursor.close();
//            weatherDb.close();
//            return citycode;
//        } else {
//            return null;
//        }
//    }
//
//    public static YaHooWeatherBean getWeatherFromYahoo(final Context context) {
//        try {
//            String city = queryCityName(context);
//            String str = sendGet("http://weather.yahooapis.com/forecastrss", "p=" + CityCode.getCityCode(city) + "&u=c");
//            YaHooWeatherBean weatherBeanYahoo = parseXml(formatString(str));
//            weatherBeanYahoo.setCityName(CityEnglishName.getCityEnglishName(city));
//            return weatherBeanYahoo;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private static String queryCityName(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(WeatherReceiver.WEATHER_CITY_CODE_NAME,
//                Context.MODE_PRIVATE);
//        String cityName = sp.getString(WeatherReceiver.CITY_NAME, "no_city");
//        if (!cityName.equals("no_city")) {
//            mCityName = cityName;
//            return cityName;
//        }
//        // HttpGet get = new HttpGet();
//        try {
//            String jison = HttpSimple.getContent("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip",
//                    null);
//            // get.setURI(new
//            // URI("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip"));
//            // HttpResponse response = new DefaultHttpClient().execute(get);
//
//            JSONObject json = new JSONObject(jison);
//            cityName = json.optString("city");
//            mCityName = cityName;
//            Editor editor = sp.edit();
//            editor.putString(WeatherReceiver.CITY_NAME, cityName);
//            editor.commit();
//            return cityName;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "北京";// 默认北京
//    }
//
//    private static String sendGet(String url, String param) {
//        String result = "";
//        BufferedReader in = null;
//        try {
//            String urlNameString = url + "?" + param;
//            URL realUrl = new URL(urlNameString);
//            // 打开和URL之间的连接
//            URLConnection connection = realUrl.openConnection();
//            // 设置通用的请求属性
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 建立实际的连接
//            connection.connect();
//            // 获取所有响应头字段
//            // Map<String, List<String>> map = connection.getHeaderFields();
//            // // 遍历所有的响应头字段
//            // for (String key : map.keySet()) {
//            // System.out.println(key + "--->" + map.get(key));
//            // }
//            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 使用finally块来关闭输入流
//        finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    // private static String getAverageTemp(Object o) {
//    // if (o instanceof CityWeatherInfoBean) {
//    // CityWeatherInfoBean mCityWeatherInfoBean = (CityWeatherInfoBean) o;
//    // int low, high;
//    // String sLow, sHigh;
//    // sLow = mCityWeatherInfoBean.gettTemp().substring(0,
//    // mCityWeatherInfoBean.getfTemp().length() - 1);
//    // sHigh = mCityWeatherInfoBean.getfTemp().substring(0,
//    // mCityWeatherInfoBean.getfTemp().length() - 1);
//    // Log.i(TAG,
//    // "getfTemp()=" + mCityWeatherInfoBean.getfTemp() + " gettTemp()=" +
//    // mCityWeatherInfoBean.gettTemp());
//    // Log.i(TAG, "sLow=" + sLow + "  sHigh=" + sHigh);
//    // low = Integer.parseInt(sLow);
//    // high = Integer.parseInt(sHigh);
//    // return String.valueOf(((low + high) / 2)) + "°";
//    // } else if (o instanceof YaHooWeatherBean) {
//    // YaHooWeatherBean mWeatherBeanYahoo = (YaHooWeatherBean) o;
//    // int low, high;
//    // String sLow, sHigh;
//    // sLow = mWeatherBeanYahoo.getfTemp();
//    // sHigh = mWeatherBeanYahoo.gettTemp();
//    // low = Integer.parseInt(sLow);
//    // high = Integer.parseInt(sHigh);
//    // return String.valueOf(((low + high) / 2)) + "°";
//    // }
//    // return null;
//    // }
//
//    private static String formatString(String weather) {
//
//        weather = weather.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">")
//                .replaceAll("&quot;", "\\").replaceAll("&apos;", "'");
//        weather = weather.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
//        weather = weather.replace("<?xml version=\"1.0\" encoding=\"utf-16\"?>", "");
//        return weather;
//    }
//
//    private static YaHooWeatherBean parseXml(String xml) {
//        YaHooWeatherBean weatherBeanYahoo = new YaHooWeatherBean();
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            InputStream instream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
//            Document doc = builder.parse(instream);
//            NodeList nl = doc.getElementsByTagName("yweather:forecast");
//            for (int i = 0; i < nl.getLength(); i++) {// 获取第一个
//                weatherBeanYahoo.setWeatherInfo(doc.getElementsByTagName("yweather:forecast").item(i).getAttributes()
//                        .getNamedItem("text").getNodeValue());
//                weatherBeanYahoo.setDay(doc.getElementsByTagName("yweather:forecast").item(i).getAttributes()
//                        .getNamedItem("day").getNodeValue());
//                weatherBeanYahoo.setDate(doc.getElementsByTagName("yweather:forecast").item(i).getAttributes()
//                        .getNamedItem("date").getNodeValue());
//                weatherBeanYahoo.setfTemp(doc.getElementsByTagName("yweather:forecast").item(i).getAttributes()
//                        .getNamedItem("low").getNodeValue()
//                        + "℃");
//                weatherBeanYahoo.settTemp(doc.getElementsByTagName("yweather:forecast").item(i).getAttributes()
//                        .getNamedItem("high").getNodeValue()
//                        + "℃");
//                weatherBeanYahoo.setCityId(doc.getElementsByTagName("yweather:forecast").item(i).getAttributes()
//                        .getNamedItem("code").getNodeValue());
//                weatherBeanYahoo.setCityName(mCityName);
//                break;
//            }
//            instream.close();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        if (weatherBeanYahoo.getfTemp() != null || weatherBeanYahoo.gettTemp() != null) {
//            return weatherBeanYahoo;
//        } else {
//            return null;
//        }
//
//    }
//}
