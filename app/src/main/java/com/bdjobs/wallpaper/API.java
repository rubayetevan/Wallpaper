package com.bdjobs.wallpaper;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Rubayet on 24-May-16.
 */
public interface API {
    String baseURL = "https://www.dropbox.com/s/";

    //https://www.dropbox.com/s/j5bbbdxe3yl1qc8/wallpaper.json?dl=0

    @GET("0u50az92julliam/FetauredWallpaper.json?raw=1")
    Call<Wallpaper> getFeaturedWallpaper();

    @GET("3jlrhpqsct8bghg/AbstractWallpaper.json?raw=1")
    Call<Wallpaper> getAbstractWallpaper();

    @GET("li0kot0dh1wq2tf/Animals.json?raw=1")
    Call<Wallpaper> getAnimalandBirdsWallpaper();

    @GET("l50yo7m4iq93io3/Architecture.json?raw=1")
    Call<Wallpaper> getArchitectureWallpaper();

    @GET("prr4ovj852w6ja9/Beach.json?raw=1")
    Call<Wallpaper> getBeachWallpaper();

    @GET("k4y3jo34mknez9f/Bikes.json?raw=1")
    Call<Wallpaper> getBikeWallpaper();

    @GET("52vk683zdd5enhk/Business.json?raw=1")
    Call<Wallpaper> getBusinessWallpaper();

    @GET("kngsvt225pphx0o/City.json?raw=1")
    Call<Wallpaper> getCityWallpaper();

    @GET("5zroprim12kg10f/Creative_Graphics.json?raw=1")
    Call<Wallpaper> getCreativeWallpaper();

    @GET("o6msowl3bxhshaj/Editor_Picked.json?raw=1")
    Call<Wallpaper> getEditorWallpaper();

    @GET("6yzu43owxx5nhhy/Flowers.json?raw=1")
    Call<Wallpaper> getFlowersWallpaper();

    @GET("qdmqcpst34p6xcm/Food.json?raw=1")
    Call<Wallpaper> getFoodWallpaper();

    @GET("rsybwv2wf9wx8yt/Funny.json?raw=1")
    Call<Wallpaper> getFunnyWallpaper();

    @GET("a2s8zmskkwq01eo/Games.json?raw=1")
    Call<Wallpaper> getGamesWallpaper();

    @GET("vh6h0sjfjngzdyu/Inspirational.json?raw=1")
    Call<Wallpaper> getInspirationalWallpaper();

    @GET("c4tbbkrpqnlqedn/Landscape.json?raw=1")
    Call<Wallpaper> getLandscapeWallpaper();

    @GET("cvv2mja6ycsh1lz/Macro.json?raw=1")
    Call<Wallpaper> getMacroWallpaper();

    @GET("mzegs97qi6vn9sw/Minimal.json?raw=1")
    Call<Wallpaper> getMinimalWallpaper();

    @GET("n7a70kk06zygxjk/Nature.json?raw=1")
    Call<Wallpaper> getNatureWallpaper();

    @GET("4adbk84vojkpd2y/Popular.json?raw=1")
    Call<Wallpaper> getPopularWallpaper();

    @GET("d2gb6cd2qunfln7/Space.json?raw=1")
    Call<Wallpaper> getSpaceWallpaper();

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
