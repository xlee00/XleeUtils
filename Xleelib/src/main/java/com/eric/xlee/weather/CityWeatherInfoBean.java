package com.eric.xlee.weather;

import java.io.Serializable;

public class CityWeatherInfoBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4553664905800791973L;
    String cityName;
    String cityId;
    String fTemp;
    String tTemp;
    String weatherInfo;
    String pTime;

    String imag1;
    String imag2;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getImag1() {
        return imag1;
    }

    public void setImag1(String imag1) {
        this.imag1 = imag1;
    }

    public String getImag2() {
        return imag2;
    }

    public void setImag2(String imag2) {
        this.imag2 = imag2;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getfTemp() {
        return fTemp;
    }

    public void setfTemp(String fTemp) {
        this.fTemp = fTemp;
    }

    public String gettTemp() {
        return tTemp;
    }

    public void settTemp(String tTemp) {
        this.tTemp = tTemp;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    @Override
    public String toString() {
        return "CityWeatherInfoBean [cityName=" + cityName + ", cityId="
                + cityId + ", fTemp=" + fTemp + ", tTemp=" + tTemp
                + ", weatherInfo=" + weatherInfo + ", dnstr=" + pTime
                + ", imag1=" + imag1 + ", imag2=" + imag2 + "]";
    }

}
