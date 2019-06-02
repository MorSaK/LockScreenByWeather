package com.morsak.lockscreenbyweather.lockscreen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.morsak.lockscreenbyweather.R;
import com.morsak.lockscreenbyweather.data.WeatherDataBean;
import com.morsak.lockscreenbyweather.impl.WeatherImpl;
import com.morsak.lockscreenbyweather.util.Constant;
import com.morsak.lockscreenbyweather.util.TimeUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * if the screen is locked, this Activity will show.
 *
 */
public class LockScreenActivity extends Activity {


    private ImageView iv_key;
    private TextView tv_time;

    public static boolean isLocked = false;
    //天气获取
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private ProgressDialog dialog;
    private String city = "唐山市";
    private WeatherImpl impl;
    //天气获取


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.activity_lock_screen);

        isLocked = true;

        iv_key = (ImageView) findViewById(R.id.iv_key);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setText(TimeUtil.getTime());//时间获取并且显示
        initWeather();
        iv_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                virbate();//解锁时的手机震动
                isLocked = false;//变回原来的未锁定状态
                Toast.makeText(LockScreenActivity.this, "已解锁", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        try {
            startService(new Intent(this, LockScreenService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override//此处是屏蔽Home键
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            // Key code constant: Home key. This key is handled by the framework and is never delivered to applications.
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override//通过重写onBackPressed来屏蔽Back键
    public void onBackPressed() {
        //return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * virbate means that the screen is unlocked success
     */
    private void virbate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }



    //天气获取
    public void initWeather(){
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        initDialog();
        initRetrofit();
        dialog.show();
        //开启定位
        mLocationClient.start();

    }
    private void initDialog()
    {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
    }
    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.juhe.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        impl= retrofit.create(WeatherImpl.class);

    }
    private void requestWeather(String city)
    {
        Call<WeatherDataBean> weatherBeanCall = impl.getWeather(city,Constant.WEATHER_KEY);
        weatherBeanCall.enqueue(new Callback<WeatherDataBean>() {
            @Override
            public void onResponse(Call<WeatherDataBean> call, Response<WeatherDataBean> response) {
                //Log.i(Constans.TAG,response.body().toString());
                WeatherDataBean bean = response.body();

                switch (bean.getResult().getRealtime().getInfo())
                {
                    case "晴":iv_key.setImageDrawable(getResources().getDrawable(R.drawable.sunny));break;
                    case "多云":iv_key.setImageDrawable(getResources().getDrawable(R.drawable.overcast));break;
                    case "阴":iv_key.setImageDrawable(getResources().getDrawable(R.drawable.cloudy));break;

                    case "特大暴雨":
                    case "大暴雨":
                    case "暴雨":
                    case "大雨":
                    case "中雨":
                    case "小雨":
                    case "雨夹雪":
                    case "雷阵雨伴有冰雹":
                    case "雷阵雨":
                    case "小到中雨":
                    case "中到大雨":
                    case "大到暴雨":
                    case "暴雨到大暴雨":
                    case "大暴雨到特大暴雨":
                    case "冻雨":
                    case "阵雨":iv_key.setImageDrawable(getResources().getDrawable(R.drawable.rainy));break;

                    case "小到中雪":
                    case "中到大雪":
                    case "大到暴雪":
                    case "暴雪":
                    case "大雪":
                    case "中雪":
                    case "小雪":
                    case "阵雪":
                    case "雪":iv_key.setImageDrawable(getResources().getDrawable(R.drawable.snowy));break;
                }
                //iv_key.setImageURI("@drawable/key");
//                Calendar cal;
//                cal = Calendar.getInstance();
//                cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//                String hour,minute,second;
//                if (cal.get(Calendar.AM_PM) == 0)
//                    hour = String.valueOf(cal.get(Calendar.HOUR));
//                else
//                    hour = String.valueOf(cal.get(Calendar.HOUR)+12);
//                minute = String.valueOf(cal.get(Calendar.MINUTE));
//                second = String.valueOf(cal.get(Calendar.SECOND));
//                mTvCity.setText(bean.getResult().getCity());
//                mTvUpdateTime.setText(hour+":"+minute+":"+second);
//                mTvCode.setText(bean.getResult().getRealtime().getTemperature());
//                mTvWeather.setText(bean.getResult().getRealtime().getInfo());
//                mTvWind.setText("风向|"+bean.getResult().getRealtime().getDirect());
//                mTvWindCode.setText("风力|"+bean.getResult().getRealtime().getPower());
            }

            @Override
            public void onFailure(Call<WeatherDataBean> call, Throwable t) {
                Log.i(Constant.TAG,"发生未知错误");
            }
        });
    }
    //初始化定位
    private void initLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);//如果没有则不会显示定位位置
        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            dialog.dismiss();
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //String addr = location.getAddrStr();    //获取详细地址信息
            //String country = location.getCountry();    //获取国家
            //String province = location.getProvince();    //获取省份
            //String city = location.getCity();    //获取城市
            //String district = location.getDistrict();    //获取区县
            //String street = location.getStreet();    //获取街道信息
            switch (location.getLocType())
            {
                case BDLocation.TypeGpsLocation:
                case BDLocation.TypeNetWorkLocation:
                case BDLocation.TypeOffLineLocation:
                    city = location.getCity();
                    break;
                case BDLocation.TypeServerError:
                case BDLocation.TypeNetWorkException:
                case BDLocation.TypeCriteriaException:
                    Log.i(Constant.TAG,"发生未知错误");
                    break;
            }
            requestWeather(city.toString().substring(0,city.toString().length()-1));
        }
    }
    //天气获取
}
