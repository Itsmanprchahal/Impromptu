package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateDraft {

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
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("cat_id")
        @Expose
        private Integer catId;
        @SerializedName("b_event_hostname")
        @Expose
        private String bEventHostname;
        @SerializedName("addressline1")
        @Expose
        private String addressline1;
        @SerializedName("addressline2")
        @Expose
        private String addressline2;
        @SerializedName("postcode")
        @Expose
        private String postcode;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time_from")
        @Expose
        private String timeFrom;
        @SerializedName("time_to")
        @Expose
        private String timeTo;
        @SerializedName("attendees_gender")
        @Expose
        private String attendeesGender;
        @SerializedName("attendees_no")
        @Expose
        private String attendeesNo;
        @SerializedName("free_event")
        @Expose
        private String freeEvent;
        @SerializedName("ticket_type")
        @Expose
        private String ticketType;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("no_of_tickets")
        @Expose
        private String noOfTickets;
        @SerializedName("status")
        @Expose
        private String status;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Integer getCatId() {
            return catId;
        }

        public void setCatId(Integer catId) {
            this.catId = catId;
        }

        public String getBEventHostname() {
            return bEventHostname;
        }

        public void setBEventHostname(String bEventHostname) {
            this.bEventHostname = bEventHostname;
        }

        public String getAddressline1() {
            return addressline1;
        }

        public void setAddressline1(String addressline1) {
            this.addressline1 = addressline1;
        }

        public String getAddressline2() {
            return addressline2;
        }

        public void setAddressline2(String addressline2) {
            this.addressline2 = addressline2;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTimeFrom() {
            return timeFrom;
        }

        public void setTimeFrom(String timeFrom) {
            this.timeFrom = timeFrom;
        }

        public String getTimeTo() {
            return timeTo;
        }

        public void setTimeTo(String timeTo) {
            this.timeTo = timeTo;
        }

        public String getAttendeesGender() {
            return attendeesGender;
        }

        public void setAttendeesGender(String attendeesGender) {
            this.attendeesGender = attendeesGender;
        }

        public String getAttendeesNo() {
            return attendeesNo;
        }

        public void setAttendeesNo(String attendeesNo) {
            this.attendeesNo = attendeesNo;
        }

        public String getFreeEvent() {
            return freeEvent;
        }

        public void setFreeEvent(String freeEvent) {
            this.freeEvent = freeEvent;
        }

        public String getTicketType() {
            return ticketType;
        }

        public void setTicketType(String ticketType) {
            this.ticketType = ticketType;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getNoOfTickets() {
            return noOfTickets;
        }

        public void setNoOfTickets(String noOfTickets) {
            this.noOfTickets = noOfTickets;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}
