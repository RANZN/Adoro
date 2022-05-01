package com.hm.mmmhmm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsernameModel {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("statusCode")
    private String statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}
