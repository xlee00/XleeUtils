package com.eric.xlee.weather;

public class YaHooWeatherBean extends CityWeatherInfoBean {

    /**
     *
     */
    private static final long serialVersionUID = 283528785356719941L;
    String day;
    String date;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "YaHooWeatherBean [day=" + day + ", date=" + date
                + ", cityName=" + cityName + ", cityId=" + cityId + ", fTemp="
                + fTemp + ", tTemp=" + tTemp + ", weatherInfo=" + weatherInfo
                + ", pTime=" + pTime + ", imag1=" + imag1 + ", imag2=" + imag2
                + "]";
    }

}
