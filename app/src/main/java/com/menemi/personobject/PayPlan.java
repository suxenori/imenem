package com.menemi.personobject;

import com.menemi.utils.Utils;

/**
 * Created by Ui-Developer on 10.08.2016.
 */
public class PayPlan {
    int id;
    int price;
    int coins;
    boolean isPopular;

    public PayPlan(int id, int price, int coins, String isPopular) {
        this.id = id;
        this.price = price;
        this.coins = coins;
        this.isPopular = Utils.stringToBool(isPopular);
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getCoins() {
        return coins;
    }

    public boolean isPopular() {
        return isPopular;
    }
}
