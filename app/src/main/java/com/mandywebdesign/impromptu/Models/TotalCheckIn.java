package com.mandywebdesign.impromptu.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalCheckIn {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("total")
        @Expose
        private Integer total;
        @SerializedName("checkin_total")
        @Expose
        private Integer checkinTotal;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getCheckinTotal() {
            return checkinTotal;
        }

        public void setCheckinTotal(Integer checkinTotal) {
            this.checkinTotal = checkinTotal;
        }

    }
}
