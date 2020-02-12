package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroGetEventData {

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
        @SerializedName("hostname")
        @Expose
        private String hostname;
        @SerializedName("host-image")
        @Expose
        private String hostImage;
        @SerializedName("favourite")
        @Expose
        private Integer favourite;

        public String getEvent_status() {
            return event_status;
        }

        public void setEvent_status(String event_status) {
            this.event_status = event_status;
        }

        @SerializedName("event_status")
        @Expose
        private String event_status;
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

        public String getEvent_host_type() {
            return event_host_type;
        }

        public void setEvent_host_type(String event_host_type) {
            this.event_host_type = event_host_type;
        }

        @SerializedName("event_host_type")
        @Expose
        private String event_host_type;
        @SerializedName("lattitude")
        @Expose
        private String lattitude;

        public String getLattitude() {
            return lattitude;
        }

        public void setLattitude(String lattitude) {
            this.lattitude = lattitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("city")
        @Expose
        private String city;

        public String getbEventHostname() {
            return bEventHostname;
        }

        public void setbEventHostname(String bEventHostname) {
            this.bEventHostname = bEventHostname;
        }


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

        @SerializedName("transaction_id")
        @Expose
        private String transactionId;

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("link1")
        @Expose
        private String link1;
        @SerializedName("link2")
        @Expose
        private String link2;
        @SerializedName("link3")
        @Expose
        private String link3;
        @SerializedName("file")
        @Expose
        private List<File> file = null;
        @SerializedName("event_start_dt")
        @Expose
        private String eventStartDt;
        @SerializedName("event_end_dt")
        @Expose
        private String eventEndDt;
        @SerializedName("event_start_gmt")
        @Expose
        private String eventStartGmt;

        public String getTickets_booked_by_user() {
            return tickets_booked_by_user;
        }

        public void setTickets_booked_by_user(String tickets_booked_by_user) {
            this.tickets_booked_by_user = tickets_booked_by_user;
        }

        @SerializedName("tickets_booked_by_user")
        @Expose
        private String tickets_booked_by_user;
        @SerializedName("event_end_gmt")
        @Expose
        private String eventEndGmt;
        @SerializedName("timezone")
        @Expose
        private String timezone;
        @SerializedName("follow_status")
        @Expose
        private Integer followStatus;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("event_book")
        @Expose
        private String eventBook;
        @SerializedName("total_event_bookings")
        @Expose
        private Integer totalEventBookings;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("tickets_type")
        @Expose
        private List<TicketsType> ticketsType = null;

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

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public String getHostImage() {
            return hostImage;
        }

        public void setHostImage(String hostImage) {
            this.hostImage = hostImage;
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

        public String getLink1() {
            return link1;
        }

        public void setLink1(String link1) {
            this.link1 = link1;
        }

        public String getLink2() {
            return link2;
        }

        public void setLink2(String link2) {
            this.link2 = link2;
        }

        public String getLink3() {
            return link3;
        }

        public void setLink3(String link3) {
            this.link3 = link3;
        }

        public List<File> getFile() {
            return file;
        }

        public void setFile(List<File> file) {
            this.file = file;
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

        public Integer getFollowStatus() {
            return followStatus;
        }

        public void setFollowStatus(Integer followStatus) {
            this.followStatus = followStatus;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getEventBook() {
            return eventBook;
        }

        public void setEventBook(String eventBook) {
            this.eventBook = eventBook;
        }

        public Integer getTotalEventBookings() {
            return totalEventBookings;
        }

        public void setTotalEventBookings(Integer totalEventBookings) {
            this.totalEventBookings = totalEventBookings;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public List<TicketsType> getTicketsType() {
            return ticketsType;
        }

        public void setTicketsType(List<TicketsType> ticketsType) {
            this.ticketsType = ticketsType;
        }

    }

    public class File {

        @SerializedName("img")
        @Expose
        private List<String> img = null;

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }

    }

    public class TicketsType {

        @SerializedName("ticket_type")
        @Expose
        private String ticketType;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("no_of_tickets")
        @Expose
        private Integer noOfTickets;

        public Integer getBooked_tickets() {
            return booked_tickets;
        }

        public void setBooked_tickets(Integer booked_tickets) {
            this.booked_tickets = booked_tickets;
        }

        @SerializedName("booked_tickets")
        @Expose
        private Integer booked_tickets;

        public String getTicketType() {
            return ticketType;
        }

        public void setTicketType(String ticketType) {
            this.ticketType = ticketType;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getNoOfTickets() {
            return noOfTickets;
        }

        public void setNoOfTickets(Integer noOfTickets) {
            this.noOfTickets = noOfTickets;
        }

    }
}