package com.colin.http;


import java.util.Map;

import io.reactivex.Observable;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;


/**
 * Created by Administrator on 2017-9-28.
 */

public interface RxApiService {
    @GET("Mobile/login/{user}")
    Observable<ResponseBody> login(@Path("user") String user);
    @POST
    @Headers("Content-Type: application/json")
    Observable<ResponseBody> getServerData(@Url String url, @Body String requestBody);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFiles(@Url String url, @PartMap Map<String, RequestBody> params);
    @Multipart
    @POST
    Observable<ResponseBody> uploadImages(@Url String url, @PartMap Map<String, RequestBody> params);
}
