package com.mandywebdesign.impromptu.Models;

import java.util.ArrayList;

public class AllEventsPojo {

   String title;
    int Image;
    String  price;

    public AllEventsPojo(String title, int image, String price) {
        this.title = title;
        Image = image;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
