package com.bdjobs.wallpaper;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rubayet on 03-Oct-16.
 */

public interface DownloadAndView {

    String baseURL = "http://errorstation.com/admin/";

    @GET("viewdownload.php?")
    Call<DorV> CountDownloadOrView(@Query("pid") String pid, @Query("action") String action);

    class Factory {
        public static DownloadAndView downloadAndView;

        public static DownloadAndView getInstance() {
            if (downloadAndView == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(baseURL)
                        .build();
                downloadAndView = retrofit.create(DownloadAndView.class);
                return downloadAndView;
            } else {
                return downloadAndView;
            }
        }
    }
}
