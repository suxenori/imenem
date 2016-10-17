package com.menemi.personobject;

/**
 * Created by Ui-Developer on 10.08.2016.
 */
public class VipPlan {
    int id;
    int price;
    String duration;


    public VipPlan(int id, int price, String duration) {
        this.id = id;
        this.price = price;
        this.duration = duration;

    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }
}
