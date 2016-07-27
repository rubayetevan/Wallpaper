package com.bdjobs.wallpaper;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Tabriz on 24-May-16.
 */
public interface API {
    String baseURL = "https://www.dropbox.com/s/";

    //https://www.dropbox.com/s/j5bbbdxe3yl1qc8/wallpaper.json?dl=0

    @GET("0u50az92julliam/FetauredWallpaper.json?raw=1")
    Call<Wallpaper> getFeaturedWallpaper();

    @GET("3jlrhpqsct8bghg/AbstractWallpaper.json?raw=1")
    Call<Wallpaper> getAbstractWallpaper();


    @GET("0u50az92julliam/FetauredWallpaper.json?raw=1")
    Call<Wallpaper> getAnimalandBirdsWallpaper();

    @GET("0u50az92julliam/FetauredWallpaper.json?raw=1")
    Call<Wallpaper> getArchitectureWallpaper();

    @GET("0u50az92julliam/FetauredWallpaper.json?raw=1")
    Call<Wallpaper> getBeachWallpaper();


    @GET("0u50az92julliam/FetauredWallpaper.json?raw=1")
    Call<Wallpaper> getBikeWallpaper();

    class Factory {
        public static API api;

        public static API getInstance() {
            if (api == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(baseURL)
                        .build();
                api = retrofit.create(API.class);
                return api;
            } else {
                return api;
            }
        }
    }
}
