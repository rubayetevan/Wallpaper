package com.bdjobs.wallpaper;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Rubayet on 24-May-16.
 */
public interface API {
    String baseURL = "http://appstudio.creativemine.net/hd_wallpaper_pro/API/";

    @GET("FetauredWallpaper.json")
    Call<Wallpaper> getFeaturedWallpaper();

    @GET("AbstractWallpaper.json")
    Call<Wallpaper> getAbstractWallpaper();

    @GET("Animals.json")
    Call<Wallpaper> getAnimalandBirdsWallpaper();

    @GET("Architecture.json")
    Call<Wallpaper> getArchitectureWallpaper();

    @GET("Beach.json")
    Call<Wallpaper> getBeachWallpaper();

    @GET("Bikes.json")
    Call<Wallpaper> getBikeWallpaper();

    @GET("Business.json")
    Call<Wallpaper> getBusinessWallpaper();

    @GET("City.json")
    Call<Wallpaper> getCityWallpaper();

    @GET("Creative_Graphics.json")
    Call<Wallpaper> getCreativeWallpaper();

    @GET("Editor_Picked.json")
    Call<Wallpaper> getEditorWallpaper();

    @GET("Flowers.json")
    Call<Wallpaper> getFlowersWallpaper();

    @GET("Food.json")
    Call<Wallpaper> getFoodWallpaper();

    @GET("Funny.json")
    Call<Wallpaper> getFunnyWallpaper();

    @GET("Games.json")
    Call<Wallpaper> getGamesWallpaper();

    @GET("Inspirational.json")
    Call<Wallpaper> getInspirationalWallpaper();

    @GET("Landscape.json")
    Call<Wallpaper> getLandscapeWallpaper();

    @GET("Macro.json")
    Call<Wallpaper> getMacroWallpaper();

    @GET("Minimal.json")
    Call<Wallpaper> getMinimalWallpaper();

    @GET("Nature.json")
    Call<Wallpaper> getNatureWallpaper();

    @GET("Popular.json")
    Call<Wallpaper> getPopularWallpaper();

    @GET("Space.json")
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
