package com.example.fardinlab31;

public class item {

    String ID, itemName;
    Double cost;
    long date;

    public item(String ID, String ItemName, double cost, long date){
        this.cost = cost;
        this.date = date;
        this.ID = ID;
        this.itemName = ItemName;
    }
}
