package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RefundAPI {

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

        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("event_id")
        @Expose
        private String eventId;
        @SerializedName("transaction_id")
        @Expose
        private String transactionId;
        @SerializedName("stripe_refund_id")
        @Expose
        private String stripeRefundId;
        @SerializedName("stripe_charge_id")
        @Expose
        private String stripeChargeId;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private Integer id;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getStripeRefundId() {
            return stripeRefundId;
        }

        public void setStripeRefundId(String stripeRefundId) {
            this.stripeRefundId = stripeRefundId;
        }

        public String getStripeChargeId() {
            return stripeChargeId;
        }

        public void setStripeChargeId(String stripeChargeId) {
            this.stripeChargeId = stripeChargeId;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }
}