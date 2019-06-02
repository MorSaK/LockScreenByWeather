package com.morsak.lockscreenbyweather.impl;

import com.morsak.lockscreenbyweather.data.WeatherDataBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherImpl {
    @GET("simpleWeather/query?")
    Call<WeatherDataBean> getWeather(@Query("city") String city, @Query("key") String key);
}
