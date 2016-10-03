
package com.bdjobs.wallpaper;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DorV {

    @SerializedName("success")
    @Expose
    private String success;

    /**
     * 
     * @return
     *     The success
     */
    public String getSuccess() {
        return success;
    }

    /**
     * 
     * @param success
     *     The success
     */
    public void setSuccess(String success) {
        this.success = success;
    }

}
