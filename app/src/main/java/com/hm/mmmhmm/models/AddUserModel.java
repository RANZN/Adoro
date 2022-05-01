package com.hm.mmmhmm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class AddUserModel {

    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("statusCode")
    private String statusCode;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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

    public static class Data {
        @Expose
        @SerializedName("count")
        private String count;
        @Expose
        @SerializedName("updated_at")
        private String updated_at;
        @Expose
        @SerializedName("created_at")
        private String created_at;
        @Expose
        @SerializedName("selected")
        private String selected;
        @Expose
        @SerializedName("hexCodeBottom")
        private String hexCodeBottom;
        @Expose
        @SerializedName("hexCodeTop")
        private String hexCodeTop;
        @Expose
        @SerializedName("firebase_user_id")
        private String firebase_user_id;
        @Expose
        @SerializedName("chatcount")
        private String chatcount;
        @Expose
        @SerializedName("username")
        private String username;
        @Expose
        @SerializedName("user_id")
        private String user_id;
        @Expose
        @SerializedName("id")
        private String id;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }

        public String getHexCodeBottom() {
            return hexCodeBottom;
        }

        public void setHexCodeBottom(String hexCodeBottom) {
            this.hexCodeBottom = hexCodeBottom;
        }

        public String getHexCodeTop() {
            return hexCodeTop;
        }

        public void setHexCodeTop(String hexCodeTop) {
            this.hexCodeTop = hexCodeTop;
        }

        public String getFirebase_user_id() {
            return firebase_user_id;
        }

        public void setFirebase_user_id(String firebase_user_id) {
            this.firebase_user_id = firebase_user_id;
        }

        public String getChatcount() {
            return chatcount;
        }

        public void setChatcount(String chatcount) {
            this.chatcount = chatcount;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
