package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroGetProfile {

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

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("type")
        @Expose
        private Object type;
        @SerializedName("charity_number")
        @Expose
        private String charityNumber;
        @SerializedName("profile_number")
        @Expose
        private Integer profileNumber;
        @SerializedName("address1")
        @Expose
        private Object address1;
        @SerializedName("address2")
        @Expose
        private Object address2;
        @SerializedName("postcode")
        @Expose
        private Object postcode;
        @SerializedName("city")
        @Expose
        private Object city;
        @SerializedName("aboutOrganisation")
        @Expose
        private Object aboutOrganisation;
        @SerializedName("webUrl")
        @Expose
        private Object webUrl;
        @SerializedName("facebookUrl")
        @Expose
        private Object facebookUrl;
        @SerializedName("instagramUrl")
        @Expose
        private Object instagramUrl;
        @SerializedName("twitterUrl")
        @Expose
        private Object twitterUrl;
        @SerializedName("avatar")
        @Expose
        private String avatar;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public String getCharityNumber() {
            return charityNumber;
        }

        public void setCharityNumber(String charityNumber) {
            this.charityNumber = charityNumber;
        }

        public Integer getProfileNumber() {
            return profileNumber;
        }

        public void setProfileNumber(Integer profileNumber) {
            this.profileNumber = profileNumber;
        }

        public Object getAddress1() {
            return address1;
        }

        public void setAddress1(Object address1) {
            this.address1 = address1;
        }

        public Object getAddress2() {
            return address2;
        }

        public void setAddress2(Object address2) {
            this.address2 = address2;
        }

        public Object getPostcode() {
            return postcode;
        }

        public void setPostcode(Object postcode) {
            this.postcode = postcode;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getAboutOrganisation() {
            return aboutOrganisation;
        }

        public void setAboutOrganisation(Object aboutOrganisation) {
            this.aboutOrganisation = aboutOrganisation;
        }

        public Object getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(Object webUrl) {
            this.webUrl = webUrl;
        }

        public Object getFacebookUrl() {
            return facebookUrl;
        }

        public void setFacebookUrl(Object facebookUrl) {
            this.facebookUrl = facebookUrl;
        }

        public Object getInstagramUrl() {
            return instagramUrl;
        }

        public void setInstagramUrl(Object instagramUrl) {
            this.instagramUrl = instagramUrl;
        }

        public Object getTwitterUrl() {
            return twitterUrl;
        }

        public void setTwitterUrl(Object twitterUrl) {
            this.twitterUrl = twitterUrl;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

    }
}
