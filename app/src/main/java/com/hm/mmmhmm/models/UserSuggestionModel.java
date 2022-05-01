package com.hm.mmmhmm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSuggestionModel {

    @Expose
    @SerializedName("data")
    private List<Data> data;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("statusCode")
    private String statusCode;

    @Expose
    @SerializedName("showpopup")
    private String showpopup;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

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

    public String getShowpopup() {
        return showpopup;
    }

    public void setShowpopup(String showpopup) {
        this.showpopup = showpopup;
    }

    public static class Data {
        @Expose
        @SerializedName("username")
        private String username;
        @Expose
        @SerializedName("id")
        private String id;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }


}
