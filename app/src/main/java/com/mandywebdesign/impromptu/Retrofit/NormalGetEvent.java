package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NormalGetEvent {

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
        @SerializedName("userid")
        @Expose
        private Integer userid;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("favourite")
        @Expose
        private Integer favourite;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("categories_count")
        @Expose
        private Integer categoriesCount;
        @SerializedName("b_event_hostname")
        @Expose
        private String bEventHostname;
        @SerializedName("addressline1")
        @Expose
        private String addressline1;
        @SerializedName("addressline2")
        @Expose
        private String addressline2;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("lattitude")
        @Expose
        private String lattitude;
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
        private String price;
        @SerializedName("no_of_tickets")
        @Expose
        private String noOfTickets;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("event_start_dt")
        @Expose
        private String eventStartDt;
        @SerializedName("event_end_dt")
        @Expose
        private String eventEndDt;
        @SerializedName("event_start_gmt")
        @Expose
        private String eventStartGmt;
        @SerializedName("event_end_gmt")
        @Expose
        private String eventEndGmt;
        @SerializedName("timezone")
        @Expose
        private String timezone;
        @SerializedName("file")
        @Expose
        private String file;

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

        public Integer getFavourite() {
            return favourite;
        }

        public void setFavourite(Integer favourite) {
            this.favourite = favourite;
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

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public Integer getCategoriesCount() {
            return categoriesCount;
        }

        public void setCategoriesCount(Integer categoriesCount) {
            this.categoriesCount = categoriesCount;
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

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLattitude() {
            return lattitude;
        }

        public void setLattitude(String lattitude) {
            this.lattitude = lattitude;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
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

        public String getEventStartDt() {
            return eventStartDt;
        }

        public void setEventStartDt(String eventStartDt) {
            this.eventStartDt = eventStartDt;
        }

        public String getEventEndDt() {
            return eventEndDt;
        }

        public void setEventEndDt(String eventEndDt) {
            this.eventEndDt = eventEndDt;
        }

        public String getEventStartGmt() {
            return eventStartGmt;
        }

        public void setEventStartGmt(String eventStartGmt) {
            this.eventStartGmt = eventStartGmt;
        }

        public String getEventEndGmt() {
            return eventEndGmt;
        }

        public void setEventEndGmt(String eventEndGmt) {
            this.eventEndGmt = eventEndGmt;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

    }

}