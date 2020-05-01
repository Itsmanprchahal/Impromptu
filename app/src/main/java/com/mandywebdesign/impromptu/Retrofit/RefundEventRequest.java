package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RefundEventRequest {

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
        @SerializedName("event_name")
        @Expose
        private String eventName;
        @SerializedName("refund_count")
        @Expose
        private Integer refundCount;
        @SerializedName("outstanding_balance")
        @Expose
        private String outstandingBalance;
        @SerializedName("event_end_date")
        @Expose
        private String eventEndDate;
        @SerializedName("event_image")
        @Expose
        private String event_image;

        public String getEvent_image() {
            return event_image;
        }

        public void setEvent_image(String event_image) {
            this.event_image = event_image;
        }

        public Integer getEventId() {
            return eventId;
        }

        public void setEventId(Integer eventId) {
            this.eventId = eventId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public Integer getRefundCount() {
            return refundCount;
        }

        public void setRefundCount(Integer refundCount) {
            this.refundCount = refundCount;
        }

        public String getOutstandingBalance() {
            return outstandingBalance;
        }

        public void setOutstandingBalance(String outstandingBalance) {
            this.outstandingBalance = outstandingBalance;
        }

        public String getEventEndDate() {
            return eventEndDate;
        }

        public void setEventEndDate(String eventEndDate) {
            this.eventEndDate = eventEndDate;
        }

    }
}