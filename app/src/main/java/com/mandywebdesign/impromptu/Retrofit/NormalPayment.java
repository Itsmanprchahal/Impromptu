package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NormalPayment {

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

        @SerializedName("balance_transaction: ")
        @Expose
        private String balanceTransaction;
        @SerializedName("amount: ")
        @Expose
        private String amount;
        @SerializedName("currency: ")
        @Expose
        private String currency;
        @SerializedName("paid: ")
        @Expose
        private Boolean paid;
        @SerializedName("status: ")
        @Expose
        private String status;
        @SerializedName("event_id:")
        @Expose
        private String eventId;
        @SerializedName("userid:")
        @Expose
        private Integer userid;
        @SerializedName("total_tickets:")
        @Expose
        private String totalTickets;

        public String getBalanceTransaction() {
            return balanceTransaction;
        }

        public void setBalanceTransaction(String balanceTransaction) {
            this.balanceTransaction = balanceTransaction;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Boolean getPaid() {
            return paid;
        }

        public void setPaid(Boolean paid) {
            this.paid = paid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public String getTotalTickets() {
            return totalTickets;
        }

        public void setTotalTickets(String totalTickets) {
            this.totalTickets = totalTickets;
        }

    }
}
