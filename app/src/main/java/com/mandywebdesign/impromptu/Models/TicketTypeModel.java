package com.mandywebdesign.impromptu.Models;

import java.util.ArrayList;

public class TicketTypeModel {

    String tikcettype;
    Float price;
    int numberofticket;


    public TicketTypeModel(String tikcettype, Float price, String numberofticket) {
        this.tikcettype = tikcettype;
        this.price = (price);
        this.numberofticket = Integer.parseInt(numberofticket);
    }

    public String getTikcettype() {
        return tikcettype;
    }

    public void setTikcettype(String tikcettype) {
        this.tikcettype = tikcettype;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getNumberofticket() {
        return numberofticket;
    }

    public void setNumberofticket(int numberofticket) {
        this.numberofticket = numberofticket;
    }
}
