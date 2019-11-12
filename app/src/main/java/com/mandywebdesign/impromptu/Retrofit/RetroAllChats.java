package com.mandywebdesign.impromptu.Retrofit;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroAllChats {

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

        @SerializedName("sender_id")
        @Expose
        private String senderId;
        @SerializedName("receiver_id")
        @Expose
        private String receiverId;
        @SerializedName("event_id")
        @Expose
        private String eventId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("ticket_type")
        @Expose
        private String ticketType;
        @SerializedName("file")
        @Expose
        private String file;
        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("last_message_show")
        @Expose
        private String lastMessageShow;
        @SerializedName("last_message_datetime")
        @Expose
        private String lastMessageDatetime;
        @SerializedName("rating_status")
        @Expose
        private String ratingStatus;
        @SerializedName("booking_status")
        @Expose
        private String bookingStatus;
        @SerializedName("event_status")
        @Expose
        private String eventStatus;
        @SerializedName("event_name")
        @Expose
        private String eventName;
        @SerializedName("booked_by")
        @Expose
        private String bookedBy;

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTicketType() {
            return ticketType;
        }

        public void setTicketType(String ticketType) {
            this.ticketType = ticketType;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getLastMessageShow() {
            return lastMessageShow;
        }

        public void setLastMessageShow(String lastMessageShow) {
            this.lastMessageShow = lastMessageShow;
        }

        public String getLastMessageDatetime() {
            return lastMessageDatetime;
        }

        public void setLastMessageDatetime(String lastMessageDatetime) {
            this.lastMessageDatetime = lastMessageDatetime;
        }

        public String getRatingStatus() {
            return ratingStatus;
        }

        public void setRatingStatus(String ratingStatus) {
            this.ratingStatus = ratingStatus;
        }

        public String getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(String bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        public String getEventStatus() {
            return eventStatus;
        }

        public void setEventStatus(String eventStatus) {
            this.eventStatus = eventStatus;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getBookedBy() {
            return bookedBy;
        }

        public void setBookedBy(String bookedBy) {
            this.bookedBy = bookedBy;
        }

    }
}