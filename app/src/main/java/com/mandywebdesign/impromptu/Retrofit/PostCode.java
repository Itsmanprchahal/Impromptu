package com.mandywebdesign.impromptu.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostCode {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("result")
    @Expose
    private Result result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("postcode")
        @Expose
        private String postcode;
        @SerializedName("quality")
        @Expose
        private Integer quality;
        @SerializedName("eastings")
        @Expose
        private Integer eastings;
        @SerializedName("northings")
        @Expose
        private Integer northings;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("nhs_ha")
        @Expose
        private String nhsHa;
        @SerializedName("longitude")
        @Expose
        private Double longitude;
        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("european_electoral_region")
        @Expose
        private String europeanElectoralRegion;
        @SerializedName("primary_care_trust")
        @Expose
        private String primaryCareTrust;
        @SerializedName("region")
        @Expose
        private String region;
        @SerializedName("lsoa")
        @Expose
        private String lsoa;
        @SerializedName("msoa")
        @Expose
        private String msoa;
        @SerializedName("incode")
        @Expose
        private String incode;
        @SerializedName("outcode")
        @Expose
        private String outcode;
        @SerializedName("parliamentary_constituency")
        @Expose
        private String parliamentaryConstituency;
        @SerializedName("admin_district")
        @Expose
        private String adminDistrict;
        @SerializedName("parish")
        @Expose
        private String parish;
        @SerializedName("admin_county")
        @Expose
        private Object adminCounty;
        @SerializedName("admin_ward")
        @Expose
        private String adminWard;
        @SerializedName("ced")
        @Expose
        private Object ced;
        @SerializedName("ccg")
        @Expose
        private String ccg;
        @SerializedName("nuts")
        @Expose
        private String nuts;
        @SerializedName("codes")
        @Expose
        private Codes codes;

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public Integer getQuality() {
            return quality;
        }

        public void setQuality(Integer quality) {
            this.quality = quality;
        }

        public Integer getEastings() {
            return eastings;
        }

        public void setEastings(Integer eastings) {
            this.eastings = eastings;
        }

        public Integer getNorthings() {
            return northings;
        }

        public void setNorthings(Integer northings) {
            this.northings = northings;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getNhsHa() {
            return nhsHa;
        }

        public void setNhsHa(String nhsHa) {
            this.nhsHa = nhsHa;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public String getEuropeanElectoralRegion() {
            return europeanElectoralRegion;
        }

        public void setEuropeanElectoralRegion(String europeanElectoralRegion) {
            this.europeanElectoralRegion = europeanElectoralRegion;
        }

        public String getPrimaryCareTrust() {
            return primaryCareTrust;
        }

        public void setPrimaryCareTrust(String primaryCareTrust) {
            this.primaryCareTrust = primaryCareTrust;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getLsoa() {
            return lsoa;
        }

        public void setLsoa(String lsoa) {
            this.lsoa = lsoa;
        }

        public String getMsoa() {
            return msoa;
        }

        public void setMsoa(String msoa) {
            this.msoa = msoa;
        }

        public String getIncode() {
            return incode;
        }

        public void setIncode(String incode) {
            this.incode = incode;
        }

        public String getOutcode() {
            return outcode;
        }

        public void setOutcode(String outcode) {
            this.outcode = outcode;
        }

        public String getParliamentaryConstituency() {
            return parliamentaryConstituency;
        }

        public void setParliamentaryConstituency(String parliamentaryConstituency) {
            this.parliamentaryConstituency = parliamentaryConstituency;
        }

        public String getAdminDistrict() {
            return adminDistrict;
        }

        public void setAdminDistrict(String adminDistrict) {
            this.adminDistrict = adminDistrict;
        }

        public String getParish() {
            return parish;
        }

        public void setParish(String parish) {
            this.parish = parish;
        }

        public Object getAdminCounty() {
            return adminCounty;
        }

        public void setAdminCounty(Object adminCounty) {
            this.adminCounty = adminCounty;
        }

        public String getAdminWard() {
            return adminWard;
        }

        public void setAdminWard(String adminWard) {
            this.adminWard = adminWard;
        }

        public Object getCed() {
            return ced;
        }

        public void setCed(Object ced) {
            this.ced = ced;
        }

        public String getCcg() {
            return ccg;
        }

        public void setCcg(String ccg) {
            this.ccg = ccg;
        }

        public String getNuts() {
            return nuts;
        }

        public void setNuts(String nuts) {
            this.nuts = nuts;
        }

        public Codes getCodes() {
            return codes;
        }

        public void setCodes(Codes codes) {
            this.codes = codes;
        }

        public class Codes {

            @SerializedName("admin_district")
            @Expose
            private String adminDistrict;
            @SerializedName("admin_county")
            @Expose
            private String adminCounty;
            @SerializedName("admin_ward")
            @Expose
            private String adminWard;
            @SerializedName("parish")
            @Expose
            private String parish;
            @SerializedName("parliamentary_constituency")
            @Expose
            private String parliamentaryConstituency;
            @SerializedName("ccg")
            @Expose
            private String ccg;
            @SerializedName("ccg_id")
            @Expose
            private String ccgId;
            @SerializedName("ced")
            @Expose
            private String ced;
            @SerializedName("nuts")
            @Expose
            private String nuts;

            public String getAdminDistrict() {
                return adminDistrict;
            }

            public void setAdminDistrict(String adminDistrict) {
                this.adminDistrict = adminDistrict;
            }

            public String getAdminCounty() {
                return adminCounty;
            }

            public void setAdminCounty(String adminCounty) {
                this.adminCounty = adminCounty;
            }

            public String getAdminWard() {
                return adminWard;
            }

            public void setAdminWard(String adminWard) {
                this.adminWard = adminWard;
            }

            public String getParish() {
                return parish;
            }

            public void setParish(String parish) {
                this.parish = parish;
            }

            public String getParliamentaryConstituency() {
                return parliamentaryConstituency;
            }

            public void setParliamentaryConstituency(String parliamentaryConstituency) {
                this.parliamentaryConstituency = parliamentaryConstituency;
            }

            public String getCcg() {
                return ccg;
            }

            public void setCcg(String ccg) {
                this.ccg = ccg;
            }

            public String getCcgId() {
                return ccgId;
            }

            public void setCcgId(String ccgId) {
                this.ccgId = ccgId;
            }

            public String getCed() {
                return ced;
            }

            public void setCed(String ced) {
                this.ced = ced;
            }

            public String getNuts() {
                return nuts;
            }

            public void setNuts(String nuts) {
                this.nuts = nuts;
            }


        }
    }
}
