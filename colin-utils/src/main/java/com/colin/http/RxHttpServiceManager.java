package com.colin.http;





import com.colin.constant.Constants;

import io.reactivex.android.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2017-9-28.
 */

public class RxHttpServiceManager {
    private static RxHttpServiceManager instance = null;

    public synchronized static RxHttpServiceManager getInstance() {
        return instance != null ? instance : new RxHttpServiceManager();
    }
    private RxHttpServiceManager(){

    }

    private OkHttpClient client = new OkHttpClient()
        .newBuilder()
        .addInterceptor(new HttpLoggingInterceptor()
            .setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))

        .build();
    private Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
       // .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
    private RxApiService mRxApiService = retrofit.create(RxApiService.class);
    public RxApiService getRxApiService(){
        return mRxApiService;
    }
}
