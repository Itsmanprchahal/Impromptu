package com.mandywebdesign.impromptu.Models;

import java.util.ArrayList;

public class TicketTypeModel {

    String tikcettype;
    int price;
    int numberofticket;


    public TicketTypeModel(String tikcettype, String price, String numberofticket) {
        this.tikcettype = tikcettype;
        this.price = Integer.parseInt(price);
        this.numberofticket = Integer.parseInt(numberofticket);
    }

    public String getTikcettype() {
        return tikcettype;
    }

    public void setTikcettype(String tikcettype) {
        this.tikcettype = tikcettype;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumberofticket() {
        return numberofticket;
    }

    public void setNumberofticket(int numberofticket) {
        this.numberofticket = numberofticket;
    }
}
