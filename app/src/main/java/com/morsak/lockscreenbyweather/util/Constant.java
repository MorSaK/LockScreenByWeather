package com.morsak.lockscreenbyweather.util;

/**
 * Created by Andy on 2016/3/21.
 */
public final class Constant {

    public static final String LOCK_SCREEN_ACTION = "android.intent.lockscreen";

    private Constant() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static final String TAG = "SimpleWeather";
    public static final String WEATHER_KEY = "c7347a30b3a6e34dceeed69ef167977d";

}
