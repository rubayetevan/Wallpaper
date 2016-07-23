
package com.bdjobs.wallpaper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Wallpaper {

    @SerializedName("wallpaper")
    @Expose
    private List<Wallpaper_> wallpaper = new ArrayList<Wallpaper_>();

    /**
     * 
     * @return
     *     The wallpaper
     */
    public List<Wallpaper_> getWallpaper() {
        return wallpaper;
    }

    /**
     * 
     * @param wallpaper
     *     The wallpaper
     */
    public void setWallpaper(List<Wallpaper_> wallpaper) {
        this.wallpaper = wallpaper;
    }

}
