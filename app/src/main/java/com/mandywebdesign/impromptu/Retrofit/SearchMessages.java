package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchMessages {

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
        private Integer senderId;
        @SerializedName("receiver_id")
        @Expose
        private Integer receiverId;
        @SerializedName("event_id")
        @Expose
        private Integer eventId;
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
        private Integer count;
        @SerializedName("last_message_show")
        @Expose
        private String lastMessageShow;
        @SerializedName("last_message_datetime")
        @Expose
        private String lastMessageDatetime;
        @SerializedName("rating_status")
        @Expose
        private Integer ratingStatus;
        @SerializedName("booking_status")
        @Expose
        private String bookingStatus;
        @SerializedName("event_status")
        @Expose
        private String eventStatus;

        public Integer getSenderId() {
            return senderId;
        }

        public void setSenderId(Integer senderId) {
            this.senderId = senderId;
        }

        public Integer getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(Integer receiverId) {
            this.receiverId = receiverId;
        }

        public Integer getEventId() {
            return eventId;
        }

        public void setEventId(Integer eventId) {
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

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
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

        public Integer getRatingStatus() {
            return ratingStatus;
        }

        public void setRatingStatus(Integer ratingStatus) {
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

    }
}
