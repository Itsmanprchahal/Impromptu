package com.mandywebdesign.impromptu.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckInGuest {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("total_checkin")
    @Expose
    private String checkin_total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCheckin_total() {
        return checkin_total;
    }

    public void setCheckin_total(String checkin_total) {
        this.checkin_total = checkin_total;
    }

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
        @SerializedName("userid")
        @Expose
        private Integer userid;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("file")
        @Expose
        private String file;
        @SerializedName("ticket_type")
        @Expose
        private String ticketType;
        @SerializedName("total_tickets")
        @Expose
        private String  totalTickets;
        @SerializedName("total_tickets_by_user")
        @Expose
        private Integer  total_tickets_by_user;
        @SerializedName("checkin")
        @Expose
        private String checkin;

        public void setTotalTickets(String totalTickets) {
            this.totalTickets = totalTickets;
        }

        public Integer getTotal_tickets_by_user() {
            return total_tickets_by_user;
        }

        public void setTotal_tickets_by_user(Integer total_tickets_by_user) {
            this.total_tickets_by_user = total_tickets_by_user;
        }

        public Integer getEventId() {
            return eventId;
        }

        public void setEventId(Integer eventId) {
            this.eventId = eventId;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getTicketType() {
            return ticketType;
        }

        public void setTicketType(String ticketType) {
            this.ticketType = ticketType;
        }


        public String getCheckin() {
            return checkin;
        }

        public void setCheckin(String checkin) {
            this.checkin = checkin;
        }

    }
}
