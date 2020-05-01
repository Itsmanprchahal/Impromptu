package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RefundList {

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

        @SerializedName("refund_id")
        @Expose
        private Integer refundId;
        @SerializedName("event_id")
        @Expose
        private Integer eventId;
        @SerializedName("event_name")
        @Expose
        private String eventName;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("avatar")
        @Expose
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        @SerializedName("numberoftickets")
        @Expose
        private Integer numberoftickets;
        @SerializedName("ticket_type")
        @Expose
        private String ticketType;
        @SerializedName("ticket_price")
        @Expose
        private String ticketPrice;
        @SerializedName("total_ticket_amount")
        @Expose
        private String totalTicketAmount;
        @SerializedName("reason")
        @Expose
        private String reason;

        public Integer getRefundId() {
            return refundId;
        }

        public void setRefundId(Integer refundId) {
            this.refundId = refundId;
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

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Integer getNumberoftickets() {
            return numberoftickets;
        }

        public void setNumberoftickets(Integer numberoftickets) {
            this.numberoftickets = numberoftickets;
        }

        public String getTicketType() {
            return ticketType;
        }

        public void setTicketType(String ticketType) {
            this.ticketType = ticketType;
        }

        public String getTicketPrice() {
            return ticketPrice;
        }

        public void setTicketPrice(String ticketPrice) {
            this.ticketPrice = ticketPrice;
        }

        public String getTotalTicketAmount() {
            return totalTicketAmount;
        }

        public void setTotalTicketAmount(String totalTicketAmount) {
            this.totalTicketAmount = totalTicketAmount;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

    }
}