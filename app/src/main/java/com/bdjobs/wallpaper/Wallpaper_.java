
package com.bdjobs.wallpaper;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Wallpaper_ {

    @SerializedName("picid")
    @Expose
    private String picid;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("picurl")
    @Expose
    private String picurl;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("downloads")
    @Expose
    private String downloads;

    /**
     * 
     * @return
     *     The picid
     */
    public String getPicid() {
        return picid;
    }

    /**
     * 
     * @param picid
     *     The picid
     */
    public void setPicid(String picid) {
        this.picid = picid;
    }

    /**
     * 
     * @return
     *     The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * 
     * @param thumb
     *     The thumb
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    /**
     * 
     * @return
     *     The picurl
     */
    public String getPicurl() {
        return picurl;
    }

    /**
     * 
     * @param picurl
     *     The picurl
     */
    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    /**
     * 
     * @return
     *     The source
     */
    public String getSource() {
        return source;
    }

    /**
     * 
     * @param source
     *     The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 
     * @return
     *     The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * 
     * @param rating
     *     The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     *     The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 
     * @return
     *     The views
     */
    public String getViews() {
        return views;
    }

    /**
     * 
     * @param views
     *     The views
     */
    public void setViews(String views) {
        this.views = views;
    }

    /**
     * 
     * @return
     *     The downloads
     */
    public String getDownloads() {
        return downloads;
    }

    /**
     * 
     * @param downloads
     *     The downloads
     */
    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

}
