package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroChat {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("event_id")
        @Expose
        private Integer eventId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("user_avatar")
        @Expose
        private String userAvatar;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("sender_id")
        @Expose
        private Integer userId;

        public Integer getEventId() {
            return eventId;
        }

        public void setEventId(Integer eventId) {
            this.eventId = eventId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

    }


}