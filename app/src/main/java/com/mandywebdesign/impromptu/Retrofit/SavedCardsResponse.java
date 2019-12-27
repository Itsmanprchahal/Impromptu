package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavedCardsResponse {

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private Data data;

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
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

        @SerializedName("card_number")
        @Expose
        private String cardNumber;
        @SerializedName("card_sdate")
        @Expose
        private String cardSdate;
        @SerializedName("card_edate")
        @Expose
        private String cardEdate;

        public String getCard_holder_name() {
            return card_holder_name;
        }

        public void setCard_holder_name(String card_holder_name) {
            this.card_holder_name = card_holder_name;
        }

        @SerializedName("card_holder_name")
        @Expose
        private String card_holder_name;

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getCardSdate() {
            return cardSdate;
        }

        public void setCardSdate(String cardSdate) {
            this.cardSdate = cardSdate;
        }

        public String getCardEdate() {
            return cardEdate;
        }

        public void setCardEdate(String cardEdate) {
            this.cardEdate = cardEdate;
        }

    }
}