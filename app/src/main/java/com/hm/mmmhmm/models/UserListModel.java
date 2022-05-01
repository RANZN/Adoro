package com.hm.mmmhmm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserListModel {

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
        @SerializedName("totalrecord")
        private String totalrecord;
        @Expose
        @SerializedName("currentpage")
        private String currentpage;
        @Expose
        @SerializedName("userlist")
        private List<Userlist> userlist;

        public String getTotalrecord() {
            return totalrecord;
        }

        public void setTotalrecord(String totalrecord) {
            this.totalrecord = totalrecord;
        }

        public String getCurrentpage() {
            return currentpage;
        }

        public void setCurrentpage(String currentpage) {
            this.currentpage = currentpage;
        }

        public List<Userlist> getUserlist() {
            return userlist;
        }

        public void setUserlist(List<Userlist> userlist) {
            this.userlist = userlist;
        }
    }

    public static class Userlist {
        @Expose
        @SerializedName("message")
        private String message;
        @Expose
        @SerializedName("firebaseUserId")
        private String firebaseUserId;
        @Expose
        @SerializedName("username")
        private String username;
        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("username_user_id")
        private String username_user_id;
        @Expose
        @SerializedName("hexCodeTop")
        private String hexCodeTop;
        @Expose
        @SerializedName("hexCodeBottom")
        private String hexCodeBottom;
        @Expose
        @SerializedName("senderId")
        private String senderId;
        @Expose
        @SerializedName("senderIdfirbase")
        private String senderIdfirbase;
        @Expose
        @SerializedName("messagecount")
        private String messagecount;
        @Expose
        @SerializedName("blockstatus")
        private String blockstatus;
        @Expose
        @SerializedName("blockby")
        private String blockby;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getFirebaseUserId() {
            return firebaseUserId;
        }

        public void setFirebaseUserId(String firebaseUserId) {
            this.firebaseUserId = firebaseUserId;
        }

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

        public String getHexCodeTop() {
            return hexCodeTop;
        }

        public void setHexCodeTop(String hexCodeTop) {
            this.hexCodeTop = hexCodeTop;
        }

        public String getHexCodeBottom() {
            return hexCodeBottom;
        }

        public void setHexCodeBottom(String hexCodeBottom) {
            this.hexCodeBottom = hexCodeBottom;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getSenderIdfirbase() {
            return senderIdfirbase;
        }

        public void setSenderIdfirbase(String senderIdfirbase) {
            this.senderIdfirbase = senderIdfirbase;
        }

        public String getMessagecount() {
            return messagecount;
        }

        public void setMessagecount(String messagecount) {
            this.messagecount = messagecount;
        }

        public String getUsername_user_id() {
            return username_user_id;
        }

        public void setUsername_user_id(String username_user_id) {
            this.username_user_id = username_user_id;
        }


        public String getBlockstatus() {
            return blockstatus;
        }

        public void setBlockstatus(String blockstatus) {
            this.blockstatus = blockstatus;
        }

        public String getBlockby() {
            return blockby;
        }

        public void setBlockby(String blockby) {
            this.blockby = blockby;
        }
    }
}
