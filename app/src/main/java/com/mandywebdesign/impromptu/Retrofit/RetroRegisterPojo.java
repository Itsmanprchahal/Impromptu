package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroRegisterPojo {

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

        @SerializedName("name")
        @Expose
        private List<String> name = null;
        @SerializedName("email")
        @Expose
        private List<String> email = null;
        @SerializedName("charity_number")
        @Expose
        private List<String> charityNumber = null;
        @SerializedName("activation_token")
        @Expose
        private List<String> activationToken = null;

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
        }

        public List<String> getEmail() {
            return email;
        }

        public void setEmail(List<String> email) {
            this.email = email;
        }

        public List<String> getCharityNumber() {
            return charityNumber;
        }

        public void setCharityNumber(List<String> charityNumber) {
            this.charityNumber = charityNumber;
        }

        public List<String> getActivationToken() {
            return activationToken;
        }

        public void setActivationToken(List<String> activationToken) {
            this.activationToken = activationToken;
        }

    }
}